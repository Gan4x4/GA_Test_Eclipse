package com.gan4x4.greedyalarm.test.android;



import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



import com.gan4x4.greedyalarm.GaSoundsHard;
import com.gan4x4.greedyalarm.GaUtils;



import java.io.OutputStream;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;








import android.net.Uri;
import android.os.Environment;
import android.test.AndroidTestCase;
import android.view.View;

public class GaUtilsTest extends AndroidTestCase {
	protected GaUtils utils;
	String file_to_copy = "GreedyAlarm.ogg";
	File dir_to_del = new File(Environment.getExternalStorageDirectory(),"dir_to_del");
	
	public GaUtilsTest() {
		this("GaUtilsTest");
	}
	
	public GaUtilsTest(String name) {
		super();
		setName(name);
	}

	protected void setUp() throws Exception {
		super.setUp();
				
	}
	
	public final void testPreconditions() {
		//assertNotNull(sounds);
		//assertTrue(mApi.isLogged());
		//assertNotNull(mList);
		assertFalse(dir_to_del.exists());
		try {
			List<String> cur_assets  = Arrays.asList(mContext.getResources().getAssets().list("ringtones"));
			//assertTrue("File to copy not found "+file_to_copy,Arrays.asList(mContext.getResources().getAssets().list("")).contains(file_to_copy));
			assertTrue("File to copy not found "+file_to_copy,cur_assets.contains(file_to_copy));
			File f = new File(GaUtils.getRingtoneDir(),file_to_copy);
			assertFalse(f.exists());
		} catch (IOException e) {
			fail();
			e.printStackTrace();
			}
		}

	protected void tearDown() throws Exception {
		super.tearDown();
		File f = new File(GaUtils.getRingtoneDir(),file_to_copy);
		if (f.exists()){
			f.delete();
		}
	}

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
	
	
	@SuppressLint("InlinedApi")
	public void testFileCopy(){
		long fsize=0;
		
		try {
		
			AssetFileDescriptor fd = mContext.getResources().getAssets().openFd("ringtones/"+file_to_copy);
			fsize = fd.getLength();
			fd.close();
		} catch (IOException e) {
			fail("No file in asset");
			e.printStackTrace();
		}
		
		try {
			GaUtils.copyFromAssets(mContext.getAssets(),"ringtones/"+file_to_copy,GaUtils.getRingtoneDir()+"/"+file_to_copy);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			fail("Error whili files copied");
		}
		
		File dir = new File(GaUtils.getRingtoneDir());
		File newSoundFile = new File(dir, file_to_copy);
		assertTrue(dir.exists());
		assertTrue(newSoundFile.exists());
		long real_size = newSoundFile.length();
		assertEquals("File sizes not equals ",fsize, real_size);
	}
	
	
	public void testDeleteDir(){
		try {
			
			assertFalse(dir_to_del.exists());
			assertTrue(dir_to_del.mkdirs());
			assertTrue(dir_to_del.exists());
			String file_path = dir_to_del.getPath()+java.io.File.separator +file_to_copy;
			GaUtils.copyFromAssets(mContext.getAssets(),"ringtones/"+file_to_copy,file_path);
			//assertTrue(dir_to_del.delete());
			GaUtils.deleteDir(dir_to_del.getPath());
			assertFalse(dir_to_del.exists());
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public final void testGetExtension() {
		String path = "/bla/laa.llll../iiii/fili.ogg";
		File f = new File(path);
		String ext = GaUtils.getFileExtension(f);
		assertEquals("ogg",ext);
	}
	
	
	public final void testGetFileName() {
		String path = "/bla/laa.llll../iiii/fili.ogg";
		String simple_path = "myfile.txt";
		File f = new File(path);
		String name;
		try {
			name = GaUtils.getFileNameWithoutExtension(f);
			assertEquals("fili",name);
			File f2 = new File(simple_path);
			name = GaUtils.getFileNameWithoutExtension(f2);
			assertEquals("myfile",name);
		} catch (Exception e) {
			fail("Throw exception on Good filiname");
		}
	}
	

	public final void testGetLog(){
		String log = GaUtils.getLog();
		assertFalse(log.length() == 0);
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
	/*
	public final void testSaveAlarm() {
		
		
		HashMap<String,String> data = new HashMap<String,String>();
		Calendar now =  GregorianCalendar.getInstance();
		Alarm a = new Alarm(now.getTimeInMillis() +1000*5*60);
		a.setEnabled(true);
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
*/
	
	
	
}
