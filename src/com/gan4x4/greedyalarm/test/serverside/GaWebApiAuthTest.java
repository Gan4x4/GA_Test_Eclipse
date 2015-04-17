package com.gan4x4.greedyalarm.test.serverside;



import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import com.gan4x4.greedyalarm.GaWebApi;
import com.gan4x4.greedyalarm.objects.ActionBet;
import com.gan4x4.greedyalarm.objects.Alarm;
import com.gan4x4.greedyalarm.objects.HttpRequestResponse;
import com.gan4x4.greedyalarm.test.UserAccount;

import android.test.AndroidTestCase;

public class GaWebApiAuthTest extends AndroidTestCase {
	protected GaWebApi mApi;
	protected int sync_time = 1000;
	protected UserAccount user1;
	
	public GaWebApiAuthTest() {
		this("GaWebApiTest");
	}
	
	public GaWebApiAuthTest(String name) {
		super();
		setName(name);
	}

	protected void setUp() throws Exception {
		super.setUp();
		mApi = new GaWebApi(mContext);
		user1 = new UserAccount("greedyalarm1@gmail.com","000000ga1");
	}
	
	public final void testPreconditions() {
		assertNotNull(mApi);
		//assertTrue(mApi.isLogged());
		}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public final void testClearCookie() throws InterruptedException {
		String cookie = "Set-Cookie: ID=732423sdfs73242; expires=Fri, 31 Dec 2025 23:59:59 GMT; path=/; domain=.greedyalarm.com";
		mApi.setCookie(cookie);
		mApi.syncCookie();
		Thread.sleep(sync_time);
		assertEquals("Cookie not set","Set-Cookie: ID=732423sdfs73242",mApi.getCookie());
		mApi.clearCookie();
		Thread.sleep(sync_time);
		assertEquals("Cookie not cleared","",mApi.getCookie());
		//assertTrue(mApi.isLogged());
		}
	
	
	protected boolean isUserExistOnSite(UserAccount user){
		HttpRequestResponse is_user = mApi.isUserExists(user.getEmail());
		if (is_user.getCode() == HttpRequestResponse.CODE_SUCCESS ){
			return true;
		}
		else{
			return false;
		}
	}
	
	
	
	
	
	/*
	public void testCreateNewUser(){
		assertFalse(isUserExistOnSite(user1));
	
	}
	*/
	
	
	/*
	public void testDeleteUser(){
		
		if (! isUserExistOnSite(user1){
			
		};
		
		HttpRequestResponse r = service.api.isUserExists(user1.getEmail());
		if (r.getCode() == HttpRequestResponse.CODE_SUCCESS){
			int user_id = Integer.parseInt(r.getDataByKey("user_id"));
			HttpRequestResponse del = service.api.deleteUserFromServer(user_id);
			assertEquals(HttpRequestResponse.CODE_SUCCESS,del.getCode());
		}
		
		assertFalse(isUserExists());
		
		
	}
	/*
	
	
	
	
	/*
	
	public final void testSaveAlarm() {
		
		
		HashMap<String,String> data = new HashMap<String,String>();
		Calendar now =  GregorianCalendar.getInstance();
		Alarm a = new Alarm(now.getTimeInMillis() +1000*5*60);
		a.turnOn(now.getTimeInMillis());
		String xml = a.saveToXml();
		data.put("alarm",xml);
		
		ActionBet b = new ActionBet();
		b.setEnabled(true);
		b.setAmount((float) 1.4);
		xml = b.saveToXml();
		data.put("actionbet",xml);
		String redirect = mApi.saveAlarmData(data);
		
		assertFalse(redirect.equals("false"));
		
		
		//Alarm = new Alarm();
		//assertEquals(actual,GaWebApi.CANCEL_ALARM_OK);
		
		
	}

	
	public final void testGrabAlarm(){
		
		//Alarm a= params[0];
		//Float p = a.getGrabPercent(getGurrentTime());
		//assertEquals(mApi.grabAlarm(96l,0f),GaWebApi.GRAB_ALARM_OK);
	}
	
	
	public final void testCancelAlarm(){
		
		//Alarm a = new Alarm();
		//Float p = a.getGrabPercent(getGurrentTime());
		//assertEquals(mApi.grabAlarm(96l,0f),GaWebApi.GRAB_ALARM_OK);
	}
	*/
	
}
