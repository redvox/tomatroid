package com.example.tomatroid;

import java.util.ArrayList;

import com.example.tomatroid.chrono.Counter;
import com.example.tomatroid.digram.Bar;

import android.os.Bundle;
import android.app.Activity;
import android.graphics.Typeface;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

public class MainActivity extends Activity {

	LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,
			LayoutParams.MATCH_PARENT);

	LayoutParams barParams = new TableLayout.LayoutParams(
			LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 1f);

	LinearLayout digram;
	LinearLayout control;
	LinearLayout headline;

	TextView timeText;
	Counter counter;
	ControlListener controlListener; 

	ArrayList<View> bars = new ArrayList<View>();

	int pomodoroTime = 25;
	int shortBreakTime = 5;
	int longBreakTime = 35;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		digram = (LinearLayout) findViewById(R.id.digram);
		control = (LinearLayout) findViewById(R.id.control);
		headline = (LinearLayout) findViewById(R.id.headline);

		// Adding Bars
		for (int i = 1; i < 4; i++) {
			View bar = new Bar(digram.getContext(), i * 10);
			bars.add(bar);
			digram.addView(bar, barParams);
		}

		// Control Buttons
		controlListener = new ControlListener(this, control);

		timeText = new TextView(this);
		timeText.setTextSize(50);
		timeText.setText("00:00");
		Typeface tf = Typeface.createFromAsset(getAssets(), "Roboto-Black.ttf");
		timeText.setTypeface(tf);
		headline.addView(timeText);
	}

	@Override
	protected void onResume() {
		super.onResume();

//		if (bA.length != 0) {
//			for (int i = 0; i < bA.length; i++) {
//				bA[i].startAnimation(StoredAnimation.inFromRightAnimation(i));
//			}
//		}
//		for (int i = 0; i < bars.size(); i++) {
//			bars.get(i)
//					.startAnimation(StoredAnimation.inFromButtomAnimation(i));
//		}
	}
	
	public void startCounter(int minutes){
		timeText.setText("00:00");
		if (counter != null)
			counter.cancel();
		counter = new Counter(minutes, this, timeText);
		counter.start();
	}
	
	public int stopCounter(){
		timeText.setText("00:00");
		counter.cancel();
		return counter.getMinutesPast();
	}

	public void startPomodoro() {
		startCounter(pomodoroTime);
	}
	
	public void stopPomodoro(){
	}
	
	public void startShortBreak(){
		startCounter(shortBreakTime);
	}
	
	public void stopShortBreak(){
	}
	
	public void startLongBreak(){
		startCounter(longBreakTime);
	}
	
	public void stopLongBreak(){
		startCounter(longBreakTime);
	}
	
	public void counterFinish() {
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
}
