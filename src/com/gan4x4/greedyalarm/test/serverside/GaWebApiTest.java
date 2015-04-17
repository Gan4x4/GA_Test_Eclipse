package com.gan4x4.greedyalarm.test.serverside;



import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import com.gan4x4.greedyalarm.GaWebApi;
import com.gan4x4.greedyalarm.objects.ActionBet;
import com.gan4x4.greedyalarm.objects.Alarm;
import com.gan4x4.greedyalarm.objects.HttpRequestResponse;

import android.test.AndroidTestCase;

public class GaWebApiTest extends AndroidTestCase {
	protected GaWebApi mApi;
	
	public GaWebApiTest() {
		this("GaWebApiTest");
	}
	
	public GaWebApiTest(String name) {
		super();
		setName(name);
	}

	protected void setUp() throws Exception {
		super.setUp();
		mApi = new GaWebApi(mContext);
		
	}
	
	public final void testPreconditions() {
		assertNotNull(mApi);
		assertTrue(mApi.isLogged());
		//assertNotNull(mList);
		}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	/*
	public final void testSendData() {
		//fail("Not yet implemented");
		HashMap<String,String> data = new HashMap<String,String>();
		Calendar now =  GregorianCalendar.getInstance();
		Alarm a = new Alarm(now.getTimeInMillis() +1000*5*60);
		a.setEnabled(true);
		String xml = a.saveToXml();
		data.put("alarm",xml);
		assertTrue(mApi.SaveAlarm(data));
	}
*/
	
	/*
	public final void testGetData() {
		fail("Not yet implemented");
		HashMap<String,String> data = mApi.getAlarmData();
		assertTrue(data.size() > 0);
	}
	
	public final void testCancelAlarm() {
		int actual = mApi.cancelAlarm(20l);
		assertEquals(actual,GaWebApi.CANCEL_ALARM_OK);
		
		
	}
*/
	
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
		//String redirect = mApi.saveAlarmData(data);
		
		HttpRequestResponse response =  mApi.saveAlarmData(data);
		
		assertTrue(response.isSuccess());
		
		
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
	
}
