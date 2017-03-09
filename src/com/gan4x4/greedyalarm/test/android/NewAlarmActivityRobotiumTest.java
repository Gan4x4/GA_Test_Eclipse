
package com.gan4x4.greedyalarm.test.android;

import java.lang.reflect.Field;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;

import org.apache.http.HttpStatus;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.mockito.Matchers.anyFloat;
import static org.mockito.Mockito.*;
import android.app.Activity;
import android.app.Instrumentation.ActivityMonitor;
import android.content.Context;
import android.content.Intent;
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
import com.gan4x4.greedyalarm.GaWebViewActivity;
import com.gan4x4.greedyalarm.GreedyAlarmService;
import com.gan4x4.greedyalarm.MainActivity;
import com.gan4x4.greedyalarm.MathTaskActivity;
import com.gan4x4.greedyalarm.NewAlarmActivity;
import com.gan4x4.greedyalarm.descriptions.DescriptionForAlarm;
import com.gan4x4.greedyalarm.mock.GaCalendar;
import com.gan4x4.greedyalarm.objects.Alarm;
import com.gan4x4.greedyalarm.objects.GaObject;
import com.gan4x4.greedyalarm.objects.HttpRequestResponse;
import com.gan4x4.greedyalarm.objects.TaskMath;
import com.gan4x4.greedyalarm.test.GaActivityTest;
import com.gan4x4.greedyalarm.test.TestHelper;
import com.google.android.gms.games.internal.GamesContract.GameSearchSuggestionsColumns;
import com.robotium.solo.Condition;
import com.robotium.solo.Solo;

public class NewAlarmActivityRobotiumTest extends
		GaActivityTest<NewAlarmActivity> {

	private Button btCancel;
	private Button btOk;
	
	public NewAlarmActivityRobotiumTest() {
		this("NewAlarmMainActivityTest");
	}
	
	public NewAlarmActivityRobotiumTest(String name) {
		super(name, NewAlarmActivity.class);
		setName(name);
	}
	
	
	@Override
	public void initActivityControls() {
		btCancel = (Button) mActivity.findViewById(com.gan4x4.greedyalarm.R.id.button_cancel);
		btOk = (Button) mActivity.findViewById(com.gan4x4.greedyalarm.R.id.button_ok);
	}
	
	
	protected void setUp() throws Exception {
		super.setUp();		
		startServiceAndActivity();
		max_delay = 10000;
		assertTrue(solo.waitForActivity(NewAlarmActivity.class));
		// May be not all test require this init in future
		setTimeAndNetwork();
	}

// ========================================== Adapters ================================================================================		


	
	
// Mockito ============================================================================================================================
	
	
	
	
	private  Answer<HttpRequestResponse> getMockResponse(final int return_code,final int http_code){
		Answer<HttpRequestResponse> x =new Answer<HttpRequestResponse>() {
			   @Override
			   public HttpRequestResponse answer(InvocationOnMock invocation) throws InterruptedException, SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException{
			     assertTrue(solo.waitForDialogToOpen());
			     return getStubResponse(return_code,http_code);
			   }
			};
		return x;
	}
	
	
	private  Answer<HttpRequestResponse> getMockPausedResponse(final int return_code,final int http_code,final long pause){
		Answer<HttpRequestResponse> x =new Answer<HttpRequestResponse>() {
			   @Override
			   public HttpRequestResponse answer(InvocationOnMock invocation) throws InterruptedException, SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException{
			     Thread.sleep(pause);
			     return getStubResponse(return_code,http_code);
			   }
			};
		return x;
	}
	
	private  Answer<HttpRequestResponse> getMockResponseWithRedirect(final int return_code,final long pause){
		Answer<HttpRequestResponse> x =new Answer<HttpRequestResponse>() {
			   @Override
			   public HttpRequestResponse answer(InvocationOnMock invocation) throws InterruptedException, SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException{
			     Thread.sleep(pause);
			     HttpRequestResponse r = getStubResponse(return_code);
			    	 try {
						doReturn("http://www.google.com").when(r).getDataByKey("redirect");
					} catch (Exception e) {
						e.printStackTrace();
						fail("Crash on getDataByKey on mocked response");
					}
			    	 
			     return r;
			   }
			};
		return x;
	}
	
	
	private void setServiceCommandPause(long pause){
		Field fpause;
		try {
			fpause = GreedyAlarmService.class.getDeclaredField("pause");
			fpause.setAccessible(true);
			fpause.set(mActivity.ga_service, pause);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Can not set pause in GaService");
		}
		
	}
	
	private Integer setServiceCommandAttempts(Integer att){
		Field fattempts;
		Integer result; 
		try {
			fattempts = GreedyAlarmService.class.getDeclaredField("attempts");
			fattempts.setAccessible(true);
			if (att != null) fattempts.set(mActivity.ga_service, att);
			result = fattempts.getInt(mActivity.ga_service);
		} catch (Exception e) {
			e.printStackTrace();
			fail(" Can not set attempts count in GaService");
			result = null;
		}
		return result;
	}
	
	
	private Integer getServiceCommandAttempts(){
		return setServiceCommandAttempts(null);
	}
	
	
	private void assertActivityClosedWithResult(int result){
		assertTrue(waitForActivityIsFinished(mActivity));
		int rc = getResultCode(mActivity);
		assertEquals(result, rc);
	}
	
	private void setTimeAndNetwork(){
		TestHelper.setSystemTime(1);
		simulateNetwork();
		assertNetworkOn();
	}
	
// ===================================================== tests ==========================================================================
	
	public void testButtonOnScreen(){
		assertVisible(btCancel);
		assertVisible(btOk);	
	}
	
	
	public void testSetup() throws InterruptedException{
		TestHelper.setSystemTime(1);
		simulateNetwork();
		assertNetworkOn();
		
		Mockito.when(mockWebApi.saveAlarmData(anyMap())).thenAnswer(getMockResponse(HttpRequestResponse.CODE_SETUP_COMPLETE,HttpStatus.SC_OK));
		
		solo.clickOnView(btOk);
		assertActivityClosedWithResult(Activity.RESULT_OK);
	}
	
	public void testCloseOnSetup(){
		TestHelper.setSystemTime(1);
		simulateNetwork();
		assertNetworkOn();
		solo.clickOnView(btOk);
		Mockito.when(mockWebApi.saveAlarmData(anyMap())).thenAnswer(getMockPausedResponse(HttpRequestResponse.CODE_ERROR,HttpStatus.SC_OK,network_delay));
		//assertTrue(solo.waitForDialogToOpen(ui_delay)); // Less then wait
		assertTextOnScreen(com.gan4x4.greedyalarm.R.string.message_connect);
		try {
			solo.goBack();
			//solo.wait
			solo.finishOpenedActivities();
			//assertTrue(waitForActivityIsFinished(mActivity));
			//mActivity.finish();
		} catch (Exception e) {
			fail("Error if new alarm activity finished in connect \n"+e.getMessage()+e.getStackTrace());
		}
	}
	
	
	public void testNetworkError() {
		TestHelper.setSystemTime(1);
		simulateNetwork();
		assertNetworkOn();
		
		Mockito.when(mockWebApi.saveAlarmData(anyMap())).thenAnswer(getMockPausedResponse(HttpRequestResponse.CODE_ERROR,HttpStatus.SC_BAD_REQUEST,network_delay));
		
		// No pause between attempts and default attempts count
		setServiceCommandPause(1l);
		int att = getServiceCommandAttempts();		

		solo.clickOnView(btOk);
		assertTrue(solo.waitForDialogToOpen(ui_delay));
		waitForText(com.gan4x4.greedyalarm.R.string.message_connect);

		verify(mockWebApi, timeout((int)(network_delay + ui_delay)*att).times(att)).saveAlarmData(anyMap());
		waitForText(com.gan4x4.greedyalarm.R.string.message_network_error);
		solo.clickOnButton(0);
		testButtonOnScreen();
		
	}
	
	
	public void testAlarmDataError() {
		TestHelper.setSystemTime(1);
		simulateNetwork();
		assertNetworkOn();
		
		Mockito.when(mockWebApi.saveAlarmData(anyMap())).thenAnswer(getMockPausedResponse(HttpRequestResponse.CODE_SETUP_ERROR_INVALID_STATE,HttpStatus.SC_OK,network_delay));
		
		// No pause between attempts and default attempts count
		setServiceCommandPause(1l);
		int att = getServiceCommandAttempts();		

		solo.clickOnView(btOk);
		//assertTrue(solo.waitForDialogToOpen(ui_delay));
		//waitForText(com.gan4x4.greedyalarm.R.string.label_dialog_wait);
		waitForText(com.gan4x4.greedyalarm.R.string.message_connect);
		//assertTrue(solo.waitForDialogToClose(ui_delay));
		verify(mockWebApi, timeout((int)(network_delay + ui_delay)*att).times(1)).saveAlarmData(anyMap());
		//assertTrue(solo.waitForDialogToOpen(ui_delay));
		waitForText(com.gan4x4.greedyalarm.R.string.label_dialog_error);
		// TODO check error message
		solo.clickOnButton(0);
		testButtonOnScreen();
		
	}
	
	public void testSomeNetworkErrorButFinalOk() {
		//Setup
		TestHelper.setSystemTime(1);
		simulateNetwork();
		assertNetworkOn();

		int att = 3; // third call be successful
		
		Mockito.when(mockWebApi.saveAlarmData(anyMap()))
		.thenAnswer(getMockPausedResponse(HttpRequestResponse.CODE_SETUP_COMPLETE,HttpStatus.SC_GATEWAY_TIMEOUT,network_delay))
		.thenAnswer(getMockPausedResponse(HttpRequestResponse.CODE_SETUP_COMPLETE,HttpStatus.SC_MOVED_PERMANENTLY,network_delay))
		.thenAnswer(getMockPausedResponse(HttpRequestResponse.CODE_SETUP_COMPLETE,HttpStatus.SC_OK,network_delay));
		
		setServiceCommandPause(1l);
		setServiceCommandAttempts(att);

		// actions
		solo.clickOnView(btOk);
		assertTrue(solo.waitForDialogToOpen(ui_delay));
		waitForText(com.gan4x4.greedyalarm.R.string.message_connect); // sometimes fail on this string
		assertTrue(solo.waitForDialogToClose(network_delay*att + 2*ui_delay));
		
		verify(mockWebApi, timeout((int)(network_delay + ui_delay)*att).times(att)).saveAlarmData(anyMap());

		assertActivityClosedWithResult(Activity.RESULT_OK);
		
	}
	
	public void testSetupRedirect() throws InterruptedException{
		TestHelper.setSystemTime(1);
		simulateNetwork();
		assertNetworkOn();
		
		Mockito.when(mockWebApi.saveAlarmData(anyMap()))
		.thenAnswer(getMockResponseWithRedirect(HttpRequestResponse.CODE_SETUP_REDIRECT,network_delay));
		
		String[] fake_finish = {"fake1","fake2"}; 
		when(mockWebApi.getFinishLoginUrl()).thenReturn(fake_finish);
		
		setServiceCommandPause(1l);
		
		solo.clickOnView(btOk);
		//assertTrue(solo.waitForDialogToOpen(ui_delay));
		waitForText(com.gan4x4.greedyalarm.R.string.message_connect);
		
		assertTrue(solo.waitForDialogToClose(2*network_delay+ui_delay));
		
		assertTrue(solo.waitForActivity(GaWebViewActivity.class));
		//verifyNoMoreInteractions(mockWebApi);
		
	}

	
	public void testDoExitInService(){
		assertTrue(solo.waitForActivity(NewAlarmActivity.class));
		solo.clickOnView(btCancel);
		//assertTrue(solo.waitForActivity(MainActivity.class));
		mActivity.ga_service.doExit();
		assertTrue(waitForActivityIsFinished(mActivity));
		assertTrue(waitForServiceStop());
		
	}

	
	
	
	/*
	
	public void testSetupNeedLogin(){
		
		
		
	}
	*/
}
