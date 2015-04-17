
package com.gan4x4.greedyalarm.test.android;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;

import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import android.app.Instrumentation.ActivityMonitor;
import android.content.Context;
import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;
import android.test.UiThreadTest;
import android.test.ViewAsserts;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ProgressBar;

import com.gan4x4.greedyalarm.GaWebApi;
import com.gan4x4.greedyalarm.GreedyAlarmService;
import com.gan4x4.greedyalarm.MainActivity;
import com.gan4x4.greedyalarm.MathTaskActivity;
import com.gan4x4.greedyalarm.NewAlarmActivity;
import com.gan4x4.greedyalarm.descriptions.DescriptionForAlarm;
import com.gan4x4.greedyalarm.mock.GaCalendar;
import com.gan4x4.greedyalarm.objects.Alarm;
import com.gan4x4.greedyalarm.objects.TaskMath;
import com.gan4x4.greedyalarm.test.TestHelper;

public class MainActivityAndroidOriginalTest extends
		ActivityInstrumentationTestCase2<MainActivity> {
	
	MainActivity mActivity;
	
	
	//GreedyAlarmService mockService;
	
	//ListView mList;
	private Button btCancel;
	private Button btSetup;
	private Button btSnooze;
	private ProgressBar pbConnect;
	
	private long alarmTime = 10000; //sec
	private long activityAppearTimeout = 10000;
	
	
	
	@Mock
	private GaWebApi mockWebApi;
	private GreedyAlarmService service;
	

	public MainActivityAndroidOriginalTest() {
		this("MainActivityTests");
	}
	
	public MainActivityAndroidOriginalTest(String name) {
		super(name, MainActivity.class);
		//super(MainActivity.class);
		setName(name);
	}
	

	protected void setUp() throws Exception {
		super.setUp();
		
		MockitoAnnotations.initMocks(this);
		final Context c = getInstrumentation().getTargetContext();
		final Intent intent = new Intent(c,GreedyAlarmService.class);     
	    //intent.putExtra("background", true);
		
    	c.startService(intent);
		
		/*
		try {
			this.runTestOnUiThread(new Runnable() {
			        public void run() {
			        	c.startService(intent);        	
			       }
			   });
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		}
		*/
		
		mockWebApi = Mockito.mock(GaWebApi.class);
		
		mActivity = this.getActivity();
		btSetup = (Button) mActivity.findViewById(com.gan4x4.greedyalarm.R.id.button_setup);
		btCancel = (Button) mActivity.findViewById(com.gan4x4.greedyalarm.R.id.button_cancel);
		btSnooze = (Button) mActivity.findViewById(com.gan4x4.greedyalarm.R.id.button_snooze);
		pbConnect = (ProgressBar) mActivity.findViewById(com.gan4x4.greedyalarm.R.id.pb_connect);
		
		
		service = mActivity.ga_service; 
		if (service != null)
		{
			mActivity.ga_service.api = mockWebApi;
			mActivity.ga_service.setSoundVolume(0.1f);
		}
		
		/*
		Intent intent = new Intent();
		intent.putExtra("object","alarm");
		String[] data  = {"name","snooze_interval","preFinishInterval"};
		intent.putExtra("fields", data);
		setActivityIntent(intent);
		mActivity = getActivity();
		mList = (ListView) mActivity.findViewById(com.gan4x4.greedyalarm.R.id.lv_main);
			*/	
		
		
		
		
	}

	protected void tearDown() throws Exception {
		
		GaCalendar.removeFake();
		GreedyAlarmService s = mActivity.ga_service;
		mActivity.finish();
		s.stopSelf();
		super.tearDown();
		
	}
	
	public final void testPreconditions() {
		assertNotNull(mActivity);
		assertNotNull(btSetup);
		assertNotNull(btCancel);
		assertNotNull(btSnooze);
		assertNotNull(pbConnect);
		assertNotNull(service);
		}
	
	@UiThreadTest
	public void assertVisible(View v){
		final View origin = mActivity.getWindow().getDecorView();
		ViewAsserts.assertOnScreen(origin, v);
		assertEquals(v.getVisibility(),View.VISIBLE);
	}
	@UiThreadTest
	public void assertUnvisible(View v){
		assertEquals(" is Visible !",v.getVisibility(),View.GONE);
	}
	
	public void assertNetworkOn(){
		
		assertTrue(mActivity.ga_service.api.isLogged());
		assertTrue(mActivity.ga_service.api.isOnline());
		assertTrue(mActivity.ga_service.api.checkNetwork());
		
	}
	
	public void assertNetworkOff(){
		assertFalse(mActivity.ga_service.api.checkNetwork());
	}
	
	
	public void assertOnlyCancel(){
		assertVisible(btCancel);
		assertUnvisible(btSetup);
		assertUnvisible(btSnooze);
	}
	
	public void assertNoButtons(){
		assertUnvisible(btCancel);
		assertUnvisible(btSetup);
		assertUnvisible(btSnooze);
	}
	
	public void assertOnlySetup(){
		assertUnvisible(btCancel);
		assertVisible(btSetup);
		assertUnvisible(btSnooze);
	}
	
	public void assertSnoozeAndCancel(){
		assertVisible(btCancel);
		assertUnvisible(btSetup);
		assertVisible(btSnooze);
	}

	
	
	public void waitForUpdate(long x ){
		
		//getInstrumentation().waitForIdleSync();
		try {
			Thread.sleep(x);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		}
	}
	
	public void waitForNewAlarmActivity(){
		
		ActivityMonitor monitor = getInstrumentation().addMonitor(NewAlarmActivity.class.getName(), null, false);
		NewAlarmActivity startedActivity = (NewAlarmActivity) monitor.waitForActivityWithTimeout(activityAppearTimeout);
		assertNotNull(startedActivity);
		startedActivity.finish();
		
	}
	
	private GaWebApi setDisabledAlarm(){
		
		GaWebApi mockApi = Mockito.mock(GaWebApi.class);
		String xml_bet = TestHelper.readFileFromAssets(this,"assets/bet.xml");
		String xml_alarm = TestHelper.readFileFromAssets(this,"assets/alarm.xml");
		
		HashMap<String, String> dt = new HashMap<String, String>();
		dt.put("alarm", xml_alarm);
		dt.put("actionbet", xml_bet);
		
		//Mockito.when(mockApi.getAlarmData()).thenReturn(dt);
		Mockito.when(mockApi.isLogged()).thenReturn(true);

		//assertTrue(mockApi.isLogged());
		Mockito.when(mockApi.isOnline()).thenReturn(true);
		Mockito.when(mockApi.checkNetwork()).thenReturn(true);
		
		//HashMap<String, String> dt2 = mockApi.getAlarmData();
		//assertEquals(dt2.get("alarm"),xml_alarm);
		
		return  mockApi;
	}
	
	
	private Alarm getAlarm(long time){
		
		Calendar timeAlarm = GregorianCalendar.getInstance();
		timeAlarm.setTimeInMillis(time*1000);
		Alarm a = new Alarm(timeAlarm.getTimeInMillis());
		assertTrue(TestHelper.getWaitEnd(a)> 0);
		return a;
		
	}
	
	// @param UTC time in seconds
	private void setSystemTime(long time){
		Calendar timeWait = GregorianCalendar.getInstance();
		timeWait.setTimeInMillis(time*1000);
		GaCalendar.setFake(timeWait);
	}
	
	
	private void setAlarm(Alarm a){
		
		String xml = a.saveToXml(); 
		assertTrue(xml.length() > 0);
		
		GaWebApi mockApi = Mockito.mock(GaWebApi.class);
		
		Mockito.when(mockApi.isLogged()).thenReturn(true);
		Mockito.when(mockApi.isOnline()).thenReturn(true);
		Mockito.when(mockApi.checkNetwork()).thenReturn(true);
		
		HashMap<String, String> dt = new HashMap<String, String>();
		dt.put("alarm", xml);
		
		//Mockito.when(mockApi.getAlarmData()).thenReturn(dt);
		mActivity.ga_service.api = mockApi; 
		mActivity.ga_service.initDataOnStart();
	}
	
	public final void testClickCancel_NetworkOk_TasksNo_ActionsNo() {
		
		Alarm a = getAlarm(alarmTime); 
		setSystemTime(1);
		a.turnOn(1);
		setAlarm(a);
		
		assertNetworkOn();

		waitForUpdate(2000);
		// Wait state
		assertOnlyCancel();
		
		Mockito.when(mActivity.ga_service.api.cancelAlarm(a.getId())).thenReturn(GaWebApi.CANCEL_ALARM_ERROR);

		/*
		this.runTestOnUiThread(new Runnable() {
		        public void run() {
		            //Button btnStart = (Button) getActivity().findViewById(R.id.Button01);
		        	 btCancel.performClick();
		        }
		    });
		*/
		
		TouchUtils.clickView(this, btCancel);
		
		waitForUpdate(2000);
		
		assertNoButtons();
		
		assertVisible(pbConnect);
		
		Mockito.when(mActivity.ga_service.api.cancelAlarm(a.getId())).thenReturn(GaWebApi.CANCEL_ALARM_OK);
		
		waitForUpdate(3000); 
		
		assertOnlySetup();
	}
	
	
	public final void testClickCancel_NetworkLongDelay_TasksNo_ActionsNo() {

		// Setup
		Alarm a = getAlarm(alarmTime);
		long cancelTime = 1;
		setSystemTime(cancelTime);
		a.turnOn(1);
		setAlarm(a);
		
		assertNetworkOn();

		waitForUpdate(1000);
		// Wait state
		assertOnlyCancel();
		
		Mockito.when(mActivity.ga_service.api.cancelAlarm(a.getId())).thenReturn(GaWebApi.CANCEL_ALARM_ERROR);
		//
		TouchUtils.clickView(this, btCancel);
		waitForUpdate(1000);

		// Connect
		assertNoButtons();
		assertVisible(pbConnect);

		setSystemTime(cancelTime + DescriptionForAlarm.getConnectionLimit()/1000 +1); // Delay Greater then connection time
		waitForUpdate(3000);

		// Connect and Setup button
		assertOnlySetup();
	
		Mockito.when(mActivity.ga_service.api.cancelAlarm(a.getId())).thenReturn(GaWebApi.CANCEL_ALARM_OK);
		waitForUpdate(3000);
		
		// Off
		assertUnvisible(pbConnect);
		assertOnlySetup();
		
		
	}
	
}
