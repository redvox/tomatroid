package com.example.tomatroid.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class StoredDialogs {

	public static AlertDialog getBeforeLongBreakEndDialog(Context context) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle("Times Up");
		builder.setMessage("You are done with your Pomodoro, letz take a break.");
		builder.setPositiveButton("Take the break!", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {

			}
		});
		builder.setNegativeButton("Void it!",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						
					}
				});
		builder.setNeutralButton("Skip it!",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						
					}
				});
		return builder.create();
	}

	public static AlertDialog getBeforeShortBreakEndDialog(Context context) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle("Times Up");
		builder.setMessage("You are done with your Pomodoro, letz take a break.");
		return builder.create();
	}

	public static AlertDialog getAfterBreakEndDialo(Context context) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle("Times Up");
		builder.setMessage("Your break is over, letz do some work.");
		return builder.create();
	}
}
