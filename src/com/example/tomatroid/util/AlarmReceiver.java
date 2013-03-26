package com.example.tomatroid.util;

import com.example.tomatroid.MainActivity;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class AlarmReceiver extends BroadcastReceiver {

	MainActivity mA;
	
	public AlarmReceiver(MainActivity mA){
		mA.counterFinish(0);
	}
	
	@Override
	public void onReceive(Context context, Intent intent) {
		Log.e("AlarmReceiver","AAAAALAAAARM!");
		Toast.makeText(context, "Alarm went off", Toast.LENGTH_SHORT).show();
	}
	
	public static PendingIntent getPendingIntent(Context context){
		Intent AlarmIndent2 = new Intent(context, AlarmReceiver.class);
		return PendingIntent.getBroadcast(context, 192837, AlarmIndent2, PendingIntent.FLAG_UPDATE_CURRENT);
	}
}
