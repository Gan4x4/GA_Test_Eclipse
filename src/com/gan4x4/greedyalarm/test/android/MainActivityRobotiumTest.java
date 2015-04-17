
package com.gan4x4.greedyalarm.test.android;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;

import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.mockito.Matchers.anyFloat;
import static org.mockito.Mockito.*;
import android.app.Instrumentation.ActivityMonitor;
import android.content.Context;
import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;
import android.test.UiThreadTest;
import android.test.ViewAsserts;
import android.test.suitebuilder.annotation.SmallTest;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.gan4x4.greedyalarm.GaApplication;
import com.gan4x4.greedyalarm.GaWebApi;
import com.gan4x4.greedyalarm.GreedyAlarmService;
import com.gan4x4.greedyalarm.MainActivity;
import com.gan4x4.greedyalarm.MathTaskActivity;
import com.gan4x4.greedyalarm.NewAlarmActivity;
import com.gan4x4.greedyalarm.descriptions.DescriptionForAlarm;
import com.gan4x4.greedyalarm.mock.GaCalendar;
import com.gan4x4.greedyalarm.objects.Alarm;
import com.gan4x4.greedyalarm.objects.HttpRequestResponse;
import com.gan4x4.greedyalarm.objects.TaskMath;
import com.gan4x4.greedyalarm.test.GaActivityTest;
import com.gan4x4.greedyalarm.test.TestHelper;
import com.robotium.solo.Condition;
import com.robotium.solo.Solo;

public class MainActivityRobotiumTest extends GaActivityTest<MainActivity> {
	
	
	private Button btCancel;
	private Button btSetup;
	private Button btSnooze;
	private ProgressBar pbConnect;
	
	//@Mock
	//HttpRequestResponse good_responce;
	
	private long alarmTime = 10000; //sec

	public MainActivityRobotiumTest() {
		this("MainActivityTests");
	}
	
	public MainActivityRobotiumTest(String name) {
		super(name, MainActivity.class);
		//super(MainActivity.class);
		setName(name);
	}
	

	protected void setUp() throws Exception {
		super.setUp();
		
		//GaApplication.on_test = true;
		
		btSetup = (Button) mActivity.findViewById(com.gan4x4.greedyalarm.R.id.button_setup);
		btCancel = (Button) mActivity.findViewById(com.gan4x4.greedyalarm.R.id.button_cancel);
		btSnooze = (Button) mActivity.findViewById(com.gan4x4.greedyalarm.R.id.button_snooze);
		pbConnect = (ProgressBar) mActivity.findViewById(com.gan4x4.greedyalarm.R.id.pb_connect);
		
		if (service != null)
		{
			
			//Method m = GreedyAlarmService.class.getDeclaredMethod("scheduleInAlarmMAnager", null);
			//Method m = GreedyAlarmService.class.getDeclaredMethod("scheduleInAlarmMAnager");
			//m.setAccessible(true);
			
			
			TaskMath tm = new TaskMath();
			tm.setEnabled(false);
			mActivity.ga_service.SaveDataFromObject(tm);
			mActivity.ga_service.setSoundVolume(0.01f);
		}
		
		
		//HttpRequestResponse r = getStubResponse(HttpRequestResponse.CODE_SUCCESS);
		//Mockito.when(mockWebApi.sendLog(anyString())).thenReturn(r);
		
		//doReturn(true).when(good_responce).isSuccess();	
		//doReturn(good_responce).when(mockWebApi).sendLog(anyString());
		//doReturn(good_responce).when(mockWebApi).sendLog(null);
		
		assertTrue(solo.waitForActivity(MainActivity.class));
		
	}

	@Override
	protected void tearDown() throws Exception {
		
		//GaCalendar.removeFake();
		super.tearDown();
		/*
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
*/
	}
	
	
	public void testDoExitInService(){
		assertTrue(solo.waitForActivity(MainActivity.class));
		solo.clickOnView(btSetup);
		assertTrue(solo.waitForActivity(NewAlarmActivity.class));
		mActivity.ga_service.doExit();
		assertTrue(waitForActivityIsFinished(mActivity));
		assertTrue(waitForServiceStop());
		mActivity.ga_service = null; // for tear down
	}
	
	
	// First level Adapters
	@SmallTest
	public final  void testPreconditions() {
		//testMessageAfterTurnOff();
		//super.testPreconditions();
		assertNotNull(btSetup);
		assertNotNull(btCancel);
		assertNotNull(btSnooze);
		assertNotNull(pbConnect);
		assertNotNull(mActivity);
		assertNotNull(service);
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
	
	
	
	public boolean waitForViewIsHide(View view){
		final View v = view;
	    return solo.waitForCondition(new Condition() {
	        @Override
	        public boolean isSatisfied() {
	        	
	            return v.isShown() == false;
	        }
	    }, (int) max_delay);
	}

	public void checkConnectionScreen(){
		
		assertTrue(solo.waitForView(pbConnect));
		assertVisible(pbConnect);
		assertNoButtons();
		
	}
	
	
	
	
//========================================= Second level Adapters ====================================================================================================
	private Alarm prepareStandardEnabledAlarm(){
		Alarm a = getAlarm(alarmTime);
		TestHelper.setSystemTime(1); // Before active interval
		a.turnOn(1);
		setAlarm(a);
		assertNetworkOn();
		return a;
	}
	
	
	public void checkGarabWithNetworkOk(Alarm a){
		Mockito.when(mActivity.ga_service.api.grabAlarm(anyLong(),anyFloat())).thenAnswer(new Answer<Integer>(){
			@Override
			  public Integer answer(InvocationOnMock invocation) {
				checkConnectionScreen();
				return GaWebApi.GRAB_ALARM_OK;
		   }
		});
		
		Mockito.when(mActivity.ga_service.api.cancelAlarm(a.getId())).thenReturn(GaWebApi.CANCEL_ALARM_OK);
		assertTrue(solo.waitForView(btSetup));
		assertTrue(waitForViewIsHide(pbConnect));
		assertOnlySetup();
	}
	
	
	public void checkTurnOffWithNetworkOk(Alarm a){
		Mockito.when(mActivity.ga_service.api.cancelAlarm(a.getId())).thenAnswer(new Answer<Integer>(){
			@Override
			  public Integer answer(InvocationOnMock invocation) {
				checkConnectionScreen();
				return GaWebApi.CANCEL_ALARM_OK;
		   }
		});
		assertTrue(solo.waitForView(btSetup));
		assertTrue(waitForViewIsHide(pbConnect));
		assertOnlySetup();	
	}
	
	
	public void assertHasIcons(int count){
		
		// Check for actions count
		int t_and_a_count  = mActivity.ga_service.getActiveActionsDescriptions().size();
		assertEquals(count, t_and_a_count);
		
		GridView grid = (GridView)solo.getView(com.gan4x4.greedyalarm.R.id.gv_icons);
		if (count > 0 ){
			assertVisible(grid);
		}
		assertEquals(t_and_a_count,grid.getCount());	
		
	}
	
	public void assertNoIcons(){
		assertHasIcons(0);
	}
	
	
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
	
	public void autoOff_Expired_NetworkOk_TasksNo_ActionsNo(Alarm a){
		
		
		TestHelper.setSystemTime(TestHelper.getGrabEnd(a)+1); // Alarm Expired
		checkGarabWithNetworkOk(a);
		assertTextOnScreen(com.gan4x4.greedyalarm.R.string.label_alarm_trouble);
		
	}
	
	public void clickCancel_Grab_NetworkOk_TasksNo_ActionsNo(Alarm a){
		long grab_middle = TestHelper.getPreFinishEnd(a)+(TestHelper.getGrabEnd(a)-TestHelper.getPreFinishEnd(a))/2;
		TestHelper.setSystemTime(grab_middle); // Alarm grab hard
		assertTrue(solo.waitForView(btCancel));
		
		assertEquals(mActivity.ga_service.alarmState(),Alarm.GRAB);
		assertOnlyCancel(); // Sometimes fail
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
	
	
// ========================================== TESTS ================================================================================		
	// Robotium
	
	@SmallTest
	public final void testClickSetup(){
		
		Alarm a = getAlarm(alarmTime); // Alarm is off
		setAlarm(a);
		assertNetworkOn();
		assertEquals(mActivity.ga_service.alarmState(),Alarm.OFF);
		assertNoIcons();
		assertTrue(solo.waitForView(btSetup));
		assertOnlySetup();
		solo.clickOnView(btSetup);
		assertTrue(solo.waitForActivity(NewAlarmActivity.class,(int) ui_delay));
	}
	
	@SmallTest
	public final void testClickCancel_InWait_NetworkOk_TasksNo_ActionsNo(){
		Alarm a = prepareStandardEnabledAlarm();
		clickCancel_InWait_NetworkOk_TasksNo_ActionsNo(a);
	}
	
	
	@SmallTest
	public final void testClickCancel_InBeep_NetworkOk_TasksNo_ActionsNo(){

		Alarm a = prepareStandardEnabledAlarm();
		
		assertTrue(solo.waitForView(btCancel));
		
		assertOnlyCancel(); // Some times not work
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
	
	@SmallTest
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

	@SmallTest
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

	
	
	@SmallTest
	public final void testClickCancel_InSnooze_NetworkOk_TasksNo_ActionsNo(){
		Alarm a = prepareStandardEnabledAlarm();
		ClickCancel_InSnooze_NetworkOk_TasksNo_ActionsNo(a);
	}
	
	

	@SmallTest
	public final void testClickCancel_InPreGrab_NetworkOk_TasksNo_ActionsNo(){
		// Setup
		Alarm a = prepareStandardEnabledAlarm();
		clickCancel_InPreGrab_NetworkOk_TasksNo_ActionsNo(a);
	}
	
	
	
	@SmallTest
	public final void testClickCancel_Grab_NetworkOk_TasksNo_ActionsNo(){
		// Setup
		Alarm a = prepareStandardEnabledAlarm();
		clickCancel_Grab_NetworkOk_TasksNo_ActionsNo(a);
	}

	
	
	
	@SmallTest	
	public final void testAutoOff_Expired_NetworkOk_TasksNo_ActionsNo(){
		Alarm a = prepareStandardEnabledAlarm();
		autoOff_Expired_NetworkOk_TasksNo_ActionsNo(a);
	}
	
	@SmallTest
	public final void testMessageAfterTurnOff(){
		Alarm a = prepareStandardEnabledAlarm();
		//autoOff_Expired_NetworkOk_TasksNo_ActionsNo(a);
		clickCancel_Grab_NetworkOk_TasksNo_ActionsNo(a);
		TestHelper.setSystemTime(1);
		mActivity.ga_service.getGanAlarm().turnOn(1);
		a= mActivity.ga_service.getGanAlarm();
		
		ClickCancel_InSnooze_NetworkOk_TasksNo_ActionsNo(a);
		
		//clickCancel_InPreGrab_NetworkOk_TasksNo_ActionsNo(a);
		
		//TestHelper.setSystemTime(1);
		//String text = 
		//solo.waitForText(text)
		
		//clickCancel_InWait_NetworkOk_TasksNo_ActionsNo(mActivity.ga_service.getGanAlarm());
		// Check than message is updated properly
		//clickCancel_InWait_NetworkOk_TasksNo_ActionsNo();
	}
	
	
	
	
	
/*	
	public final void testActivityStateUpdate(){
		Alarm a = prepareStandardEnabledAlarm();
		setAlarm(a);
		assertTrue(solo.waitForView(btCancel));
		assertOnlyCancel();
		solo.goBack();
		waitForActivityIsFinished(mActivity);
		mActivity = getActivity();
		solo.sleep(10000);
		solo.waitForActivity(mActivity.getClass());
		assertOnlyCancel();
	}
*/
	
	
	/*
	@SmallTest
	public final void testLeakServiceConnection(){
		try {
			//mActivity.finish();
			GreedyAlarmService s = mActivity.ga_service;
			solo.waitForView(btSetup);
			solo.clickOnView(btSetup);
			//mActivity.finish();
			solo.finishOpenedActivities();
			solo.finalize();
			s.doExit();
			//s.stopSelf();
			solo.waitForActivity(NewAlarmActivity.class);
			//solo.clickOnButton("Cancel");
			//solo.waitForView(btSetup);
			
			//solo.finishOpenedActivities();
			//solo.finalize();
			//s.stopSelf();
			
		} catch (Throwable e) {
			// TODO: handle exception
			fail("Exception when finish called");
		}
		
		
	}
	
	*/
	
	
}
