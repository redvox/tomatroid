package com.example.tomatroid;

import java.util.ArrayList;

import com.example.tomatroid.chrono.Chrono;
import com.example.tomatroid.chrono.Counter;
import com.example.tomatroid.digram.Bar;

import android.os.Bundle;
import android.os.Vibrator;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

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
	Chrono chrono;
	ControlListener controlListener;
	DialogManager dialogManager;

	ArrayList<View> bars = new ArrayList<View>();

	int pomodoroTime = 25;
	int shortBreakTime = 5;
	int longBreakTime = 35;

	int pomodorosNum = 1;
	int pomodorosUntilLongBreakNum = 2;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		digram = (LinearLayout) findViewById(R.id.digram);
		control = (LinearLayout) findViewById(R.id.control);
		headline = (LinearLayout) findViewById(R.id.headline);
		timeText = (TextView) findViewById(R.id.timetext);

		// Adding Bars
		for (int i = 1; i < 4; i++) {
			View bar = new Bar(digram.getContext(), i * 10);
			bars.add(bar);
			digram.addView(bar, barParams);
		}

		// Control Buttons
		controlListener = new ControlListener(this, control);
		// Dialog Manager
		dialogManager = new DialogManager(this);

		// chrono = new ChronoCounter(this);

		// timeText = new TextView(this);
		// timeText.setTextSize(50);
		timeText.setText("00:00");
		Typeface tf = Typeface.createFromAsset(getAssets(), "Roboto-Black.ttf");
		timeText.setTypeface(tf);
		timeText.setTextColor(Color.parseColor("#6495ED"));

		// headline.addView(timeText);
		// headline.addView(chrono);

	}

	@Override
	protected void onResume() {
		super.onResume();

		// if (bA.length != 0) {
		// for (int i = 0; i < bA.length; i++) {
		// bA[i].startAnimation(StoredAnimation.inFromRightAnimation(i));
		// }
		// }
		// for (int i = 0; i < bars.size(); i++) {
		// bars.get(i)
		// .startAnimation(StoredAnimation.inFromButtomAnimation(i));
		// }
	}

	public void startCounter(int minutes, int type) {
		timeText.setText("00:00");
		if (counter != null)
			counter.cancel();
		counter = new Counter(minutes, this, timeText, type);
		counter.start();
	}

	public int stopCounter() {
		timeText.setTextColor(Color.parseColor("#6495ED"));
		timeText.setText("00:00");
		counter.cancel();
		return counter.getMinutesPast();
	}

	public void counterFinish(int tag) {
		timeText.setText("00:00");
		int minutespast = counter.getMinutesPast() + 1;

		// // SOS
		// int dot = 200; // Length of a Morse Code "dot" in milliseconds
		// // int dash = 500; // Length of a Morse Code "dash" in milliseconds
		// int short_gap = 200; // Length of Gap Between dots/dashes
		// // int medium_gap = 500; // Length of Gap Between Letters
		// // int long_gap = 1000; // Length of Gap Between Words
		// long[] pattern = { 0, // Start immediately
		// dot, short_gap, dot, short_gap, dot };

		// Vibrator v = (Vibrator) getSystemService(VIBRATOR_SERVICE);
		// Only perform this pattern one time (-1 means "do not repeat")
		// v.vibrate(pattern, -1);

		counter.cancel();
		counter = counter.renew();

		Toast.makeText(this, minutespast + "mins", Toast.LENGTH_SHORT).show();
		dialogManager.show(tag, checkOnLongBreak());
	}

	/**
	 * Start the appropiate counter.
	 * 
	 * @param tag
	 */
	public void start(int tag) {
		switch (tag) {
		// Pomodoro
		case 0:
			startCounter(1, tag);
			Toast.makeText(this, "Start Pomodoro", Toast.LENGTH_SHORT).show();
			break;
		// Short Break
		case 1:
			startCounter(1, tag);
			Toast.makeText(this, "Start ShortBreak", Toast.LENGTH_SHORT).show();
			break;
		// Long Break
		case 2:
			startCounter(1, tag);
			Toast.makeText(this, "Start LongBreak", Toast.LENGTH_SHORT).show();
			break;
		// Tracking
		case 3:
			break;
		// Sleeping
		case 4:
			break;
		}
	}

	/**
	 * Cancel the counter. Write minutes past to database.
	 * 
	 * @param tag
	 */
	public void end(int tag) {
		Log.e("MainActivity", "end " + tag + " :" + counter.getMinutesPast());
		stopCounter();
		timeText.setTextColor(Color.parseColor("#6495ED"));

		switch (tag) {
		case 0:
			pomodorosNum++;
			break;
		case 1:
			break;
		case 2:
			break;
		case 3:
			break;
		case 4:
			break;
		}
	}

	/**
	 * Cancel the current counter.
	 * 
	 * @param tag
	 */
	public void stop(int tag) {
		stopCounter();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	public boolean checkOnLongBreak() {
		if (pomodorosNum % pomodorosUntilLongBreakNum == 0)
			return true;
		return false;
	}
}
