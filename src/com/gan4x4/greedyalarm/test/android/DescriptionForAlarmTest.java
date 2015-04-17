package com.gan4x4.greedyalarm.test.android;


import com.gan4x4.greedyalarm.descriptions.DescriptionForAlarm;
import com.gan4x4.greedyalarm.objects.Alarm;
import com.gan4x4.greedyalarm.test.TestHelper;

import android.test.AndroidTestCase;

public class DescriptionForAlarmTest extends AndroidTestCase {

	Alarm alarm;
	
	public DescriptionForAlarmTest(){
		this("DescriptionForAlarmTest");
	}
	
	public DescriptionForAlarmTest(String name) {
		super();
		setName(name);
	}

	
	protected void setUp() throws Exception {
		
		super.setUp();		
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testMultiplicationInActiveState() {
		Alarm a  = TestHelper.getAlarm(600); // 10 min 
		TestHelper.setSystemTime(1); // Before active interval
		a.turnOn(1);
		DescriptionForAlarm d = new DescriptionForAlarm(a,getContext());
		int mult_id = d.getImageId(Alarm.WAIT);
		assertEquals("Many time mage is bad",mult_id,com.gan4x4.greedyalarm.R.anim.manytime);
		TestHelper.setSystemTime(500); // Less 30% 
		mult_id = d.getImageId(Alarm.WAIT);
		assertEquals("Many time mage is bad",mult_id,com.gan4x4.greedyalarm.R.anim.middletime);
		TestHelper.setSystemTime(590); // Less 10% 
		mult_id = d.getImageId(Alarm.WAIT);
		assertEquals("Many time mage is bad",mult_id,com.gan4x4.greedyalarm.R.anim.lowtime);
	}

}
