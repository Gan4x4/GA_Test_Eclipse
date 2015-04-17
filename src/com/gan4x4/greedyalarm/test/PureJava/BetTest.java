package com.gan4x4.greedyalarm.test.PureJava;




import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Calendar;
import java.util.List;
import java.util.Properties;

import com.gan4x4.greedyalarm.objects.ActionBet;
import com.gan4x4.greedyalarm.objects.Alarm;
import com.gan4x4.greedyalarm.objects.GaObject;
import com.gan4x4.greedyalarm.test.TestHelper;

import junit.framework.TestCase;

public class BetTest extends TestCase {

	private ActionBet to;
	
	
	
	public BetTest(String name) {
		super(name);
	}

	protected void setUp() throws Exception {
		
		
		to = new ActionBet();
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	
	public void testDefaultValues(){
		assertEquals(to.getEnabled(),false);
		assertEquals(to.getName(),"");
	}
	
	
	public void testGetPropertysDefault()
	{		
		Properties p;
		try {
			p = to.getPropertys();
			//assertEquals(Long.parseLong(p.getProperty("name")),to.getName());
			assertEquals(Boolean.parseBoolean(p.getProperty("enabled")),to.getEnabled());
			assertEquals(p.getProperty("name"),to.getName());
			
		} catch (Exception e) {
			fail();
		};
		
	}
	
	

	public void loadFromXml(ActionBet a){
		String workingDir = System.getProperty("user.dir");
		String xml = TestHelper.readFile(workingDir+"\\assets\\bet.xml");
		a.loadFromXml(xml);
	}
	
	
	public void testLoadFromXml(){
		loadFromXml(to);
		assertEquals(to.getName(),"");
	}
	
	
	public void testGetPropertysAfterLoad(){
		loadFromXml(to);
		Properties p;
		try {
			p = to.getPropertys();
			assertEquals(Boolean.parseBoolean(p.getProperty("enabled")),false);
			assertEquals(p.getProperty("name"),"");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		}
		//assertEquals(Long.parseLong(p.getProperty("name")),to.getName());
		
		
		//assertEquals(to..getGrabInterval(),0);
		//assertEquals(test_object.getMinutes(),55);
	}

	
	
	
	
	
	
}
