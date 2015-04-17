package com.gan4x4.greedyalarm.test.android;



import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import com.gan4x4.greedyalarm.GaSounds;

import com.gan4x4.greedyalarm.GaSoundsLite;
import com.gan4x4.greedyalarm.objects.Alarm;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.media.SoundPool;








import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.test.AndroidTestCase;
import android.test.mock.MockContext;
import android.util.SparseArray;

public class GaSoundsLiteTest extends AndroidTestCase {
	protected GaSoundsLite sounds;
	protected SparseArray<String> sFiles;
	Integer[] sStates = { Alarm.BEEP,Alarm.GRAB,Alarm.DO_SOMETHING};
	Integer[] usStates = { Alarm.OFF,Alarm.SNOOZED};
	HashMap<String,String> normal;
	private static final String PREFS = "sound_prefs";
	
	public GaSoundsLiteTest() {
		this("GaSoundsLiteTest");
	}
	
	public GaSoundsLiteTest(String name) {
		super();
		setName(name);
	}

	protected void setUp() throws Exception {
		super.setUp();
		
		normal = new HashMap<String,String>();
		//error = new HashMap<String,String>();
		sFiles = new SparseArray<String>();
		sFiles.append(Alarm.BEEP, "GreedyAlarm.ogg");
		sFiles.append(Alarm.DO_SOMETHING, "GreedyAlarm_Warning.ogg");
		sFiles.append(Alarm.GRAB, "GreedyAlarm_Siren.ogg");
		getContext().getSharedPreferences(PREFS, Context.MODE_PRIVATE).edit().clear().commit();
		
	}
	
	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	private String play(int id) throws InterruptedException{
		sounds.setVolume(0.01f);
		String res = sounds.play(id);
		Thread.sleep(500);
		sounds.stop();
		return res;
	}
	
	private void initGaSounds() throws Exception{
		sounds = new GaSoundsLite(mContext,normal);
		Thread.sleep(100);
	}
	
	private SharedPreferences getPrefs(){
		//return PreferenceManager.getDefaultSharedPreferences(getContext());
		return getContext().getSharedPreferences(PREFS, Context.MODE_PRIVATE);
	}
	
	
	private ArrayList<Uri> getRingtones(){
		RingtoneManager ringtoneMgr = new RingtoneManager(mContext);
		ringtoneMgr.setType(RingtoneManager.TYPE_ALARM);
		Cursor alarmsCursor = ringtoneMgr.getCursor();
		int alarmsCount = alarmsCursor.getCount();
		if (alarmsCount == 0 && !alarmsCursor.moveToFirst()) {
		    return null;
		}
		ArrayList<Uri> alarms = new ArrayList<Uri>(alarmsCount);
		while(!alarmsCursor.isAfterLast() && alarmsCursor.moveToNext()) {
		    int currentPosition = alarmsCursor.getPosition();
		    alarms.add(ringtoneMgr.getRingtoneUri(currentPosition));
		}
		alarmsCursor.close();
		return alarms;
	}
	
	
	public void testNormalPlay() throws InterruptedException{
		ArrayList<Uri> ringtones = getRingtones();
		assertTrue(ringtones.size() > 0);
		String  expected = ringtones.get(0).toString();
		normal.put(GaSounds.RING_NORMAL,expected);
		try {
			initGaSounds();
			long t1 = System.currentTimeMillis();
			String actual = play(Alarm.BEEP);
			long t2 = System.currentTimeMillis();
			
			//assertTrue("Sound loads slowly",(t2-t1) < 1000);
			assertEquals(expected,actual); 
		} catch (Exception e) {
			fail();
		}
		
		
	}
	
	public void testLongPlay() throws InterruptedException{
		testNormalPlay();
		Thread.sleep(20000);
		
	}
	

}
