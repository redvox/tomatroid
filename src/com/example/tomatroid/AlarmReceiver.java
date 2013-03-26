package com.example.tomatroid;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class AlarmReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.e("AlarmReceiver","AAAAALAAAARM!");
		Toast.makeText(context, "Alarm went off", Toast.LENGTH_SHORT).show();
	}
}
