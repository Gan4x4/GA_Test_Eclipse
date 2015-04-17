package com.gan4x4.greedyalarm.test.android;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import com.gan4x4.greedyalarm.GaRingtone;
import com.gan4x4.greedyalarm.GaSoundsHard;
import com.gan4x4.greedyalarm.GaUtils;
import com.gan4x4.greedyalarm.MainActivity;

import android.R;
import android.content.ContentResolver;
import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.database.Cursor;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.provider.MediaStore;
import android.test.AndroidTestCase;
import android.test.InstrumentationTestCase;
import android.util.Log;

public class GaRingtoneTest extends InstrumentationTestCase {
	
	Context tContext;
	String media_folder;
	
	public GaRingtoneTest() {
		this("GaRingtoneTest");
	}
	
	public GaRingtoneTest(String name) {
		super();
		setName(name);
	}
	
	protected void setUp() throws Exception {
		super.setUp();
		tContext = getInstrumentation().getContext();
		media_folder = GaUtils.getRingtoneDir() + java.io.File.separator + "test" ;
		File f = new File(media_folder); 
		if (f.exists()){
			GaUtils.deleteDir(media_folder);
			Log.w(MainActivity.TAG, "Media folder exist. try to delete");
		};
		assertTrue("Can not create directory for ringtones: "+media_folder,f.mkdir());
		assertTrue(f.exists() && f.isDirectory());
		GaRingtone.setSdMediaFolder(media_folder);
		GaRingtone.setAssetFolder("sound");
		
		// check db for GA ringtones
		
		ArrayList<String>  del = GaRingtone.deleteAllOldRingtonesfromDb(tContext);
		if (del.size() > 0){
			Log.w(MainActivity.TAG," Not all ringtones deleted before test");
		};
	}
	
	protected void tearDown() {
		
		GaRingtone.deleteAllOldRingtonesfromDb(tContext);
		assertTrue(GaUtils.deleteDir(media_folder));
		File f = new File(media_folder);
		assertFalse(f.exists());
	}
	
	
	public void testConstructor(){
		
		HashMap<String, String> good_input = new HashMap<String, String>();
		good_input.put("file", "Crow.ogg");
		good_input.put("key","k1");
		good_input.put("summary","t1");
		
		try {
			GaRingtone ringtone = new GaRingtone(good_input, tContext);
			assertEquals(good_input.get("file"),ringtone.getFile());
			assertEquals(good_input.get("key"),ringtone.getKey());
			assertEquals(good_input.get("summary"),ringtone.getTitle());
		} catch (Exception e) {
			fail("Good hasmap is not read properly"+e.getMessage());
		}
		
	}
	
	
	public void testGetDefaultRingtonesSoundsFromXml() throws Exception{
		HashMap<String,String> ring_list = GaRingtone.getDefaultRingtonesSoundsFromXml(tContext,com.gan4x4.greedyalarm.test.R.xml.preferences_good);
		assertEquals(ring_list.size(), 2);
		assertEquals("sound/Beep.ogg",ring_list.get(GaSoundsHard.RING_NORMAL));
		assertEquals("sound/Crow.ogg",ring_list.get(GaSoundsHard.RING_GRAB));
		
		//assertFalse(ring_list.containsKey("to_ignore"));
		
	}
	
	
	public void testReadRingtonesFromPreferencesWithBadFilename() {
		int pref_xml_id = com.gan4x4.greedyalarm.test.R.xml.preferences_with_error;
		ArrayList<GaRingtone> ring_list = GaRingtone.getRingtonesFromContext(tContext,pref_xml_id);
		assertNull(ring_list);
	}
	
	
	public void testReadRingtonesFromCorrectPreferences() {
		
		int pref_xml_id = com.gan4x4.greedyalarm.test.R.xml.preferences_good;
		
		ArrayList<GaRingtone> act;
		try {
			act = GaRingtone.getRingtonesFromContext(tContext,pref_xml_id);
			assertEquals(2,act.size());
			
			// Check first ringtone with summary
			GaRingtone ring = act.get(0);
			assertNotNull(ring);
			assertEquals("Beep.ogg",ring.getFile());
			assertEquals("ringtoneNormal",ring.getKey());
			assertEquals("Beep",ring.getTitle());
			
			// Check second ringtone without summary
			
			ring = act.get(1);
			assertNotNull(ring);
			assertEquals("Crow.ogg",ring.getFile());
			assertEquals("ringtoneOnGrab",ring.getKey());
			assertEquals("Crow",ring.getTitle());
			
		
		} catch (Exception e) {
			fail("Reading not successful: "+e.getMessage());
		};
		
	}

	private GaRingtone installRingtoneNum(int n,int pref_xml_id){
		ArrayList<GaRingtone> act;
		try {
			act = GaRingtone.getRingtonesFromContext(tContext,pref_xml_id);
			assertEquals(2,act.size());
			// Check first ringtone with summary
			GaRingtone ring = act.get(n);
			ring.uninstallRingtone();
			assertFalse(ring.isRingtoneInstalled());
			ring.installRingtone();
			assertTrue(ring.isRingtoneInstalled());
			return ring;
		}
		catch (Exception e) {
			fail("Reading not successful: "+e.getMessage());
		};
		return null;
	}
	
	private GaRingtone installFirstRingtone(int pref_xml_id){
		return installRingtoneNum(0,pref_xml_id);
	}
	
	public void testInstallRingtone(){
		int pref_xml_id = com.gan4x4.greedyalarm.test.R.xml.preferences_good;
		//GaRingtone
		assertNotNull(installFirstRingtone(pref_xml_id));
	}
	
	
	
	
		
	public void testGetUri(){
		int pref_xml_id = com.gan4x4.greedyalarm.test.R.xml.preferences_good;
		GaRingtone ring = installFirstRingtone(pref_xml_id);
		try {
			assertTrue(ring.isRingtoneInstalled());
			Uri uri = ring.getUri();
			assertNotNull(uri);
		} catch (Exception e) {
			fail("URi not returned");
			e.printStackTrace();
		}
	}
	
	public void testOriginalFile() throws Exception{
		int pref_xml_id = com.gan4x4.greedyalarm.test.R.xml.preferences_original_part;
		GaRingtone ring = installFirstRingtone(pref_xml_id);
		
		Uri ringtoneUri = ring.getUri();
    	Ringtone ringtone = RingtoneManager.getRingtone(tContext, ringtoneUri);
    	String name = ringtone.getTitle(tContext);
    	String expected = ring.getTitle(); 
		assertEquals(expected, name);
		/** Tear down  **/
		ringtone.stop(); // to release internal mediaplayer
	}
	
	
	/*
	public void testGetMimeType(){
		
		try {
			String[] ogg_list = getInstrumentation().getContext().getAssets().list("sound");
			
			
			//copyFromAssets(AssetManager  asset, String from, String to);
			
			
			for (String fn : ogg_list){
				assertEquals("application/ogg",GaRingtone.getMimeType(fn));
				
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail("Can't read data");
		}
		
		
		
		
	}
	*/
	
	private void scannerMediaFile (ContentResolver cr) {
		
				//; the the 
		String app_name = getInstrumentation().getTargetContext().getResources().getString(com.gan4x4.greedyalarm.R.string.application_name);
		Cursor cursor = cr.query (MediaStore.Audio.Media.INTERNAL_CONTENT_URI, new String [] {MediaStore.Audio. Media._ID, MediaStore.Audio.Media.DATA, MediaStore.Audio.Media.TITLE}, MediaStore.Audio.Media.ARTIST + " = ?" , new String [] {app_name}, null); 
		if (cursor == null ) {return;}
		assertTrue("Applications Ringtone list is empty",cursor.getCount() > 0);
		while (cursor.moveToNext ()) 
		{
			String x = cursor.getString (1);
			
		} 
	}	
	

}
