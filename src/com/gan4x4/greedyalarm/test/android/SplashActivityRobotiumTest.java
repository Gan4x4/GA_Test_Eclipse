
package com.gan4x4.greedyalarm.test.android;

import static org.mockito.Matchers.*;

import org.apache.http.HttpStatus;
import org.mockito.Mockito;
import org.mockito.internal.matchers.Any;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import android.test.suitebuilder.annotation.SmallTest;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.gan4x4.greedyalarm.GaWebApi;
import com.gan4x4.greedyalarm.GreedyAlarmService;
import com.gan4x4.greedyalarm.R;
import com.gan4x4.greedyalarm.SplashActivity;
import com.gan4x4.greedyalarm.mock.DependancyFactory;
import com.gan4x4.greedyalarm.objects.Facebook;
import com.gan4x4.greedyalarm.objects.HttpRequestResponse;
import com.gan4x4.greedyalarm.test.GaActivityTest;
import com.robotium.solo.Condition;

public class SplashActivityRobotiumTest extends GaActivityTest<SplashActivity> {
	
	
	private View buttonLoginGoogle;
	private View buttonLoginFacebook;
	private ProgressBar pbConnect;
	private View buttonReconnect;
	
	//@Mock
	//HttpRequestResponse good_responce;
	
	private long alarmTime = 10000; //sec

	public SplashActivityRobotiumTest() {
		this("SplashActivityTests");
	}
	
	public SplashActivityRobotiumTest(String name) {
		super(name, SplashActivity.class);
		setName(name);
	}

	public void initActivityControls(){
		buttonLoginGoogle =(View) mActivity.findViewById(com.gan4x4.greedyalarm.R.id.button_login);
		buttonLoginFacebook =(View) mActivity.findViewById(com.gan4x4.greedyalarm.R.id.button_fb);
		pbConnect = (ProgressBar) mActivity.findViewById(com.gan4x4.greedyalarm.R.id.pb_connect);
		buttonReconnect = (View) mActivity.findViewById(com.gan4x4.greedyalarm.R.id.button_connect);
	}
	
	
	
	
	protected void setUp() throws Exception {
		
		initMocks();
		// First call performed when activity is created because it we prepare mock first 
		
		
		super.setUp();
		
		
		//assertTrue(solo.waitForActivity(SplashActivity.class));
		
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
	
	
	
	
	// First level Adapters
	@SmallTest
	public final  void testPreconditions() {
		//testMessageAfterTurnOff();
		//super.testPreconditions();
		/*
		assertNotNull(btSetup);
		assertNotNull(btCancel);
		assertNotNull(btSnooze);
		assertNotNull(pbConnect);
		assertNotNull(mActivity);
		assertNotNull(service);
		*/
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
	    }, (int) max_delay);
	}

	public void checkConnectionScreen(){
		
	//	assertTrue(solo.waitForView(pbConnect));
	//	assertVisible(pbConnect);
	//	assertNoButtons();
		
	}
	
	
	public void assertNoButtons(){
		assertUnvisible(buttonLoginGoogle);
		assertUnvisible(buttonLoginFacebook);
	}
	
	/*
	public void assertLoginButtonsShown(){
		assertVisible(buttonLoginGoogle);
		assertVisible(buttonLoginFacebook);
	}
	*/
	
//========================================= Second level Adapters ====================================================================================================
	public void assertProgressOnButtonsNo(){
		assertTrue(solo.waitForView(pbConnect));
		assertVisible(pbConnect);
		assertNoButtons();
	}
	
	public void setAlarmDataForFirstStart(final int responseCode, final int httpCode){
		Mockito.when(mockWebApi.getAlarmData()).thenAnswer(new Answer<HttpRequestResponse>(){
			@Override
			  public HttpRequestResponse answer(InvocationOnMock invocation) throws SecurityException, IllegalArgumentException, NoSuchFieldException, IllegalAccessException {
				//assertTrue(solo.waitForActivity(SplashActivity.class));
				//assertProgressOnButtonsNo();
				HttpRequestResponse r = getStubResponse(responseCode,httpCode);
				return r;
		   }
		});
		// start Activity and service
		
	}
	
	
	
// ========================================== TESTS ================================================================================		
	// Robotium
		
	
	
	public void testNoButtonWithoutNetwork() throws SecurityException, IllegalArgumentException, NoSuchFieldException, IllegalAccessException{
		// Setup
		// Prepare mocks: no network, no alarms
		
		setAlarmDataForFirstStart(HttpRequestResponse.CODE_ERROR,HttpStatus.SC_GATEWAY_TIMEOUT);
		startServiceAndActivity();
		assertTrue(solo.waitForActivity(SplashActivity.class));
		// check that mocks work
		
		// Wait for reconnect button 
		waitForViewIsHide(pbConnect);
		assertVisible(buttonReconnect);
	}
	
	public void testLoginButtonsWithNetworkOk(){
		// Setup
		// Prepare mocks: no network, no alarms
		setAlarmDataForFirstStart(HttpRequestResponse.CODE_ERROR_AUTH,HttpStatus.SC_OK);
		// start Activity and service
		startServiceAndActivity();
		assertTrue(solo.waitForActivity(SplashActivity.class));
		//initActivityControls();
		// check that mocks work
		
		// Wait for login button 
		waitForViewIsHide(pbConnect);
		assertTrue(solo.waitForView(buttonLoginGoogle));
		assertTrue(solo.waitForView(buttonLoginFacebook));
		assertUnvisible(buttonReconnect);
	}
	
	public void testFbCancel() throws ClassNotFoundException{
		// setup 
		Facebook fakeFb = Mockito.mock(Facebook.class);
		// prepare facebook fake
		Mockito.doAnswer(new Answer<Object>() {
	        public Object answer(InvocationOnMock invocation) {
	            Object[] args = invocation.getArguments();
	            Runnable callback = (Runnable) args[1];
	            callback.run();
	            return "called with arguments: " + args;
	        }
	    }).when(fakeFb).getUserInfo(any(SplashActivity.class), any(Runnable.class));
		
		Mockito.when(fakeFb.getCheckAuth(any(SplashActivity.class))).thenReturn(null);
		
		DependancyFactory.setFake(fakeFb);
		
		setAlarmDataForFirstStart(HttpRequestResponse.CODE_ERROR_AUTH,HttpStatus.SC_OK);
		// start Activity and service
		startServiceAndActivity();
		assertTrue(solo.waitForActivity(SplashActivity.class));
		
		waitForViewIsHide(pbConnect);
		
		assertTrue(solo.waitForView(buttonLoginGoogle));
		assertTrue(solo.waitForView(buttonLoginFacebook));
		assertUnvisible(buttonReconnect);
		
		//act
		solo.clickOnView(buttonLoginFacebook);
		
		// check for connection
		assertTrue(waitForViewIsHide(buttonLoginFacebook));
		assertTrue(waitForViewIsHide(buttonLoginGoogle));
		assertTrue(solo.waitForView(pbConnect));
		
		// check for finish
		waitForViewIsHide(pbConnect);
		assertTrue(solo.waitForView(buttonLoginGoogle));
		assertTrue(solo.waitForView(buttonLoginFacebook));
		assertUnvisible(buttonReconnect);
		assertTextOnScreen(com.gan4x4.greedyalarm.R.string.message_login_error);
		
		/*
		solo.waitForView(pbConnect);
		waitForViewIsHide(pbConnect);
		
		assertTrue(solo.waitForView(buttonLoginGoogle));
		assertTrue(solo.waitForView(buttonLoginFacebook));
		assertUnvisible(buttonReconnect);
		*/
		
		/*
		
		Mockito.when(fakeFb.getUserInfo(eq(mActivity), any(Runnable.class))).thenAnswer(new Answer<Object>(){
			@Override
			  public Object answer(InvocationOnMock invocation) throws SecurityException, IllegalArgumentException, NoSuchFieldException, IllegalAccessException {
				//assertTrue(solo.waitForActivity(SplashActivity.class));
				//assertProgressOnButtonsNo();
				return "Fb called";
		   }
		});
		*/
	}
	
	
}
