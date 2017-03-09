package com.gan4x4.greedyalarm.test;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.spy;

import java.lang.reflect.Field;
import java.util.HashMap;

import org.acra.ACRA;
import org.apache.http.HttpStatus;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.gan4x4.greedyalarm.GaActivity;
import com.gan4x4.greedyalarm.GaWebApi;
import com.gan4x4.greedyalarm.GreedyAlarmService;
import com.gan4x4.greedyalarm.MainActivity;
import com.gan4x4.greedyalarm.NewAlarmActivity;
import com.gan4x4.greedyalarm.SplashActivity;
import com.gan4x4.greedyalarm.mock.GaAlarmManager;
import com.gan4x4.greedyalarm.mock.GaCalendar;
import com.gan4x4.greedyalarm.mock.GaWeb;
import com.gan4x4.greedyalarm.objects.Alarm;
import com.gan4x4.greedyalarm.objects.HttpRequestResponse;
import com.gan4x4.greedyalarm.objects.StringMap;
import com.gan4x4.greedyalarm.test.android.GreedyAlarmServiceTest;
import com.robotium.solo.Condition;
import com.robotium.solo.Solo;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.AlarmManager;
import android.app.KeyguardManager;
import android.app.KeyguardManager.KeyguardLock;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import android.test.UiThreadTest;
import android.test.ViewAsserts;
import android.util.Log;
import android.view.View;
import android.widget.Button;



public abstract class GaActivityTest<T extends GaActivity> extends ActivityInstrumentationTestCase2<T>{

	protected T mActivity;
	
	@Mock
	protected GaWebApi mockWebApi ;
	@Mock
	protected AlarmManager mockAlarmManager;
	protected GreedyAlarmService service;
	protected Solo solo;
	
	protected long network_delay = 1000, ui_delay = 2000, max_delay = 10000;
	private boolean mocksInit = false;
	
	// Abstract methods
	public abstract void initActivityControls();
	
	public GaActivityTest(String pkg, Class<T> activityClass) {
		super(pkg, activityClass);
	}

	protected void initMocks(){
		if (mocksInit) return;
		ACRA.setLog(new NoAcraLog());  // Not work :(
		// Mockito setup
		System.setProperty("dexmaker.dexcache",getInstrumentation().getTargetContext().getCacheDir().getPath());
		MockitoAnnotations.initMocks(this);
    	GaAlarmManager.setFake(mockAlarmManager); // To prevent create real Pending intents
    	GaWeb.setFake(mockWebApi);
    	mocksInit = true;
	}
	
	protected void startServiceAndActivity(){
		final Context c = getInstrumentation().getTargetContext();
		final Intent intent = new Intent(c,GreedyAlarmService.class);    
    	c.startService(intent);
		mActivity = this.getActivity();
		solo = new Solo(getInstrumentation(), mActivity);
		service = mActivity.ga_service;
		String display = TestHelper.getDisplayInfo(mActivity);
		Log.d(MainActivity.TAG,"DISPLAY: "+display);
		// Activity must do it independently
		unlockScreen(); 
		//assertTrue(solo.waitForActivity(mActivity.class));
		initActivityControls();
	}
	
	
		
	
	
	protected void setUp() throws Exception {
		super.setUp();
		initMocks(); // If not called in superclass
	}

	private void unlockScreen(){
		KeyguardManager mKeyGuardManager = (KeyguardManager) getInstrumentation().getContext().getSystemService(Context.KEYGUARD_SERVICE);
		KeyguardLock  mLock = mKeyGuardManager.newKeyguardLock(mActivity.getLocalClassName());
		mLock.disableKeyguard();
		//solo.unlockScreen();
	}
	
	protected void tearDown() throws Exception {
		
		
		/*
		GreedyAlarmService s = mActivity.ga_service;
		try{
			solo.finishOpenedActivities();
			solo.finalize();
		}
		catch (Throwable ex){
			ex.printStackTrace();
		}
		
		*/
		//s.stopSelf();
		
		super.tearDown();
		if (mActivity != null){
			if (mActivity.ga_service != null){
				mActivity.ga_service.doExit();
			}
			waitForActivityIsFinished(mActivity);
			waitForServiceStop();
			try{
				solo.finishOpenedActivities();
				solo.finalize();
			}
			catch (Throwable ex){
				ex.printStackTrace();
			}
		}
		GaCalendar.removeFake(); // We must do it after kill the service to prevent start cancelAlarm task in service
		GaAlarmManager.removeFake();
		System.gc();
	}
	
	
	
// ====================== First level Adapters ======================================================================================
	protected String getButtonName(View v){
		String viewName;
		if (v instanceof Button){
			Button b = (Button) v;
			viewName= b.getText().toString();			
		}
		else{
			viewName = getInstrumentation().getTargetContext().getResources().getResourceEntryName(v.getId());
		}
		return viewName;
	}
	
	@UiThreadTest
	public void assertVisible(View v){
		final View origin = mActivity.getWindow().getDecorView();
		ViewAsserts.assertOnScreen(origin, v);
		assertEquals(" is Visible ! "+getButtonName(v),v.isShown(),true);
		
	}

	@UiThreadTest
	public void assertUnvisible(View v){
		assertEquals(" is Visible ! "+getButtonName(v),v.isShown(),false);
	}
	
	
	protected int getResultCode(Activity a) {
		 assertTrue(a.isFinishing());
		  try {
		    Field f = Activity.class.getDeclaredField("mResultCode");
		    f.setAccessible(true);
		    int actualResultCode = (Integer)f.get(a);
		    return actualResultCode;
		    
		  } catch (NoSuchFieldException e) {
		    throw new RuntimeException("Looks like the Android Activity class has changed it's   private fields for mResultCode or mResultData.  Time to update the reflection code.", e);
		  } catch (Exception e) {
		    throw new RuntimeException(e);
		  }
		}
	
	
	
	
	
	// ================================ Robotium ==========================================================================
	protected boolean waitForViewIsHide(View view){
		
		final View v = view;

	    return solo.waitForCondition(new Condition() {
	        @Override
	        public boolean isSatisfied() {
	        	
	            return v.isShown() == false;
	        }
	    }, 10000);

		
	}
	
	
	protected boolean waitForActivityIsFinished(Activity a){
		final Activity activity = a;
	    return solo.waitForCondition(new Condition() {
	        @Override
	        public boolean isSatisfied() {
	            return activity.isFinishing() == true;
	        }
	    }, (int)max_delay);
	}
	
	private boolean isServiceRunning(Class<?> serviceClass){
		Context c = getInstrumentation().getTargetContext();
	    ActivityManager manager = (ActivityManager) c.getSystemService(Context.ACTIVITY_SERVICE);
	    for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
	        if (serviceClass.getName().equals(service.service.getClassName())) {
	            return true;
	        }
	    }
	    return false;
	}
	
	
	protected boolean waitForServiceStop() {
		final Class<?> serviceClass = GreedyAlarmService.class;
		return solo.waitForCondition(new Condition() {
	        @Override
	        public boolean isSatisfied() {
	            return isServiceRunning(serviceClass) == false;
	        }
	    }, (int)max_delay);
	}
	
	// Mockito ============================================================================================================================
	
	
	protected HttpRequestResponse getStubResponseWithAlarm(String xml) throws SecurityException, IllegalArgumentException, NoSuchFieldException, IllegalAccessException{
		
		HttpRequestResponse r = getStubResponse(HttpRequestResponse.CODE_SUCCESS);
		Field f = HttpRequestResponse.class.getDeclaredField("_data");
		f.setAccessible(true);
		StringMap data = new StringMap();
		data.put("alarm", xml);
		f.set(r, data);
		return r;
	}
	
	
	protected HttpRequestResponse getStubResponse(int rc) throws SecurityException, IllegalArgumentException, NoSuchFieldException, IllegalAccessException{
		return getStubResponse(rc,HttpStatus.SC_OK);
	}
	
	
	protected HttpRequestResponse getStubResponse(int rc,int http_code) throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException{
			 String raw_response = TestHelper.readFileFromAssets(this,"assets/response_setup.txt");
			 //HttpRequestResponse response = spy(new HttpRequestResponse(raw_response));
			 HttpRequestResponse response = new HttpRequestResponse(raw_response);
			 response.setHttpCode(http_code); // No http error
			 Field f = HttpRequestResponse.class.getDeclaredField("code");
			 f.setAccessible(true);
			 f.set(response, rc);
			 return response;
		}
	
// =================================================================================================================================
	
	
	protected void assertNetworkOn(){
		
		assertTrue(mActivity.ga_service.api.isLogged());
		assertTrue(mActivity.ga_service.api.isOnline());
		assertTrue(mActivity.ga_service.api.checkNetwork());
		
	}
	
	protected void assertNetworkOff(){
		assertFalse(mActivity.ga_service.api.checkNetwork());
	}
	
	protected Alarm getAlarm(long time){
		Alarm a = TestHelper.getAlarm(time);
		assertTrue(TestHelper.getWaitEnd(a)> 0);
		return a;
		
	}
	
	
	protected void simulateNetwork(){
		//G = Mockito.mock(GaWebApi.class);
		
		Mockito.when(mockWebApi.isLogged()).thenReturn(true);
		Mockito.when(mockWebApi.isOnline()).thenReturn(true);
		Mockito.when(mockWebApi.checkNetwork()).thenReturn(true);
		
		//HashMap<String, String> dt = new HashMap<String, String>();
		//dt.put("alarm", xml);
		
		//Mockito.when(mockApi.getAlarmData()).thenReturn(dt);
		//mActivity.ga_service.api = gaWebApi; 
		
		Alarm dummy = new Alarm (1);
		dummy.turnOff(1);
		String xml = dummy.saveToXml(); 
		
		HttpRequestResponse r;
		try {
			r = getStubResponseWithAlarm(xml);
			Mockito.when(mockWebApi.getAlarmData()).thenReturn(r);
		} catch (Exception e) {
			fail("Can ton create stub responce");
		}
		
		
		mActivity.ga_service.initDataOnStart();
		
	}
	
	
	protected void setAlarm(Alarm a) {
		
		String xml = a.saveToXml(); 
		assertTrue(xml.length() > 0);
		
		GaWebApi mockApi = Mockito.mock(GaWebApi.class);
		
		Mockito.when(mockApi.isLogged()).thenReturn(true);
		Mockito.when(mockApi.isOnline()).thenReturn(true);
		Mockito.when(mockApi.checkNetwork()).thenReturn(true);
		
		//HashMap<String, String> dt = new HashMap<String, String>();
		//StringMap dt = new StringMap();
		//dt.put("alarm", xml);
		
		
		//HttpRequestResponse r = new HttpRequestResponse(xml);
		
		HttpRequestResponse r;
		try {
			r = getStubResponseWithAlarm(xml);
			Mockito.when(mockApi.getAlarmData()).thenReturn(r);
		} catch (Exception e) {
			fail("Can ton create stub responce");
		}
		
		//Mockito.when(mockApi.getAlarmData()).thenReturn(dt);
		
		mActivity.ga_service.api = mockApi; 
		mActivity.ga_service.initDataOnStart();
	}
	
	
	public String getTextFromTargetContext(int id){
		return getInstrumentation().getTargetContext().getString(id);
	}
	
	protected void assertTextOnScreen(int id){
		String messageOk = getTextFromTargetContext(id);
		assertTrue("Text not found: "+messageOk,solo.searchText(messageOk, true));
	}
	
	protected void waitForText(int id){
		String messageOk = getTextFromTargetContext(id);
		assertTrue("Text not found: "+messageOk,solo.waitForText(messageOk,0,ui_delay));
	}

	
	
	protected void createTaskMath(){
		GreedyAlarmServiceTest.setOneEnabledMathTask(mActivity.ga_service);
	}
	
	// Tests
	/*
	public void testPreconditions() {
		assertNotNull(mActivity);
	}
	*/
}
	