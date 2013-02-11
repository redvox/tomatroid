package com.example.tomatroid.chrono;

import com.example.tomatroid.MainActivity;

import android.R;
import android.content.Context;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.util.Log;
import android.widget.TextView;

public class Counter extends CountDownTimer {

	TextView timeText;
	Context context;
	long timeFinal = 0;
	long timeLeft = 0;
	long timeBase = 0;
	int type;
	MainActivity mA;
	boolean countUp = false;

	public Counter(int minutes, MainActivity main, TextView timeText, int type) {
		super((minutes * 60) * 1000, 1000);
		this.timeFinal = (minutes * 60) * 1000;
		this.timeText = timeText;
		this.context = main;
		this.mA = main;
		this.type = type;
		// Set Time Blue
		timeText.setTextColor(Color.parseColor("#6495ED"));
	}

	@Override
	public void onFinish() {
		timeText.setText("00:00");
		mA.counterFinish(type);
	}

	public int getMinutesPast() {
		return (int) Math.round((((timeFinal - timeLeft) + timeBase) / 60000));
	}

	public long getMilliesPast() {
//		Log.e("Counter", "timeLeft "+timeLeft);
//		Log.e("Counter", "timeFinal "+timeFinal);
//		Log.e("Counter", "timeBase "+timeBase);
//		Log.e("Counter", "Gesamt "+(timeFinal - timeLeft) + timeBase);
		return (timeFinal - timeLeft) + timeBase;
	}
	
	public long getMilliesBase() {
		return timeFinal + timeBase;
	}

	public Counter renew() {
		Counter counter = new Counter(1, mA, timeText, type);
		counter.toggleCountUp();
		counter.setBaseTime(getMilliesBase());

		timeText = null;
		context = null;
		mA = null;
		Log.e("Counter", "###################");
		counter.start();
		return counter;
	}

	public void toggleCountUp() {
		countUp = !countUp;
		timeText.setTextColor(Color.parseColor("#DC143C"));
	}

	public void setBaseTime(long millies) {
		timeBase = millies;
	}

	@Override
	public void onTick(long millisUntilFinished) {
		timeLeft = millisUntilFinished;
		StringBuffer stb = new StringBuffer();

		long millies = 0;
		
		if (countUp) {
			millies = getMilliesPast();

		} else {
			millies = millisUntilFinished;
		}

		int secs = ((int) millies % 60000) / 1000;
		int mins = (int) (millies / 60000);
		if (mins < 10)
			stb.append("0");
		stb.append(mins);
		stb.append(":");
		if (secs < 10)
			stb.append("0");
		stb.append(secs);
		
		timeText.setText(stb);
	}
}
