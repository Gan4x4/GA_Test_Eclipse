package com.gan4x4.greedyalarm.test.android;

import com.gan4x4.greedyalarm.descriptions.DescriptionForActionBet;
import com.gan4x4.greedyalarm.objects.ActionBet;

import android.test.AndroidTestCase;

public class DescriptionForActionBetTest extends AndroidTestCase {

	ActionBet action;
	
	public DescriptionForActionBetTest(){
		this("DescriptionForActionBetTest");
	}
	
	public DescriptionForActionBetTest(String name) {
		super();
		setName(name);
	}

	
	protected void setUp() throws Exception {
		
		super.setUp();
		action = new ActionBet();
		action.setAmount(11f);
		action.setCurrency("USD");
		
		
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testGetDescription() {
		DescriptionForActionBet d = new DescriptionForActionBet(action,getContext());
		String dtext = d.getDescription();
		String message = "Not replaced pattern "; 
		assertFalse(message+dtext,dtext.contains(DescriptionForActionBet.AMOUNT));
		assertFalse(message+dtext,dtext.contains(DescriptionForActionBet.PAY_SYSTEM));
		
	}

}
