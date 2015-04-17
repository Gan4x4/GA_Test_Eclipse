
package com.gan4x4.greedyalarm.test.serverside;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.mockito.Matchers.anyFloat;
import static org.mockito.Mockito.*;
import android.accounts.AccountAuthenticatorActivity;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.Instrumentation.ActivityMonitor;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;
import android.test.UiThreadTest;
import android.test.ViewAsserts;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.gan4x4.greedyalarm.GaWebApi;
import com.gan4x4.greedyalarm.GreedyAlarmService;
import com.gan4x4.greedyalarm.MainActivity;
import com.gan4x4.greedyalarm.SplashActivity;
import com.gan4x4.greedyalarm.MathTaskActivity;
import com.gan4x4.greedyalarm.NewAlarmActivity;
import com.gan4x4.greedyalarm.descriptions.DescriptionForAlarm;
import com.gan4x4.greedyalarm.mock.GaCalendar;
import com.gan4x4.greedyalarm.objects.Alarm;
import com.gan4x4.greedyalarm.objects.HttpRequestResponse;
import com.gan4x4.greedyalarm.objects.TaskMath;
import com.gan4x4.greedyalarm.test.TestHelper;
import com.gan4x4.greedyalarm.test.UserAccount;
import com.google.android.gms.common.AccountPicker;
import com.robotium.solo.Condition;
import com.robotium.solo.Solo;

public class SplashActivityAuthRobotiumTest extends
		ActivityInstrumentationTestCase2<SplashActivity> {
	
	SplashActivity mActivity;
	//private long alarmTime = 10000; //sec
	private long wait_time = 10000;
	protected UserAccount user1;
	
	Button btLogin;
	
	@Mock
	//private GaWebApi mockWebApi;
	private GreedyAlarmService service;
	private Solo solo;

	public SplashActivityAuthRobotiumTest() {
		this("SplashActivityTests");
	}
	
	public SplashActivityAuthRobotiumTest(String name) {
		super(name, SplashActivity.class);
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
		
		//mockWebApi = Mockito.mock(GaWebApi.class);
		
		mActivity = this.getActivity();
		solo = new Solo(getInstrumentation(), mActivity);
		//solo = new Solo(getInstrumentation());
		
		btLogin = (Button) mActivity.findViewById(com.gan4x4.greedyalarm.R.id.button_login);
		
		/*
		btSetup = (Button) mActivity.findViewById(com.gan4x4.greedyalarm.R.id.button_setup);
		btCancel = (Button) mActivity.findViewById(com.gan4x4.greedyalarm.R.id.button_cancel);
		btSnooze = (Button) mActivity.findViewById(com.gan4x4.greedyalarm.R.id.button_snooze);
		pbConnect = (ProgressBar) mActivity.findViewById(com.gan4x4.greedyalarm.R.id.pb_connect);
		
		*/
		service = mActivity.ga_service;
				
		service.api.clearCookie();
		if (service != null)
		{
			//mActivity.ga_service.api = mockWebApi;
			//mActivity.ga_service.setSoundVolume(0.01f);
			//TaskMath tm = new TaskMath();
			//tm.setEnabled(false);
			//mActivity.ga_service.SaveDataFromObject(tm);
		}
		assertTrue(solo.waitForActivity(SplashActivity.class,10000));
		//solo.waitForActivity(SplashActivity.class,10000);
		//displayActivity(solo);
		user1 = new UserAccount("greedyalarm1@gmail.com","000000ga1");
		HttpRequestResponse r = service.api.isUserExists(user1.getEmail());
		if (r.getCode() == HttpRequestResponse.CODE_SUCCESS){
			int user_id = Integer.parseInt(r.getDataByKey("user_id"));
			HttpRequestResponse del = service.api.deleteUserFromServer(user_id);
			assertEquals(HttpRequestResponse.CODE_SUCCESS,del.getCode());
		}
		
		assertFalse(isUserExists());

		
	} 

	protected boolean isUserExists(){
		
		HttpRequestResponse r = service.api.isUserExists(user1.getEmail());
		return r.getCode() == HttpRequestResponse.CODE_SUCCESS;
		
	}
	
	protected void tearDown() throws Exception {
		
		//GaCalendar.removeFake();
		GreedyAlarmService s = mActivity.ga_service;
		
		try{
			solo.finishOpenedActivities();
			solo.finalize();
		}
		catch (Throwable ex){
			ex.printStackTrace();
		}
		s.stopSelf();
		super.tearDown();

	}
	
	
	void displayActivity(Solo s){
		String name = s.getCurrentActivity().getComponentName().getClassName();
		Log.d(MainActivity.TAG,"Activity class : "+name);
		
	}
	
	
	// First level Adapters
	//public final void testPreconditions() {
		/*
		assertNotNull(mActivity);
		assertNotNull(btSetup);
		assertNotNull(btCancel);
		assertNotNull(btSnooze);
		assertNotNull(pbConnect);
		assertNotNull(service);
		*/
	//	}
	
	// If useful must be in separate class
	
	@UiThreadTest
	public void assertVisible(View v){
		final View origin = mActivity.getWindow().getDecorView();
		ViewAsserts.assertOnScreen(origin, v);
		//assertEquals(v.getVisibility(),View.VISIBLE);
		assertEquals(" is Visible !",v.isShown(),true);
		
	}
	@UiThreadTest
	public void assertUnvisible(View v){
		//assertEquals(" is Visible !",v.getVisibility(),View.GONE);
		assertEquals(" is Visible !",v.isShown(),false);
	}
	
	public void assertNetworkOn(){
		
		assertTrue(mActivity.ga_service.api.isLogged());
		assertTrue(mActivity.ga_service.api.isOnline());
		assertTrue(mActivity.ga_service.api.checkNetwork());
		
	}
	
	public void assertNetworkOff(){
		assertFalse(mActivity.ga_service.api.checkNetwork());
	}
	
	
	
public boolean waitForViewIsHide(View view){
		
		final View v = view;

	    return solo.waitForCondition(new Condition() {
	        @Override
	        public boolean isSatisfied() {
	        	
	            return v.isShown() == false;
	        }
	    }, 10000);

		
	}

	
	private void assertTextOnScreen(int id){
		
		String messageOk = getInstrumentation().getTargetContext().getString(id);
		assertTrue("Text not found: "+messageOk,solo.searchText(messageOk, true));
		
	}
	
	
//========================================= Second level Adapters ====================================================================================================
	
	
	
	
	
	private void getCurrentActivityName()
	{
		List<String> names = new ArrayList<String>();
		//String fn ="com.google.android.gms.common.account.AccountPickerActivity";
		String fn =".common.account.AccountPickerActivity";
		names.add("AccountPicker");
		names.add(fn);
		names.add("AccountPickerActivity");
		names.add("Выберите аккаунт");
		//names.add(checkActivityName(getInstrumentation().getContext()));
		int tm = 3000;
		for (String s : names){
			if (solo.waitForActivity(s,tm)){
				Log.d(MainActivity.TAG,"Found : "+s);		
			}
			else{
				Log.d(MainActivity.TAG,"Not Found : "+s);
			}
		}
		//names.add(checkActivityName(getInstrumentation().getContext()));
		names.add(checkActivityName(getInstrumentation().getTargetContext()));
		
		try {
			Class<? extends android.app.Activity> launcherActivityClass = (Class<? extends Activity>) Class.forName(fn);
			if (solo.waitForActivity(launcherActivityClass)){
				Log.d(MainActivity.TAG,"Found : "+fn);		
			}
			else{
				Log.d(MainActivity.TAG,"Not Found : "+fn);
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			Log.d(MainActivity.TAG,"Class not found : "+e.getMessage());
		}

		
	}
	
	private String checkActivityName(Context c){
		
		
		ActivityManager am = (ActivityManager) c.getSystemService(Activity.ACTIVITY_SERVICE);
		String packageName = am.getRunningTasks(1).get(0).topActivity.getPackageName();
		
		Log.d(MainActivity.TAG,"Package : "+packageName);
		
		String className = am.getRunningTasks(1).get(0).topActivity.getClassName();
		String Name = am.getRunningTasks(1).get(0).topActivity.getShortClassName();
		//String Name1 = am.getRunningTasks(1).get(0).topActivity.getLocalClassName();
		Log.d(MainActivity.TAG,"Activity class : "+className);
		Log.d(MainActivity.TAG,"Activity class : "+Name);
		//Log.d(MainActivity.TAG,"Activity class : "+Name1);
		return className;
		
		/*
		PackageManager pm = c.getPackageManager();
		RunningAppProcessInfo info = am.getRunningAppProcesses().get(0);
		try { 
			CharSequence c = pm.getApplicationLabel(pm.getApplicationInfo(info.processName, PackageManager.GET_META_DATA)); 
			System.out.println("the label of the app is " + c); Log.w("LABEL", c.toString()); 
			} catch(Exception e) {
				//Name Not FOund Exception 
			} 
		}
		*/
		
	}
	/*
	public Solo getSoloForTopMostActivity(){
		
		//ActivityManager am = (ActivityManager) getInstrumentation().getContext().getSystemService(Activity.ACTIVITY_SERVICE);
		//String packageName = am.getRunningTasks(1).get(0).topActivity.
		
		//Activity a = am.getRunningTasks(1).get(0).topActivity;
		//,(Class<? extends Activity>)am.getRunningTasks(1).get(0).topActivity.getClass());
		return Solo picker = new Solo(getInstrumentation());
		
		
	}
	
	*/
	
// ========================================== ROBOTIUM TESTS ================================================================================		 
	
	
	
	
	public final void testShowAccountPicker() throws InterruptedException{
		solo.waitForDialogToClose(wait_time);
		assertTrue(solo.waitForView(btLogin));
		//solo.wait(50000);
		solo.clickOnView(btLogin);
		
		
		// User Actions
		
		
		assertTrue(solo.waitForActivity(MainActivity.class,180000));
		
		
		//getCurrentActivityName();
		
		//Solo picker = new Solo(getInstrumentation());
		
		//String name = picker.getCurrentActivity().getComponentName().getClassName();
		//Log.d(MainActivity.TAG,"Activity class : "+name);
		
		//solo.waitForText(user1.getEmail());
		//Solo picker = new Solo(getInstrumentation());
		//displayActivity(picker);
		
		
		//solo.clickOnText(user1.getEmail());
		
		//solo.clickOnScreen(200, 300);
		//solo.clickOnButton("ОК"); // Bad id
		
		
		
		
		
		
		
		//solo.wait(50000);
	}
	
	
	
	
	/*
	private void  clickCancel_InWait_NetworkOk_TasksNo_ActionsNo(Alarm a){
		
		assertTrue(solo.waitForView(btCancel));
		
		assertOnlyCancel();
		assertNoIcons();
		assertEquals(mActivity.ga_service.alarmState(),Alarm.WAIT);
		
		TestHelper.setSystemTime(TestHelper.getWaitEnd(a)-1); // Alarm not beep
		
		assertTrue(solo.waitForView(btCancel));
		
		assertOnlyCancel();
		assertNoIcons();
		
		solo.clickOnView(btCancel);
		
		
		checkTurnOffWithNetworkOk(a);
		assertTextOnScreen(com.gan4x4.greedyalarm.R.string.label_alarm_ok);

		
	}
	
	
	public final void testClickCancel_InWait_NetworkOk_TasksNo_ActionsNo(){
		Alarm a = prepareStandardEnabledAlarm();
		clickCancel_InWait_NetworkOk_TasksNo_ActionsNo(a);
	}
	
	
	public final void testClickCancel_InBeep_NetworkOk_TasksNo_ActionsNo(){

		Alarm a = prepareStandardEnabledAlarm();
		
		assertTrue(solo.waitForView(btCancel));
		
		assertOnlyCancel();
		assertNoIcons();
		assertEquals(mActivity.ga_service.alarmState(),Alarm.WAIT);
		
		TestHelper.setSystemTime(TestHelper.getWaitEnd(a)+1); // Alarm  beep
		
		assertTrue(solo.waitForView(btSnooze));
		
		assertNoIcons();
		assertSnoozeAndCancel();
		
		solo.clickOnView(btCancel);
		
		checkTurnOffWithNetworkOk(a);
		assertTextOnScreen(com.gan4x4.greedyalarm.R.string.label_alarm_ok);
	}
	
	
	public final void testClickCancel_InBeep_NetworkOk_TasksYes_ActionsNo(){
		
		Alarm a = getAlarm(alarmTime);
		
		TestHelper.setSystemTime(1); // Before active interval
		
		TaskMath tm = new TaskMath();
		tm.setEnabled(true);
		mActivity.ga_service.SaveDataFromObject(tm);
		
		a.turnOn(1);
		setAlarm(a);
		
		assertNetworkOn();
		
		assertTrue(solo.waitForView(btCancel));
		assertTrue(solo.waitForView(com.gan4x4.greedyalarm.R.id.gv_icons));
		assertOnlyCancel();
		assertEquals(mActivity.ga_service.alarmState(),Alarm.WAIT);
		
		assertHasIcons(1);
		
		TestHelper.setSystemTime(TestHelper.getWaitEnd(a)+1); // Alarm start beep
		
		assertTrue(solo.waitForView(btSnooze));
		assertSnoozeAndCancel();
		
		solo.clickOnButton("Cancel");
		assertTrue(solo.waitForActivity(MathTaskActivity.class));
		
	}

	
	public final void testClickCancel_InSnooze_NetworkOk_TasksYes_ActionsNo(){

		createTaskMath();
		Alarm a = prepareStandardEnabledAlarm();
		
		TestHelper.setSystemTime(TestHelper.getSnoozeEnd(a)-1); // Alarm  beep
		
		assertTrue(solo.waitForView(btCancel));
		assertHasIcons(1);
		assertSnoozeAndCancel();
		
		solo.clickOnView(btSnooze);
		
		waitForViewIsHide(btSnooze);
		assertOnlyCancel();
		
		assertHasIcons(1);
		
		solo.clickOnButton("Cancel");
		assertTrue(solo.waitForActivity(MathTaskActivity.class));
		
	}

	
	
	public void ClickCancel_InSnooze_NetworkOk_TasksNo_ActionsNo(Alarm a){

		TestHelper.setSystemTime(TestHelper.getWaitEnd(a)+1); // Alarm  beep
		assertTrue(solo.waitForView(btCancel));
		assertNoIcons();
		assertSnoozeAndCancel();
		solo.clickOnView(btSnooze);
		waitForViewIsHide(btSnooze);
		assertNoIcons();
		assertOnlyCancel();
		solo.clickOnView(btCancel);
		checkTurnOffWithNetworkOk(a);
		assertTextOnScreen(com.gan4x4.greedyalarm.R.string.label_alarm_ok);
		
	}
	public final void testClickCancel_InSnooze_NetworkOk_TasksNo_ActionsNo(){
		Alarm a = prepareStandardEnabledAlarm();
		ClickCancel_InSnooze_NetworkOk_TasksNo_ActionsNo(a);
	}
	
	
	public void clickCancel_InPreGrab_NetworkOk_TasksNo_ActionsNo(Alarm a){
		TestHelper.setSystemTime(TestHelper.getSnoozeEnd(a)+1); // Alarm beep hard
		
		//Excercise
		assertTrue(solo.waitForView(btCancel));
		
		assertOnlyCancel();
		assertNoIcons();
		//Do
		solo.clickOnView(btCancel);
		//Excercise
		checkTurnOffWithNetworkOk(a);
		assertTextOnScreen(com.gan4x4.greedyalarm.R.string.label_alarm_ok);
		
		
	}
	
	public final void testClickCancel_InPreGrab_NetworkOk_TasksNo_ActionsNo(){
		// Setup
		Alarm a = prepareStandardEnabledAlarm();
		clickCancel_InPreGrab_NetworkOk_TasksNo_ActionsNo(a);
	}
	
	
	public void clickCancel_Grab_NetworkOk_TasksNo_ActionsNo(Alarm a){
		long grab_middle = TestHelper.getPreFinishEnd(a)+(TestHelper.getGrabEnd(a)-TestHelper.getPreFinishEnd(a))/2;
		TestHelper.setSystemTime(grab_middle); // Alarm grab hard
		assertTrue(solo.waitForView(btCancel));
		
		assertEquals(mActivity.ga_service.alarmState(),Alarm.GRAB);
		assertOnlyCancel();
		assertNoIcons();
		
		solo.clickOnView(btCancel);
		solo.waitForView(pbConnect);
		// check for percent
		assertEquals(mActivity.ga_service.getGanAlarm().getCancelTime(),grab_middle*1000); 
		Float percent = mActivity.ga_service.getGanAlarm().getGrabPercent();
		assertTrue("Percent not near 50% : "+percent.toString(),Math.abs(percent - 0.5) < 0.1);
		
		checkGarabWithNetworkOk(a);
		assertTextOnScreen(com.gan4x4.greedyalarm.R.string.label_alarm_trouble);
		
	}
	
	public final void testClickCancel_Grab_NetworkOk_TasksNo_ActionsNo(){
		// Setup
		Alarm a = prepareStandardEnabledAlarm();
		clickCancel_Grab_NetworkOk_TasksNo_ActionsNo(a);
	}

	
	
	public void autoOff_Expired_NetworkOk_TasksNo_ActionsNo(Alarm a){
		
		
		TestHelper.setSystemTime(TestHelper.getGrabEnd(a)+1); // Alarm Expired
		checkGarabWithNetworkOk(a);
		assertTextOnScreen(com.gan4x4.greedyalarm.R.string.label_alarm_trouble);
		
	}
	
	public final void testAutoOff_Expired_NetworkOk_TasksNo_ActionsNo(){
		Alarm a = prepareStandardEnabledAlarm();
		autoOff_Expired_NetworkOk_TasksNo_ActionsNo(a);
	}
	
	
	public final void testMessageAfterTurnOff(){
		Alarm a = prepareStandardEnabledAlarm();
		//autoOff_Expired_NetworkOk_TasksNo_ActionsNo(a);
		clickCancel_Grab_NetworkOk_TasksNo_ActionsNo(a);
		TestHelper.setSystemTime(1);
		mActivity.ga_service.getGanAlarm().turnOn(1);
		a= mActivity.ga_service.getGanAlarm();
		
		ClickCancel_InSnooze_NetworkOk_TasksNo_ActionsNo(a);
		
	}
	*/
}
