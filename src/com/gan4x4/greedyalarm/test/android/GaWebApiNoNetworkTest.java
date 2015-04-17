package com.gan4x4.greedyalarm.test.android;



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

public class GaWebApiNoNetworkTest extends AndroidTestCase {
	protected GaWebApi mApi;
	protected int sync_time = 1000;
	
	
	public GaWebApiNoNetworkTest() {
		this("GaWebApiNoNetworkTest");
	}
	
	public GaWebApiNoNetworkTest(String name) {
		super();
		setName(name);
	}

	protected void setUp() throws Exception {
		super.setUp();
		mApi = new GaWebApi(mContext);
		mApi.clearCookie();
		mApi.syncCookie();
		Thread.sleep(sync_time);
	
	}
	
	public final void testPreconditions() {
		assertNotNull(mApi);
		//assertTrue(mApi.isLogged());
		}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public final void testClearCookie() throws InterruptedException {
		assertEquals("Cookie not empty","",mApi.getCookie());
		String cookie = "Set-Cookie: ID=732423sdfs73242; expires=Fri, 31 Dec 2025 23:59:59 GMT; path=/; domain=.greedyalarm.com";
		mApi.setCookie(cookie);
		mApi.syncCookie();
		//Thread.sleep(sync_time);
		assertEquals("Cookie not set",cookie,mApi.getCookie());
		mApi.clearCookie();
		//Thread.sleep(sync_time);
		assertEquals("Cookie not cleared","",mApi.getCookie());
		//assertTrue(mApi.isLogged());
		}
	
	
	public final void testSetCookie() throws InterruptedException{
		assertEquals("Cookie not empty","",mApi.getCookie());
		String cookie = "my cookie";
		mApi.setCookie(cookie);
		Thread.sleep(sync_time);
		String actual = mApi.getCookie();
		assertEquals("Cookie not set",cookie,actual);
		
	}
	
		
}
