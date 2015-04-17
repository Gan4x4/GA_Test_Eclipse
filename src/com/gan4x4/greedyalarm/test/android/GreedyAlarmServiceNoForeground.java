package com.gan4x4.greedyalarm.test.android;


import android.content.Intent;


import com.gan4x4.greedyalarm.GreedyAlarmService;


public class GreedyAlarmServiceNoForeground extends GreedyAlarmService {

	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		//Notification n = getNotification();
		
		//ganAlarm.
		 
		//Notification n = getNotification("GA","GA service installed");
		//startForeground( NOTIFICATION_ID, n  );
		
		
		//boolean background = intent.getBooleanExtra("background", false); 
		//Log.d(MainActivity.TAG, "Service Extra "+background);
		
		//if (background) initDataOnStart();
		
		//return super.onStartCommand(intent, flags, startId);
		return onStartCommandForTest(intent,flags,startId);
	}
}
