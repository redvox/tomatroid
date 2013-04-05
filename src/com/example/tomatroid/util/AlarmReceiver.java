package com.example.tomatroid.util;

import com.example.tomatroid.MainActivity;
import com.example.tomatroid.R;
import com.example.tomatroid.sql.SQHelper;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.SystemClock;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

public class AlarmReceiver extends BroadcastReceiver {
	
	static public final String KEY_TYPE = "type";
	static public final int TYPE_ONLY_NOTIFICATION = 0;
	static public final int TYPE_BUTTON_VOID = 1;
	static public final int TYPE_BUTTON_EXTEND = 2;
	static public final int TYPE_BUTTON_TAKE = 3;
	
	static public final String KEY_TAG = "tag";
	static public final String KEY_VIBRATE = "vibrate";
	
	@Override
	public void onReceive(Context context, Intent intent) {
		int type = intent.getIntExtra(KEY_TYPE, -1);
		if(type == TYPE_ONLY_NOTIFICATION){
//			setVibration(context, false);
			fireNotification(context, intent.getIntExtra(KEY_TAG, -1));

			SharedPreferences settings = context.getSharedPreferences(MainActivity.PREFS_NAME, 0);

			if(settings.getBoolean(MainActivity.KEY_VIBRATE, false))
				fireVibration(context);

			if(settings.getBoolean(MainActivity.KEY_PLAYSOUND, false))
				fireSound(context);
		} else {
//			Intent startMain = new Intent(context, MainActivity.class);
//			startMain.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
//			startMain.putExtra(KEY_TYPE, intent.getIntExtra(KEY_TYPE, -1));
//			context.startActivity(startMain);
		}
	}
	
	public static PendingIntent getPendingIntent(Context context, int type){
		Intent aIndent = new Intent(context, AlarmReceiver.class);
		aIndent.putExtra(KEY_TYPE, type);
		return PendingIntent.getBroadcast(context, 192837, aIndent, PendingIntent.FLAG_UPDATE_CURRENT);
	}
	
	public static void stopAlarmManager(Context context){
//		Log.e("MainActivity", "AlarmManager Stopped");
		AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		am.cancel(AlarmReceiver.getPendingIntent(context, 0));
	}
	
	public static void startAlarmManager(Context context, long timeinmilliesinthefuture, int tag){
//		Log.e("MainActivity", "AlarmManager Started in "+timeinmilliesinthefuture/1000);
		
		Intent aIndent = new Intent(context, AlarmReceiver.class);
		aIndent.putExtra(KEY_TYPE, TYPE_ONLY_NOTIFICATION);
		aIndent.putExtra(KEY_TAG, tag);
		PendingIntent pIntent = PendingIntent.getBroadcast(context, 192837, aIndent, PendingIntent.FLAG_UPDATE_CURRENT);
		AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		am.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime()+timeinmilliesinthefuture, pIntent);
	}
	
	public static void fireNotification(Context context, int tag){
		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
				context).setSmallIcon(R.drawable.ic_launcher);

		if(tag == SQHelper.TYPE_POMODORO){
			mBuilder.setContentTitle("Pomodoro over");
			mBuilder.setContentText("Lets take a break");
		} else {
			mBuilder.setContentTitle("Break over");
			mBuilder.setContentText("Lets do some work!");
		}
	
//		mBuilder.addAction(android.R.drawable.btn_minus, "Void it", getPendingIntent(context, TYPE_BUTTON_VOID));
//		mBuilder.addAction(android.R.drawable.btn_plus, "Extend", getPendingIntent(context, TYPE_BUTTON_EXTEND));
//		mBuilder.addAction(android.R.drawable.btn_star, "Taking", getPendingIntent(context, TYPE_BUTTON_TAKE));

		Intent resultIntent = new Intent(context, MainActivity.class);
//		resultIntent.putExtra(KEY_VIBRATE, false);
		
		mBuilder.setAutoCancel(true);
		TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
		stackBuilder.addParentStack(MainActivity.class);
		stackBuilder.addNextIntent(resultIntent);
		PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0,
				PendingIntent.FLAG_UPDATE_CURRENT);
		mBuilder.setContentIntent(resultPendingIntent);
		NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		// mId allows you to update the notification later on.
		mNotificationManager.notify(0, mBuilder.build());
	}
	
	public static void cancelNotification(Context context){
		NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		mNotificationManager.cancelAll();
	}
	
	public static void fireVibration(Context context){
		// // SOS
		int dot = 200; // Length of a Morse Code "dot" in milliseconds
		// int dash = 500; // Length of a Morse Code "dash" in milliseconds
		int short_gap = 200; // Length of Gap Between dots/dashes
		// int medium_gap = 500; // Length of Gap Between Letters
		// int long_gap = 1000; // Length of Gap Between Words
		// long[] pattern = { 0, // Start immediately
		// dot, short_gap, dot, short_gap, dot };
		long[] pattern = { 0, // Start immediately
				dot };

		Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
		// Only perform this pattern one time (-1 means "do not repeat")
		v.vibrate(pattern, -1);
	}
	
	public static void fireSound(Context context){
		Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
		Ringtone r = RingtoneManager.getRingtone(context, notification);
		r.play();
	}

	public static void setVibration(Context context, boolean bool){
		SharedPreferences settings = context.getSharedPreferences(MainActivity.PREFS_NAME, 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putBoolean(KEY_VIBRATE, bool);
		editor.commit();
	}
}
