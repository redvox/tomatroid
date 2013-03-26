package com.example.tomatroid.chrono;

import com.example.tomatroid.MainActivity;

import android.content.Context;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.widget.TextView;

public class Counter extends CountDownTimer {

	TextView timeText;
	Context context;
	long runTime = 0;
	long timeLeft = 0;
	long timeBase = 0;
	int tag;
	MainActivity mA;
	boolean countUp = false;

	public Counter(int minutes, MainActivity main, TextView timeText, int tag) {
		super((minutes * 60) * 1000, 1000);
		this.runTime = (minutes * 60) * 1000;
		this.timeText = timeText;
		this.context = main;
		this.mA = main;
		this.tag = tag;
	}

	public Counter(long millies, MainActivity main, TextView timeText, int tag) {
		super(millies, 1000);
		this.runTime = millies;
		this.timeText = timeText;
		this.context = main;
		this.mA = main;
		this.tag = tag;
	}

	@Override
	public void onFinish() {
		timeText.setText("00:00");
		mA.counterFinish(tag);
	}

	public int getMinutesPast() {
		return (int) Math.round((((runTime - timeLeft) + timeBase) / 60000));
	}

	public long getMilliesPast(){
		return (runTime - timeLeft) ;
	}

	public long getMilliesLeft() {
		return timeLeft;
	}

	public long getMilliesBase() {
		return runTime + timeBase;
	}

	public long getMilliesRawBase() {
		return timeBase;
	}

	public void toggleCountUp() {
		countUp = true;
		timeText.setTextColor(Color.parseColor("#DC143C"));
	}

	public boolean isCountUp() {
		return countUp;
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
			millies = getMilliesPast() + timeBase;
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
