package com.gan4x4.greedyalarm.test.android;



import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import com.gan4x4.greedyalarm.GaSoundsHard;
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

public class GaSoundsHardTest extends AndroidTestCase {
	protected GaSoundsHard sounds;
	protected SparseArray<String> sFiles;
	Integer[] sStates = { Alarm.BEEP,Alarm.GRAB,Alarm.DO_SOMETHING};
	Integer[] usStates = { Alarm.OFF,Alarm.SNOOZED};
	HashMap<String,String> normal, error;
	private static final String PREFS = "sound_prefs";
	
	
	
	public GaSoundsHardTest() {
		this("GaSoundsHardTest");
	}
	
	public GaSoundsHardTest(String name) {
		super();
		setName(name);
	}

	protected void setUp() throws Exception {
		super.setUp();
		
		normal = new HashMap<String,String>();
		error = new HashMap<String,String>();
		
		//sounds = new GaSounds(mContext,PreferenceManager.getDefaultSharedPreferences(mContext),com.gan4x4.greedyalarm.R.xml.preferences);
		
		
		//Thread.sleep(100); // For copability with API 7 we can't use onLoadListener
		
		
		sFiles = new SparseArray<String>();
		sFiles.append(Alarm.BEEP, "GreedyAlarm.ogg");
		sFiles.append(Alarm.DO_SOMETHING, "GreedyAlarm_Warning.ogg");
		sFiles.append(Alarm.GRAB, "GreedyAlarm_Siren.ogg");
		
		//normal = new HashMap<String,String>();
		//error = new HashMap<String,String>();
		//sounds = new GaSounds(mContext,normal,error);
		//sounds.setVolume(0.01f);
		
		getContext().getSharedPreferences(PREFS, Context.MODE_PRIVATE).edit().clear().commit();
		
	}
	
	/*
	class DelegatedMockContext extends MockContext {

	    private Context mDelegatedContext;
	        private static final String PREFIX = "test.";

	        public DelegatedMockContext(Context context) {
	             mDelegatedContext = context;
	        }

	        @Override
	        public String getPackageName(){
	            return PREFIX;
	        }

	        @Override
	        public SharedPreferences getSharedPreferences(String name, int mode) {
	            return mDelegatedContext.getSharedPreferences(name, mode);
	        }
	    }
	*/
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
		sounds = new GaSoundsHard(mContext,normal,error);
		Thread.sleep(100);
	}
	
	private SharedPreferences getPrefs(){
		//return PreferenceManager.getDefaultSharedPreferences(getContext());
		return getContext().getSharedPreferences(PREFS, Context.MODE_PRIVATE);
	}
	
	
	public void testGetRingtonesFromSettings(){
		SharedPreferences s = getPrefs();
		SharedPreferences.Editor prefEditor = s.edit();
		prefEditor.putString(GaSoundsHard.RING_NORMAL, "String one");
		prefEditor.putString(GaSoundsHard.RING_GRAB, "String three");
		prefEditor.putString("to_ignore", "Not sound string");
		prefEditor.commit();
		
		HashMap<String,String> ring_list = GaSoundsHard.getRingtonesFromSettings(s);
		//assertTrue(ring_list.(GaSounds.RING_NORMAL));
		assertEquals(ring_list.get(GaSoundsHard.RING_NORMAL), "String one");
		assertEquals(ring_list.get(GaSoundsHard.RING_GRAB), "String three");
		assertFalse(ring_list.containsKey("to_ignore"));
	}
	
	
	
	/*
	public void testPreferences(){
		
		
		//SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getContext());
		
		SharedPreferences sp = getContext().getSharedPreferences(PREFS, Context.MODE_PRIVATE);
		assertFalse(sp.contains(GaSounds.RING_NORMAL));
		
		SharedPreferences.Editor prefEditor = sp.edit();
		prefEditor.putString(GaSounds.RING_NORMAL, "la");
		prefEditor.commit();
		assertTrue(sp.contains(GaSounds.RING_NORMAL));
		
	}
	*/
	
	
	/*
	public void testAssets() throws IllegalArgumentException, SecurityException, IllegalStateException, IOException, InterruptedException{
		
		MediaPlayer player = new MediaPlayer();
		player.reset();
		player.setAudioStreamType(AudioManager.STREAM_ALARM);
		//File f = new File("file:///android_asset/ringtones/GreedyAlarm_Siren.ogg");
		File f = new File("file:///android_asset/ringtones/GreedyAlarm_Siren.ogg");
		//player.setDataSource("file:///android_asset/ringtones/GreedyAlarm_Siren.ogg");
		f = new File("file:///android_asset/sound/Crow.ogg");
		AssetFileDescriptor afd = mContext.getAssets().openFd("ringtones/GreedyAlarm_Siren.ogg");
	    player.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
	    player.prepare();
	    player.start();
	    Thread.sleep(500);
		//player.setDataSource(f.getPath());
		//player.prepare();
		//player.start();
	}
	
	*/
	
	/*
	public void testPlayFromAsset() throws InterruptedException{
			
	HashMap<String,String> normal = new HashMap<String,String>();
	HashMap<String,String> error = new HashMap<String,String>();
	
	//Uri def = Settings.System.DEFAULT_RINGTONE_URI;
	//normal.put("ringtoneNormal","file:///android_asset/sound/Crow.ogg");
	normal.put("ringtoneNormal","file:///android_asset/ringtones/GreedyAlarm_Siren.ogg");
	Thread.sleep(100);
	GaSounds sounds;
	try {
		sounds = new GaSounds(mContext,normal,error);
		play(sounds,Alarm.BEEP);
	} catch (Exception e) {
		fail();
	}

		
	}
	
	*/
	
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
		normal.put(GaSoundsHard.RING_NORMAL,ringtones.get(0).toString());
		try {
			initGaSounds();
			long t1 = System.currentTimeMillis();
			String actual = play(Alarm.BEEP);
			long t2 = System.currentTimeMillis();
			String expected  = ringtones.get(0).toString();
			//assertTrue("Sound loads slowly",(t2-t1) < 1000);
			assertEquals(expected,actual); 
		} catch (Exception e) {
			fail(e.getMessage());
		}
		
		
	}
	
	public void testPlayOnError() throws InterruptedException{
		normal.put(GaSoundsHard.RING_PRE,"Bad uri");
		String expected  = "ringtones/GreedyAlarm_Siren.ogg";
		error.put(GaSoundsHard.RING_PRE,expected);
		try {
			initGaSounds();
			String actual = play(Alarm.DO_SOMETHING);
			assertEquals(expected,actual); 
		} catch (Exception e) {
			fail();
		}
		
	}
	
	public void testPlayOnSecondError() throws InterruptedException{
		Uri def = Settings.System.DEFAULT_RINGTONE_URI;
		normal.put(GaSoundsHard.RING_GRAB,"Bad uri");
		String expected  = def.toString();
		error.put(GaSoundsHard.RING_GRAB,"Bad asset");
		try {
			initGaSounds();
			String actual = play(Alarm.GRAB);
			assertEquals(expected,actual); 
		} catch (Exception e) {
			fail();
		}
	}
	
	
	/*
	public void testGetDefaultFile(){
		
		try {
			String fp = sounds.getDefaultRingtoneFile(Alarm.BEEP);
			assertTrue(fp.length()>0);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		}
		
		
		
		
		
	}
	
	
	public final void testPreconditions() {
		assertNotNull(sounds);
		//assertTrue(mApi.isLogged());
		//assertNotNull(mList);
		}
	
	
	public final void testSoundFilesNames() {
		
		for(int state: sStates){
			assertEquals(sFiles.get(state),sounds.getDefaultSoundFileName(state));
		};
		
		assertNull(sounds.getDefaultSoundFileName(-100));
	}
	
	public void testAssetsHasSoundFiles(){
		
		for(int state: sStates){
			assertEquals(true,sounds.isAssetsHasSoundFile(state));
		};
		
		for(int state: usStates){
			assertFalse(sounds.isAssetsHasSoundFile(state));
		};
	}
	
	
	public void testSetup()
	{
		
		
		
	}
	
	
	public void estGoodBehavior(){
	
		SoundPool p = new SoundPool(3, AudioManager.STREAM_ALARM, 0);
		AssetFileDescriptor afd;
		try {
			
			afd = mContext.getResources().getAssets().openFd(sounds.getAssetsSoundFile(Alarm.GRAB));
			int soundID1 = p.load(afd, 1);
			
			afd = mContext.getResources().getAssets().openFd(sounds.getAssetsSoundFile(Alarm.BEEP));
			int soundID2 = p.load(afd, 1);
			
			
			p.play(soundID1, 1, 1, 1, -1, 1);
			
			p.stop(soundID1);
			
			p.play(soundID2, 1, 1, 1, -1, 1);
			
		} catch (IOException e) {
			fail();
		}
		
		
		
		
	}
	
	*/
	
	/*
	public void testPlayDefaultSound(){
		int rnd = -896;
		sounds.playDefaultSound(rnd);
	}
	*/
/*	
	public void offtestPlayFromAssets() throws InterruptedException{
			sounds.play(Alarm.BEEP);
			Thread.sleep(2000);
		//	sounds.stop();
			sounds.play(Alarm.DO_SOMETHING);
			Thread.sleep(2000);
			sounds.play(Alarm.GRAB);
			Thread.sleep(2000);
				
		
	}
*/

	/*
	public void testPlayFromRes() throws InterruptedException{
		
		//String workingDir = System.getProperty("user.dir");
		//String path = workingDir+"\\assets\\sound\\Beep.ogg";
		//int this.getInstrumentation().getContext().getResources().openRawResource(R.raw.blah_blah)
		//getInstrumentation().getContext().getResources().
		//int i = mContext.getResources().getIdentifier("beep","raw", mContext.getPackageName());
		
		//int i = mContext.getResources().getIdentifier("ic_launcher","raw", mContext.getPackageName());
		
		String f1 = "file:///system/media/audio/ringtones/Cairo.ogg";
		
		//String f1 = "file:///system/media/audio/ringtones/BirdWispher.ogg";
		
		//String f2 = "file:///system/media/audio/ringtones/Crystal.ogg";
			
		//String f3 =	"file:///system/media/audio/ringtones/LeisureTime.ogg";
		
		SoundPool p = new SoundPool(1, AudioManager.STREAM_ALARM, 0);
		String path = Uri.parse(f1).getPath();
		int x = p.load(path,1);
		
		//path = Uri.parse(f2).getPath();
		//int x2 = p.load(path,1);
		int str = p.play(x, 1, 1, 1, 1, 1);
		//p.setLoop(str,2);
		p.stop(str);
		//str = p.play(x2, 1, 1, 1, 1, 1);
		//p.setLoop(str,3);
		
		
		
		
		sounds.add("test1", f1);
		sounds.add("test2", f2);
		
		sounds.play("test2",15);
		
		//Thread.sleep(2000);
		
		sounds.play("test1",15);
		//Thread.sleep(2000);
		
	}
	
*/
	
	/*
	public void testMp(){
		
		//String f1 = "file:///system/media/audio/ringtones/BirdWispher.ogg";
		//String f2 = "file:///system/media/audio/ringtones/Crystal.ogg";
		//String f3 =	"file:///system/media/audio/ringtones/LeisureTime.ogg";
		
		String f1 = "/sdcard/media/audio/ringtones/GreadyAlarm_Warning.ogg";
		
		//String f1 = "file:///sdcard/media/audio/ringtones/";
		String f2 = "/sdcard/media/audio/ringtones/GreadyAlarm.ogg";
		String f3 = "/sdcard/media/audio/ringtones/GreadyAlarm_Siren.ogg";
		
		
		sounds.add(1, f1);
		sounds.add(2, f2);
		sounds.add(3, f3);
		
		
		sounds.play(1);
		
		sounds.play(2);
		sounds.stop();
		sounds.play(3);
		

		MediaPlayer player = MediaPlayer.create(mContext,  Uri.parse(f1));
		player.setAudioStreamType(AudioManager.STREAM_MUSIC);
		int duration = player.getDuration();
		player.setLooping(true);
		player.start();
		player.stop();
	}
	
	

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
	
	
	

}
