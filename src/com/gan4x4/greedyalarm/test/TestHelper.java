package com.gan4x4.greedyalarm.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.util.Calendar;
import java.util.GregorianCalendar;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.util.DisplayMetrics;

import com.gan4x4.greedyalarm.mock.GaCalendar;
import com.gan4x4.greedyalarm.objects.Alarm;

public class TestHelper {

	
	public static String readFile(String filename)
	{
	   String content = null;
	   
	   
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
	
	
	
	public static String readFileFromAssets(Object o, String filename){
		
		InputStream in = o.getClass().getClassLoader().getResourceAsStream(filename);
		return TestHelper.readIt(in);
		
	}
	
	
	public static String readIt(InputStream in) {
		String res = "";
		  BufferedReader reader = null;
		  try {
		    reader = new BufferedReader(new InputStreamReader(in));
		    String line = "";
		    
		    while ((line = reader.readLine()) != null) {
		      res = res + line;
		    }
		    return res;
		  } catch (IOException e) {
		    e.printStackTrace();
		  } finally {
		    if (reader != null) {
		      try {
		        reader.close();
		      } catch (IOException e) {
		        e.printStackTrace();
		        }
		      return res;
		    }
		  }
		  return res;
		} 

	public static long getWaitEnd(Alarm a){
		 return a.getAlarmTime().getTimeInMillis()/1000;
	}

	public static long getSnoozeEnd(Alarm a){
		 return getWaitEnd(a) +  a.getSnoozeInterval();
	}
	
	
	public static long getPreFinishEnd(Alarm a){
		 return getWaitEnd(a) +  a.getSnoozeInterval() + a.getPreFinishInterval();
	}
	
	public static long getGrabEnd(Alarm a){
		 return getPreFinishEnd(a) +  a.getGrabInterval();
	}
	
	// @param UTC time in seconds
		public static void setSystemTime(long time){
			Calendar timeWait = GregorianCalendar.getInstance();
			timeWait.setTimeInMillis(time*1000);
			GaCalendar.setFake(timeWait);
	}
		
		
	public static Alarm getAlarm(long time){
		Calendar timeAlarm = GregorianCalendar.getInstance();
		timeAlarm.setTimeInMillis(time*1000);
		Alarm a = new Alarm(timeAlarm.getTimeInMillis());
		return a;
		
	}
		
	public static String getDisplayInfo(Context c){
		//Determine screen size
		String result;
	    if ((c.getResources().getConfiguration().screenLayout &      Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE) {     
	        result = "Large screen";

	    }
	    else if ((c.getResources().getConfiguration().screenLayout &      Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_NORMAL) {     
	    	result ="Normal sized screen";

	    } 
	    else if ((c.getResources().getConfiguration().screenLayout &      Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_SMALL) {     
	    	result =  "Small sized screen";
	    }
	    else {
	        result = "Screen size is neither large, normal or small";
	    }



	    
	    	//Determine density
	    	//DisplayMetrics metrics = new DisplayMetrics();
	        //c.getWindowManager().getDefaultDisplay().getMetrics(metrics);
	        
	        DisplayMetrics metrics = c.getResources().getDisplayMetrics();
	        int density = metrics.densityDpi;

	        if (density==DisplayMetrics.DENSITY_HIGH) {
	        	result += "\nDENSITY_HIGH... Density is " + String.valueOf(density);
	        }
	        else if (density==DisplayMetrics.DENSITY_MEDIUM) {
	         	result += "\nDENSITY_MEDIUM... Density is " + String.valueOf(density);
	            
	        }
	        else if (density==DisplayMetrics.DENSITY_LOW) {
	        	result += "\nDENSITY_LOW... Density is " + String.valueOf(density);
	            //Toast.makeText(this, "DENSITY_LOW... Density is " + String.valueOf(density),  Toast.LENGTH_LONG).show();
	        }
	        else {
	        	result += "\n Density is neither HIGH, MEDIUM OR LOW.  Density is  " + String.valueOf(density);
	            
	        }
	        
	        return result;
	
	    }
	
	
	
	
}
