package com.example.tomatroid;

import java.util.ArrayList;

import com.example.tomatroid.chrono.Counter;
import com.example.tomatroid.digram.Axis;
import com.example.tomatroid.digram.Bar;
import com.example.tomatroid.digram.PieChart;
import com.example.tomatroid.sql.SQHelper;

import android.os.Bundle;
import android.os.SystemClock;
import android.os.Vibrator;
import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Chronometer;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,
			LayoutParams.MATCH_PARENT);

	LayoutParams barParams = new TableLayout.LayoutParams(
			LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 1f);

	SQHelper sqhelper = new SQHelper(this);

	RelativeLayout digram;
	LinearLayout headline;

	Chronometer timeText;
	TextView pomodorosNumText;
	Counter counter;
	ControlListener controlListener;
	DialogManager dialogManager;

	ArrayList<View> bars = new ArrayList<View>();

	int pomodoroTime = 1;
	int shortBreakTime = 1;
	int longBreakTime = 1;
	int rememberTime = 1;
	String trackingTheme = "Kein Thema";
	boolean tracking = false;

	int pomodorosNum = 1;
	int pomodorosUntilLongBreakNum = 2;

	Bar pomodoroBar;
	Bar breakBar;
	Axis axis;

	final String KEY_THEME = "theme";
	final String KEY_CHRONOSTATE = "chrono";
	final String KEY_TRACKINGSTATE = "tracking";
	final String KEY_ACTIVEBUTTON = "button";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		digram = (RelativeLayout) findViewById(R.id.digram);
		headline = (LinearLayout) findViewById(R.id.headline);
		timeText = (Chronometer) findViewById(R.id.timetext);
		pomodorosNumText = (TextView) findViewById(R.id.pomodorosNum);

		pomodorosNum = sqhelper.getStartUpPomodoroCount();
		pomodorosNumText.setTextColor(Color.parseColor("#fdf700"));
		pomodorosNumText.setText("" + pomodorosNum);

		LinearLayout digramLayout = new LinearLayout(this);
		digram.addView(digramLayout);
		LinearLayout axisLayout = new LinearLayout(this);
		digram.addView(axisLayout);

		// Calculate for Bars
		int startPomodoroTime = sqhelper.getStartUpPomodoroTime();
		int startBreakTime = sqhelper.getStartUpBreakTime();
		float maxStartTime = Math.max(startPomodoroTime, startBreakTime);
		int maxValue = (int) Math.max(60, ((maxStartTime / 100) * 125));

		axis = new Axis(digram.getContext(), maxValue);
		axisLayout.addView(axis);

		// float values[]={3456,5734,5735,5477,9345,3477};
		// PieChart pie = new PieChart(this, values);
		// digramLayout.addView(pie);

		pomodoroBar = new Bar(this, digram.getContext(), startPomodoroTime,
				"#fdf700", maxValue);
		digramLayout.addView(pomodoroBar, barParams);

		breakBar = new Bar(this, digram.getContext(), startBreakTime,
				"#04B404", maxValue);
		digramLayout.addView(breakBar, barParams);

		// Control Buttons
		controlListener = new ControlListener(this, sqhelper);
		// Dialog Manager
		dialogManager = new DialogManager(this);

		timeText.setText("00:00");
		// Typeface tf = Typeface.createFromAsset(getAssets(),
		// "Roboto-Black.ttf");
		// timeText.setTypeface(tf);

		Typeface tf = Typeface.createFromAsset(getAssets(), "wwDigital.ttf");
		timeText.setTypeface(tf);
		timeText.setTextColor(Color.parseColor("#6495ED"));
		
		if (savedInstanceState != null) {
			trackingTheme = savedInstanceState.getString(KEY_THEME);
			
			tracking = savedInstanceState.getBoolean(KEY_TRACKINGSTATE);
			if (tracking) {
				timeText.setBase(savedInstanceState.getLong(KEY_CHRONOSTATE));
				controlListener.toogle(savedInstanceState.getInt(KEY_ACTIVEBUTTON));
//				controlListener.start(savedInstanceState.getInt(KEY_ACTIVEBUTTON));
				timeText.start();
			}
			Log.e("MainActivity", "Load: " + timeText.getBase());
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putString(KEY_THEME, trackingTheme);
//		long elapsedMillis = SystemClock.elapsedRealtime() - timeText.getBase();
		long elapsedMillis = timeText.getBase();
		outState.putLong(KEY_CHRONOSTATE, elapsedMillis);
		outState.putBoolean(KEY_TRACKINGSTATE, tracking);
		outState.putInt(KEY_ACTIVEBUTTON, controlListener.activeButton);
		Log.e("MainActivity", "Save: " + elapsedMillis);
	}

	public void startCounter(int minutes, int type) {
		if (counter != null)
			counter.cancel();
		counter = new Counter(minutes, this, timeText, type);
		counter.start();
	}

	public void stopCounter() {
		if (counter != null)
			counter.cancel();
	}

	public synchronized void counterFinish(int tag) {
		resetTimeText();
		int minutespast = counter.getMinutesPast();
		long milliesBase = counter.getMilliesBase();

		Counter newCounter = new Counter(rememberTime, this, timeText, tag);
		newCounter.toggleCountUp();
		newCounter.setBaseTime(milliesBase);

		// timeText = null;
		// context = null;
		// mA = null;
		newCounter.start();

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

		Vibrator v = (Vibrator) getSystemService(VIBRATOR_SERVICE);
		// Only perform this pattern one time (-1 means "do not repeat")
		v.vibrate(pattern, -1);

		counter = newCounter;

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
			startCounter(pomodoroTime, tag);
			Toast.makeText(this, "Start Pomodoro", Toast.LENGTH_SHORT).show();
			break;
		// Short Break
		case 1:
			startCounter(shortBreakTime, tag);
			Toast.makeText(this, "Start ShortBreak", Toast.LENGTH_SHORT).show();
			break;
		// Long Break
		case 2:
			startCounter(longBreakTime, tag);
			Toast.makeText(this, "Start LongBreak", Toast.LENGTH_SHORT).show();
			break;
		// Tracking
		case 3:
			timeText.setBase(SystemClock.elapsedRealtime());
			timeText.start();
			tracking = true;
			break;
		// Sleeping
		case 4:
			timeText.setBase(SystemClock.elapsedRealtime());
			timeText.start();
			tracking = true;
			break;
		}
	}

	/**
	 * Cancel the counter. Write minutes past to database.
	 * 
	 * @param tag
	 */
	public void end(int tag) {
		int minutes;
		if (tracking) {
			timeText.stop();
			long myElapsedMillis = SystemClock.elapsedRealtime()
					- timeText.getBase();
			minutes = (int) (myElapsedMillis / 60000);
			tracking = false;
		} else {
			stopCounter();
			minutes = counter.getMinutesPast();
		}

		resetTimeText();

		// #######
		minutes = 10;
		// #######

		if (minutes > 0) {
			if (tag == 0) {
				pomodorosNum++;
				pomodorosNumText.setText("" + pomodorosNum);
				pomodoroBar.addValue(minutes);
			} else if (tag == 1 || tag == 2) {
				breakBar.addValue(minutes);
			}
			Log.e("MainActivity", "end Tag: " + tag + " Duration: " + minutes
					+ " Theme: " + trackingTheme);
			sqhelper.insertDate(tag, minutes, trackingTheme);
		}
	}

	/**
	 * Cancel the current counter.
	 * 
	 * @param tag
	 */
	public void stop() {
		if (tracking) {
			timeText.stop();
			tracking = false;
		} else {
			stopCounter();
		}
		resetTimeText();
	}

	public void resetTimeText() {
		timeText.setTextColor(Color.parseColor("#6495ED"));
		timeText.setText("00:00");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.delete_history:
			sqhelper.renewTables();
			onCreate(null);
			break;
		case R.id.menu_void:
			stop();
			controlListener.stop();
		case R.id.menu_digram:
			Intent i = new Intent(this, DigramActivity.class);
			startActivity(i);
		default:

		}

		return super.onOptionsItemSelected(item);
	}

	public boolean checkOnLongBreak() {
		if (pomodorosNum == 0)
			return false;
		if ((pomodorosNum + 1) % pomodorosUntilLongBreakNum == 0)
			return true;
		return false;
	}

	public void barExceededLimit(int oldMax) {
		float cal = ((float) oldMax / 100f) * 125f;
		int newMax = (int) cal;
		pomodoroBar.adjustToNewMaximum(newMax);
		breakBar.adjustToNewMaximum(newMax);
		axis.adjustToNewMaximum(newMax);
	}
}
