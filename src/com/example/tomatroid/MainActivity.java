package com.example.tomatroid;

import java.util.ArrayList;

import com.example.tomatroid.chrono.Chrono;
import com.example.tomatroid.chrono.Counter;
import com.example.tomatroid.digram.Axis;
import com.example.tomatroid.digram.Bar;
import com.example.tomatroid.sql.SQHelper;

import android.os.Bundle;
import android.os.Vibrator;
import android.app.Activity;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
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

	SQHelper sqhelper;

	RelativeLayout digram;
	LinearLayout control;
	LinearLayout headline;

	TextView timeText;
	TextView pomodorosNumText;
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

	Bar pomodoroBar;
	Bar breakBar;
	Axis axis;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		sqhelper = new SQHelper(this);

		digram = (RelativeLayout) findViewById(R.id.digram);
		control = (LinearLayout) findViewById(R.id.control);
		headline = (LinearLayout) findViewById(R.id.headline);
		timeText = (TextView) findViewById(R.id.timetext);
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

		pomodoroBar = new Bar(this, digram.getContext(), startPomodoroTime,
				"#fdf700", maxValue);
		bars.add(pomodoroBar);
		digramLayout.addView(pomodoroBar, barParams);

		breakBar = new Bar(this, digram.getContext(), startBreakTime,
				"#04B404", maxValue);
		bars.add(breakBar);
		digramLayout.addView(breakBar, barParams);

		// Control Buttons
		controlListener = new ControlListener(this, control);
		// Dialog Manager
		dialogManager = new DialogManager(this);

		// chrono = new ChronoCounter(this);

		timeText.setText("00:00");
		Typeface tf = Typeface.createFromAsset(getAssets(), "Roboto-Black.ttf");
		timeText.setTypeface(tf);
		timeText.setTextColor(Color.parseColor("#6495ED"));

		// headline.addView(timeText);
		// headline.addView(chrono);
	}

	@Override
	protected void onPause() {
		super.onPause();
		Log.e("MainActivity", "onPause");
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
		Log.e("MainActivity", "onResume");
	}

	public void startCounter(int minutes, int type) {
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

	public synchronized void counterFinish(int tag) {
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
		int minutes = counter.getMinutesPast();
		// #######
		minutes = 10;
		// #######
		Log.e("MainActivity", "end " + tag + " :" + minutes);
		stopCounter();
		timeText.setTextColor(Color.parseColor("#6495ED"));

		if (minutes > 0) {
			if (tag == 0) {
				pomodorosNum++;
				pomodorosNumText.setText("" + pomodorosNum);
				pomodoroBar.addValue(minutes);
			} else if (tag == 1 || tag == 2) {
				breakBar.addValue(minutes);
			}
			sqhelper.insertDate(tag, minutes);
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

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// switch (item.getItemId()) {
		// case R.id.delete_history:
		// deleteHistory();
		// return true;
		//
		// default:
		//
		// }
		sqhelper.renewTables();
			onCreate(null);

		return super.onOptionsItemSelected(item);
	}

	public boolean checkOnLongBreak() {
		if (pomodorosNum % pomodorosUntilLongBreakNum == 0)
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
