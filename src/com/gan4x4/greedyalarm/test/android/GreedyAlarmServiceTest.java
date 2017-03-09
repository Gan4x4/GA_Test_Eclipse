package com.gan4x4.greedyalarm.test.android;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.test.ServiceTestCase;






import com.gan4x4.greedyalarm.GreedyAlarmService;
import com.gan4x4.greedyalarm.objects.GaTask;
import com.gan4x4.greedyalarm.objects.TaskMath;

public class GreedyAlarmServiceTest extends ServiceTestCase<GreedyAlarmServiceNoForeground> {
	private Context mAppContext;
	private GreedyAlarmServiceNoForeground service;
	private SharedPreferences mSettings;
	
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
	
	private void runService(){
		Intent intent = new Intent();
		intent.setClass(getContext(), GreedyAlarmServiceNoForeground.class);
		startService(intent);
		service = getService();
		assertNotNull(service);
	}
	
	protected void setUp() throws Exception {
		super.setUp();
		mAppContext = getSystemContext(); // Default mock context than has empty shared prefs
		mSettings =  PreferenceManager.getDefaultSharedPreferences(mAppContext);
	}
	
	public static void setOneEnabledMathTask(GreedyAlarmService s){
		TaskMath tm = new TaskMath();
		assertFalse(tm.getEnabled());
		tm.setEnabled(true);
		assertTrue(tm.getEnabled());
		s.SaveDataFromObject(tm);
	}
	
	public GaTask assertHasUncomletedTask(){
		runService();
		GaTask t = service.getNextTask();
		assertNotNull(t);
		assertTrue(t.getEnabled());
		assertFalse(t.isCompleted());
		return t;
	}
	
	//================= Tests ================================================================
	
	
	public void testSaveToSp(){
		
		runService();
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
		runService();
		setOneEnabledMathTask(service);
		GaTask t = assertHasUncomletedTask();
		t.setCompleted(true);
		assertNull(service.getNextTask());
		service.initDataOnStart();
		//GaTask t2 = service.getNextTask();
		assertHasUncomletedTask();
	}
	
	/*
	public void testInstallRingtones(){
		service.installRingtones();
	}
	*/
	private void emulateFirstRunAfterInstall(){
		
		SharedPreferences.Editor prefEditor = mSettings.edit();
		prefEditor.clear();
		prefEditor.commit();
		assertEquals(true,mSettings.getBoolean(GreedyAlarmServiceNoForeground.KEY_FIRST_RUN, true));
		Intent intent = new Intent();
		intent.setClass(mAppContext, GreedyAlarmServiceNoForeground.class);
		startService(intent);
		service = getService(); // Init on first start called here
		assertNotNull(service);
		// Check that service use mocked settings
		assertSame(mSettings, service.getSettings()); 
		// Check that first run key set
		assertEquals(false,mSettings.getBoolean(GreedyAlarmServiceNoForeground.KEY_FIRST_RUN, true));
	}
	
	public void testSetDefaultPrefValuesForAlarm(){
		
		
		
		//runService();// Action on first start must be completed
		
		//SharedPreferences settings =  PreferenceManager.getDefaultSharedPreferences(appContext);
		// check that init on first run not set
		// Check that default settings for action_post writed
		//String actPostMessage = appContext.getString(com.gan4x4.greedyalarm.R.string.actionpost_def_message);  
		//assertEquals(actPostMessage,settings.getString("actionpost_message",null));
		// Check that default settings for action_post writed
		
		emulateFirstRunAfterInstall();
		int expected = mAppContext.getResources().getInteger(com.gan4x4.greedyalarm.R.integer.alarm_snooze_def_interval);
		int actual = mSettings.getInt("alarm_snooze_interval",-1);
		//String actual_s = settings.getString("alarm_snooze_interval","ppp");
		assertEquals(expected,actual);
	}
	
	
	public void testSetDefaultPrefValuesForActionPost(){
		/*
		Context appContext = getSystemContext(); // Default mock context than has empty shared prefs
		
		SharedPreferences settings =  PreferenceManager.getDefaultSharedPreferences(appContext);
		SharedPreferences.Editor prefEditor = settings.edit();
		//prefEditor.putBoolean(GreedyAlarmServiceNoForeground.KEY_FIRST_RUN, true);
		prefEditor.clear();
		prefEditor.commit();
		
		//runService();// Action on first start must be completed
		
		//SharedPreferences settings =  PreferenceManager.getDefaultSharedPreferences(appContext);
		// check that init on first run not set
		assertEquals(true,settings.getBoolean(GreedyAlarmServiceNoForeground.KEY_FIRST_RUN, true));
		
		Intent intent = new Intent();
		intent.setClass(appContext, GreedyAlarmServiceNoForeground.class);
		startService(intent);
		service = getService(); // Init on first start called here
		assertNotNull(service);
		// Check that service use mocked settings
		assertSame(settings, service.getSettings()); 
		// Check that first run key set
		assertEquals(false,settings.getBoolean(GreedyAlarmServiceNoForeground.KEY_FIRST_RUN, true));
		// Check that default settings for action_post writed
		 * 
		 * *
		 */
		emulateFirstRunAfterInstall();
		String expected = mAppContext.getString(com.gan4x4.greedyalarm.R.string.actionpost_def_message);  
		String actual = mSettings.getString("actionpost_message",null);
		//assertEquals(actPostMessage,settings.getString("actionpost_message",null));
		assertEquals(expected,actual);
	}
	
	
}
