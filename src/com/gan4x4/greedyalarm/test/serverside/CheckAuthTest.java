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

public class CheckAuthTest extends AndroidTestCase {
	
	protected int sync_time = 1000;
	protected UserAccount user1;
	
	public CheckAuthTest() {
		this("CheckAuthTest");
	}
	
	public CheckAuthTest(String name) {
		super();
		setName(name);
	}

	protected void setUp() throws Exception {
		super.setUp();
		//mDummyActivity = new GaWebApi(mContext);
		user1 = new UserAccount("greedyalarm1@gmail.com","000000ga1");
	}
	
	
}
