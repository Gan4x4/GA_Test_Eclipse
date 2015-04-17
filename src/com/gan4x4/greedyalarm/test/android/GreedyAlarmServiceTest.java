package com.gan4x4.greedyalarm.test.android;

import android.content.Intent;
import android.test.ServiceTestCase;






import com.gan4x4.greedyalarm.GreedyAlarmService;
import com.gan4x4.greedyalarm.objects.GaTask;
import com.gan4x4.greedyalarm.objects.TaskMath;

public class GreedyAlarmServiceTest extends ServiceTestCase<GreedyAlarmServiceNoForeground> {

	
	private GreedyAlarmServiceNoForeground service;
	
	/*
	public GreedyAlarmServiceTest(Class<GreedyAlarmService> serviceClass) {
		super(name, MainActivity.class);
		//super(GreedyAlarmService.class);
		// TODO Auto-generated constructor stub
	}

	
	public GreedyAlarmServiceTest() {
		this("GreedyAlarmServiceTests");
	}
*/	
	public GreedyAlarmServiceTest() {
		super(GreedyAlarmServiceNoForeground.class);
		//super(MainActivity.class);
		//setName(name);
	}
	
	protected void setUp() throws Exception {
		super.setUp();
		Intent intent = new Intent();
		intent.setClass(getContext(), GreedyAlarmServiceNoForeground.class);
		startService(intent);
		
		service = getService();
		assertNotNull(service);
	}
	
	
	public static void setOneEnabledMathTask(GreedyAlarmService s){
		TaskMath tm = new TaskMath();
		assertFalse(tm.getEnabled());
		tm.setEnabled(true);
		assertTrue(tm.getEnabled());
		s.SaveDataFromObject(tm);
	}
	
	public GaTask assertHasUncomletedTask(){
		GaTask t = service.getNextTask();
		assertNotNull(t);
		assertTrue(t.getEnabled());
		assertFalse(t.isCompleted());
		return t;
	}
	
	//================= Tests ================================================================
	
	
	public void testSaveToSp(){
		
		
		TaskMath tm = new TaskMath();
		
		assertFalse(tm.getEnabled());
		tm.setEnabled(true);
		assertTrue(tm.getEnabled());
		
		service.SaveDataFromObject(tm);
		
		TaskMath tm2 = new TaskMath();
		assertFalse(tm2.getEnabled());
		service.loadDataToObject(tm2);
		assertTrue(tm.getEnabled());
		
		
	}
	
	
	public void testTaskLoadManyTimes(){
		
		setOneEnabledMathTask(service);
		GaTask t = assertHasUncomletedTask();
		t.setCompleted(true);
		assertNull(service.getNextTask());
		service.initDataOnStart();
		//GaTask t2 = service.getNextTask();
		assertHasUncomletedTask();
	}
	
	
	public void testInstallRingtones(){
		service.installRingtones();
	}
	
	
}
