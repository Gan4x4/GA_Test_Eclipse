package com.gan4x4.greedyalarm.test.PureJava;




import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Calendar;



import java.util.GregorianCalendar;
import java.util.List;
import java.util.Properties;














import com.gan4x4.greedyalarm.objects.Alarm;
import com.gan4x4.greedyalarm.objects.GaObject;
import com.gan4x4.greedyalarm.test.TestHelper;

import junit.framework.TestCase;

public class AlarmTest extends TestCase {

	private Alarm test_object;
	private int h;
	private int m;
	private long UTCtime;
	private int snz = 5*60;
	private int pre = 15;
	private int grab = 20;
	
	
	public AlarmTest(String name) {
		super(name);
	}

	protected void setUp() throws Exception {
		
		h = 14;
		m= 10;
		UTCtime = getTime(m,h,0);
		test_object = new Alarm(UTCtime);
		test_object.turnOn(GregorianCalendar.getInstance().getTimeInMillis());
		test_object.setName("Strange");
		test_object.setGrabInterval(grab);
		
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	
	
	
	
	// my
	// ========================== Helper functions =============================
	protected long getTime(int m, int h, int d)
	{
		return getTime(m,h,d,0,0);
	}
	
	protected long getTime(int m, int h,int d,int sec , int msec  )
	{
		//Date date = new Date();                      // timestamp now
		Calendar cal = Calendar.getInstance();       // get calendar instance
		//cal.setTime(date);                           // set cal to date
		cal.set(Calendar.HOUR_OF_DAY, h);            // set hour to midnight
		cal.set(Calendar.MINUTE, m);                 // set minute in hour
		cal.set(Calendar.SECOND, sec);                 // set second in minute
		cal.set(Calendar.MILLISECOND, msec);
		cal.add(Calendar.DAY_OF_MONTH,d);
		return cal.getTimeInMillis(); 
	}
	
	
	private void turnOn(Alarm a){
		a.turnOn(GregorianCalendar.getInstance().getTimeInMillis());
	}
	
	private void turnOff(Alarm a){
		a.turnOff(GregorianCalendar.getInstance().getTimeInMillis());
	}
	// ========================== End Helper functions =============================
	
	
	public void testConstructorLong() {
		assertEquals(test_object.getHours(),h);
		assertEquals(test_object.getMinutes(),m);
	}
	
	public void testGetTimeLabel()
	{
		assertEquals(test_object.getTimeLabel(),String.valueOf(h)+":"+String.valueOf(m));
	}
	
	
	
	
	
	public void testGetSecondsLeft()
	{
		long t = getTime(m-3, h, 0);
		assertEquals(test_object.getSecondsLeft(t),3*60);
		t = getTime(m-3, h, 0,45,0);
		assertEquals(test_object.getSecondsLeft(t),2*60+15);
		t = getTime(m+1, h, 0);
		assertEquals(test_object.getSecondsLeft(t),-60);
		t = getTime(m-1, h, 0,1,59);
		assertEquals(test_object.getSecondsLeft(t),58);
		t = getTime(m+2, h, -1);
		assertEquals(test_object.getSecondsLeft(t),60*60*24-2*60);
		
	}
	
	
	
	
	public void testGetMinutesLeft()
	{
		long t = getTime(m-3, h, 0);
		assertEquals(test_object.getMinutesLeft(t),3);
		t = getTime(m-3, h, 0,45,0);
		assertEquals(test_object.getMinutesLeft(t),2);
	}
	
	
	
	
	public void testGetCountdownLabel()
	{
		long t = getTime(m, h, 0);
		assertEquals(test_object.getCountdownLabel(t),"00:00:00");
		t = getTime(m, h+1, -1);
		assertEquals(test_object.getCountdownLabel(t),"23:00:00");
		t = getTime(m-1, h+1, -1);
		assertEquals(test_object.getCountdownLabel(t),"23:01:00");
		t = getTime(m, h-1, 0);
		assertEquals(test_object.getCountdownLabel(t),"01:00:00");
		t = getTime(m-1, h-1, 0);
		assertEquals(test_object.getCountdownLabel(t),"01:01:00");
		
		
		t = getTime(m-1, h-1, 0,15,0);
		assertEquals(test_object.getCountdownLabel(t),"01:00:45");
		
		t = getTime(m-1, h-1, 0,15,1);
		assertEquals(test_object.getCountdownLabel(t),"01:00:44");
		
		t = getTime(8, 0, 0);
		Alarm a2 = new Alarm(t);
		a2.turnOn(GregorianCalendar.getInstance().getTimeInMillis());
		assertEquals(a2.getCountdownLabel(getTime(5, 0, 0)),"00:03:00");
	}
	
	
	public void test_getPropertys()
	{		/*
		String s = test_object.getPropertys();
		Properties p = new Properties();
		ByteArrayInputStream sw = new ByteArrayInputStream(s.getBytes());
		try {
			p.loadFromXML(sw);
		
		} catch (IOException e) {
			assertTrue(1==0);
		}
		*/
		Properties p;
		try {
			p = test_object.getPropertys();
			assertEquals(Long.parseLong(p.getProperty("time")),UTCtime);
			assertEquals(Boolean.parseBoolean(p.getProperty("enabled")),true);
			assertEquals(p.getProperty("name"),"Strange");
			
			Alarm a= new Alarm();
			GaObject go = a; 
			p = go.getPropertys();
			
			
		} catch (Exception e) {
			fail();
		};
		
	}
	
	
	public void test_setPropertys()
	{
		Properties p = new Properties();
		p.put("id", "334");
		p.put("action_id", "90");
		p.put("enabled", "true");
		p.put("snoozed", "false");
		p.put("time", Long.toString(UTCtime));
		p.put("time_of_set", Integer.toString(0));
		p.put("snooze_interval", Integer.toString(5*60));
		p.put("preFinishInterval", Integer.toString(15));
		p.put("grab_interval", Integer.toString(grab));
		p.put("name", "No name");
		p.put("puzzle_id", Integer.toString(0));
		p.put("puzzle_type", "none");
		p.put("action_type", "none");
		
		
		
		ByteArrayOutputStream s = new ByteArrayOutputStream();
		try {
			p.storeToXML(s,"test");
		} catch (IOException e) {
			e.printStackTrace();
		}
		Alarm alarm = new Alarm();
		assertTrue(alarm.loadFromXml(s.toString()));
		assertEquals(alarm.getState(getTime(1, 2, 0)),Alarm.WAIT);
		assertEquals(alarm.getName(),"No name");
		
	}
	
	
	public void test_getTimeToNextAppear()
	{
		// 3 minutes to start
		long t = getTime(m-3, h, 0);
		long tta = test_object.getTimeToNextAppear(t);	
		test_object.setSnoozeInterval(snz);
		test_object.setPreFinishInterval(pre);
		assertEquals(tta,3*60*1000);
		
		// 3 minutes without 45.1900 to start
		t = getTime(m-3, h, 0,45,19);
		tta = test_object.getTimeToNextAppear(t);	
		assertEquals(tta,3*60*1000-45*1000-19);
		
		//  45.1900 sec  after start
		t = getTime(m, h, 0,45,19);
		tta = test_object.getTimeToNextAppear(t);	
		assertEquals(tta,0);
		
		
		t = getTime(m, h+3, 0,45,19);
		tta = test_object.getTimeToNextAppear(t);	
		assertTrue(tta < 0); // Time in past
		
		
		t = getTime(m+2, h, 0);
		test_object.setSnooze(true);
		tta = test_object.getTimeToNextAppear(t);	
		assertEquals(tta,(test_object.getSnoozeInterval()-2*60)*1000);
		
		t = getTime(m, h, 0,test_object.getSnoozeInterval(),0);
		tta = test_object.getTimeToNextAppear(t);	
		assertEquals(tta,0);
		
		t = getTime(m, h, 0,test_object.getSnoozeInterval(),1);
		tta = test_object.getTimeToNextAppear(t);	
		assertEquals(tta,0); // Do Something now
		
		t = getTime(m, h, 0,test_object.getSnoozeInterval()-15,0);
		tta = test_object.getTimeToNextAppear(t);	
		assertEquals(tta,15*1000); 
		
	}
	
	
	
	
	
	
	
	public void test_getStateNormalCycle()
	{
		// Off - > On -> Beep -> (Puzzle) -> Off
		Alarm test_alarm = new Alarm(getTime(12,10,0));
		test_alarm.setSnoozeInterval(snz);
		test_alarm.setPreFinishInterval(pre);
		int state = test_alarm.getState(getTime(10,14,0)); 
		assertEquals(Alarm.OFF,state);      // Alarm has been off
		
		turnOn(test_alarm);
		//test_alarm.setEnabled(true);
		state = test_alarm.getState(getTime(10,14,-1)); // Alarm in future
		assertEquals(Alarm.WAIT,state);
		
		state = test_alarm.getState(getTime(12,10, 0)); // Alarm Now 
		assertEquals(Alarm.BEEP,state);
		
		state = test_alarm.getState(getTime(13,10, 0)); // Alarm Now 
		assertEquals(Alarm.BEEP,state);
		
		turnOff(test_alarm);
		//test_alarm.setEnabled(false); // User turn Alarm off
		state = test_alarm.getState(getTime(13,10, 0)); // Alarm Now 
		assertEquals(Alarm.OFF,state);
	}	
		
	
	public void test_getStateNormalCycleWithSnooze()
	{
		//  On -> Wait -> Beep -> Snooze -> (Puzzle) -> Off
		Alarm test_alarm = new Alarm(getTime(59,18,0));
		test_alarm.setSnoozeInterval(snz);
		test_alarm.setPreFinishInterval(pre);
		//test_alarm.setEnabled(true);
		turnOn(test_alarm);
		int state = test_alarm.getState(getTime(58,18,0));
		assertEquals(Alarm.WAIT,state);

		state = test_alarm.getState(getTime(00,19, 0)); // Alarm must beep 
		assertEquals(Alarm.BEEP,state);
		
		test_alarm.setSnooze(true);
		state = test_alarm.getState(getTime(02,19, 0)); // Alarm snoozed
		assertEquals(Alarm.SNOOZED,state);
		
		//test_alarm.setEnabled(false);
		turnOff(test_alarm);
		state = test_alarm.getState(getTime(03,19, 0)); // Alarm in past
		assertEquals(Alarm.OFF,state);
	}
	
	
	public void test_getStateAlarmSnoozedBytNotOff()
	{
		//  On -> Wait -> Beep -> Snooze -> PreOff -> Off
		Alarm test_alarm = new Alarm(getTime(0,23,0));
		test_alarm.setSnoozeInterval(snz);
		test_alarm.setPreFinishInterval(pre);
		
		turnOn(test_alarm);
		//test_alarm.setEnabled(true);
		
		int state = test_alarm.getState(getTime(2,23, 0)); // Alarm must beep 
		assertEquals(Alarm.BEEP,state);
		
		test_alarm.setSnooze(true);
		state = test_alarm.getState(getTime(4,23, 0)); // Alarm snoozed
		assertEquals(Alarm.SNOOZED,state);
		
		state = test_alarm.getState(getTime(5,23, 0,1,0)); // Alarm in pre off state
		assertEquals(Alarm.DO_SOMETHING,state);
		
		state = test_alarm.getState(getTime(5,23, 1,1,0)); // Alarm in EXPIRED state
		assertEquals(Alarm.EXPIRED,state);
	}
	
	
	public void test_getStateAlarmWithGarb()
	{
		//  On -> Wait -> Beep -> Snooze -> PreOff -> Off
		Alarm test_alarm = new Alarm(getTime(0,23,0));
		test_alarm.setSnoozeInterval(snz); //5*60
		test_alarm.setPreFinishInterval(pre); // 15
		test_alarm.setGrabInterval(grab); //20
		turnOn(test_alarm);
		//test_alarm.setEnabled(true);
		
		int state = test_alarm.getState(getTime(2,23, 0)); // Alarm must beep 
		assertEquals(Alarm.BEEP,state);
		
		test_alarm.setSnooze(true);
		state = test_alarm.getState(getTime(4,23, 0)); // Alarm snoozed
		assertEquals(Alarm.SNOOZED,state);
		
		state = test_alarm.getState(getTime(5,23, 0,1,0)); // Alarm in pre off state
		assertEquals(Alarm.DO_SOMETHING,state);
		
		state = test_alarm.getState(getTime(5,23,0,pre+1,0)); // Alarm in Grab state
		assertEquals(Alarm.GRAB,state);
		
		state = test_alarm.getState(getTime(5,23,0,pre+grab+1,0)); // Alarm in EXPIRED state
		//assertTrue(test_alarm.getLastOffAuto());
		assertEquals(Alarm.EXPIRED,state);
		
		//test_alarm.setEnabled(false);
		turnOff(test_alarm);
		state = test_alarm.getState(getTime(5,23,0,pre+grab+1,0));
		assertEquals(Alarm.OFF,state);
	}

	
	
		
	public void test_GetField()
	{
		String[] tested_fields_names = {"time","name","snooze_interval"};
		for (String fname : tested_fields_names){
			Field f = test_object.getField(fname,test_object.getClass());
			assertNotNull(f);
			String x = f.getName();
			assertEquals(x,fname);
		}
		
		String[] int_fields_names = test_object.getFieldsToSave() ;
		for (String fname : int_fields_names){
			Field f = test_object.getField(fname,test_object.getClass());
			assertNotNull(f);
			String x = f.getName();
			assertEquals(x,fname);
		}
		
	}
	
	
	
	public String readFile(String filename)
	{
	   String content = null;
	 /*  
	   InputStream is = getResources().openRawResource(R.id.filename);
	   
	   Path file = ...;
	   try (InputStream in = Files.newInputStream(file);
	       BufferedReader reader =
	         new BufferedReader(new InputStreamReader(in))) {
	       String line = null;
	       while ((line = reader.readLine()) != null) {
	           System.out.println(line);
	       }
	   } catch (IOException x) {
	       System.err.println(x);
	   }
	   
	   */
	   
	   
	   File file = new File(filename); //for ex foo.txt
	   try {
	       FileReader reader = new FileReader(file);
	       char[] chars = new char[(int) file.length()];
	       reader.read(chars);
	       content = new String(chars);
	       reader.close();
	   } catch (IOException e) {
	       e.printStackTrace();
	   }
	   return content;
	}
	
	
	public void testLoadFromXml(){
		
		//InputStream is = getResources().openRawResource(R.id.filename);
		//BufferedInputStream bis = new BufferedInputStream(is);
		String workingDir = System.getProperty("user.dir");
		String xml = readFile(workingDir+java.io.File.separator+"assets"+java.io.File.separator+"alarm.xml");
		test_object.loadFromXml(xml);
		assertEquals(test_object.getSnoozeInterval(),1000);
		assertEquals(test_object.getGrabInterval(),0);
		//assertEquals(test_object.getMinutes(),55);
	}
	
	
	public void testGetGrabPercent(){
		
	//  On -> Wait -> Beep -> Snooze -> PreOff -> Off
			Alarm test_alarm = new Alarm(getTime(0,23,0));
			test_alarm.setSnoozeInterval(snz); //5*60
			test_alarm.setPreFinishInterval(pre); // 15
			test_alarm.setGrabInterval(grab); //20
			turnOn(test_alarm);
			//.setEnabled(true);
			
			int state = test_alarm.getState(getTime(2,23, 0)); // Alarm must beep 
			assertEquals(Alarm.BEEP,state);
			
			Float p = test_alarm.getCurrentGrabPercent(getTime(2,23, 0));
			assertEquals(0f,p);
			
			
			test_alarm.setSnooze(true);
			long t = getTime(1,23, 0,snz/2,0);
			state = test_alarm.getState(t); // Alarm snoozed
			assertEquals(Alarm.SNOOZED,state);
			
			p = test_alarm.getCurrentGrabPercent(t);
			assertEquals(0f,p);
			
			
			state = test_alarm.getState(getTime(5,23, 0,1,0)); // Alarm in pre off state
			assertEquals(Alarm.DO_SOMETHING,state);
			
			p = test_alarm.getCurrentGrabPercent(getTime(5,23, 0));
			assertEquals(0f,p);

			
			t = getTime(5,23,0,pre+grab/2,0);
			state = test_alarm.getState(t); // Alarm in Grab state
			assertEquals(Alarm.GRAB,state);
			
			p = test_alarm.getCurrentGrabPercent(t);
			assertEquals(0.5f,p);
			
			t = getTime(5,23,0,pre+grab*3/4,0);
			p = test_alarm.getCurrentGrabPercent(t);
			assertEquals(0.75f,p);
			
			t = getTime(5,23,0,pre+grab*1/4,0);
			p = test_alarm.getCurrentGrabPercent(t);
			assertEquals(0.25f,p);


			t = getTime(5,23,0,pre+grab+1,0);
			state = test_alarm.getState(t); // Alarm in EXPIRED state
			//assertTrue(test_alarm.getLastOffAuto());
			assertEquals(Alarm.EXPIRED,state);
			p = test_alarm.getCurrentGrabPercent(t);
			assertEquals(1f,p);
			/*
			turnOff(test_alarm);
			p = test_alarm.getCurrentGrabPercent(t);
			assertEquals(0f,p);
			*/
	}
	
	
	public void testStoredGrabPercent(){
		Alarm a = new Alarm(getTime(0,23,0));
		//long middle = getTime(5,23,0,pre +grab/2,0);
		long middle = 1000*(TestHelper.getPreFinishEnd(a)+(TestHelper.getGrabEnd(a)-TestHelper.getPreFinishEnd(a))/2);
		a.turnOff(middle);
		assertEquals(a.getCancelTime(),middle); 
		Float percent = a.getGrabPercent();
		assertTrue("Percent not near 50% : "+percent.toString(),Math.abs(percent - 0.5) < 0.1);
		
	}
	
	
	public void testIsToday(){
		Alarm a = new Alarm(getTime(0,23,5));
		
		assertFalse(a.isToday(getTime(0,23,6)));
		assertTrue(a.isToday(getTime(0,22,2)));
		assertTrue(a.isToday(getTime(59,1,2)));
		assertFalse(a.isToday(getTime(5,23,2)));
		
		a = new Alarm(getTime(50,20,5));
		assertTrue(a.isToday(getTime(3,1,5)));
		
		
		
	}

	
	public void testClone(){
		Alarm a = new Alarm(getTime(0,23,5));
		String n1 = "A1", n2 = "Newname";
		a.setName(n1);
		Alarm b = a;
		Alarm c = a.clone();
		a.setName(n2);
		assertEquals(n1, c.getName());
		assertEquals(n2, b.getName());
	}
	
	public void testDifference(){
		long diff = 1150;
		long time = test_object.getAlarmTime().getTimeInMillis();
		long time_of_set = test_object.getSetupTime();
		long to_alarm = test_object.getTimeToNextAppear(100);
		test_object.addServerTimeCorrection(diff);
		assertEquals("Incorrect difference",time+diff, test_object.getAlarmTime().getTimeInMillis());
		assertEquals("Incorrect difference of set time",time_of_set+diff, test_object.getSetupTime());
		test_object.removeServerTimeCorrection(diff);
		assertEquals("difference not removed",time, test_object.getAlarmTime().getTimeInMillis());
		assertEquals("Duration changed",to_alarm, test_object.getTimeToNextAppear(100));
	}
	
	
	public void testGetTimeToActionPercent(){
		test_object.setSnoozeInterval(snz);
		test_object.setPreFinishInterval(pre);
		int p = test_object.getTimeToActionPercent(UTCtime);
		long d = 1000*(snz+pre);
		assertEquals("",100,p);
		p = test_object.getTimeToActionPercent(UTCtime+d/2);
		assertEquals("",50,p);
		p = test_object.getTimeToActionPercent(UTCtime+d);
		assertEquals("",0,p);
		p = test_object.getTimeToActionPercent(UTCtime+d+10);
		assertEquals("",0,p);
		p = test_object.getTimeToActionPercent(UTCtime-d);
		assertEquals("",200,p);
		
	}
	
}
