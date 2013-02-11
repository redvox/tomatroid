package com.example.tomatroid.chrono;

import com.example.tomatroid.MainActivity;

import android.content.Context;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.widget.TextView;

public class Counter extends CountDownTimer {

	TextView timeText;
	Context context;
	boolean countDown = true;
	int minutespast = 0;
	MainActivity mA;

	public Counter(int minutes, MainActivity main, TextView timeText) {
		super((minutes * 60) * 1000, 1000);
		this.timeText = timeText;
		this.context = main;
		this.mA = main;
	}

	@Override
	public void onFinish() {
		mA.counterFinish();
	}
	
	public int getMinutesPast(){
		return minutespast;
	}

	@Override
	public void onTick(long millisUntilFinished) {
		int secs = ((int) millisUntilFinished % 60000) / 1000;
		int mins = (int) (millisUntilFinished / 60000);
		StringBuffer stb = new StringBuffer();
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
