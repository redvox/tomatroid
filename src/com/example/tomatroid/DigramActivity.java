package com.example.tomatroid;

import java.util.ArrayList;

import com.example.tomatroid.digram.Axis;
import com.example.tomatroid.digram.Bar;
import com.example.tomatroid.sql.SQHelper;

import android.os.Bundle;
import android.app.Activity;
import android.database.Cursor;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;

public class DigramActivity extends Activity {

	SQHelper sqHelper;
	RelativeLayout main;

	Bar pomodoroBar;
	Bar breakBar;
	Axis axis;

	int howManyDays = 14;

	LayoutParams barParams = new TableLayout.LayoutParams(
			LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 1f);

	LayoutParams params = new TableLayout.LayoutParams(
			LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_digram);

		LayoutInflater mInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

		SQHelper sqHelper = new SQHelper(this);
		RelativeLayout main = (RelativeLayout) findViewById(R.id.maindigram);

		LinearLayout digramLayout = new LinearLayout(this);
		main.addView(digramLayout);
		LinearLayout axisLayout = new LinearLayout(this);
		main.addView(axisLayout);

		Cursor c = sqHelper.getLastXDays(howManyDays);

		ArrayList<Integer> pomodoroTime = new ArrayList<Integer>();
		ArrayList<Integer> breakTime = new ArrayList<Integer>();
		ArrayList<Integer> gameTime = new ArrayList<Integer>();
		ArrayList<Integer> weekdays = new ArrayList<Integer>();

		int maxValue = 0;

		if (c.moveToFirst()) {
			int day = c.getInt(0);
			int pomodoroT = 0;
			int breakT = 0;
			int gameT = 0;
			while (c.moveToNext()) {
				if (day != c.getInt(0)) {
					weekdays.add(c.getInt(0));
					pomodoroTime.add(pomodoroT);
					breakTime.add(breakT);
					gameTime.add(gameT);

					day = c.getInt(0);
					pomodoroT = 0;
					breakT = 0;
					gameT = 0;
				}

				switch (c.getInt(1)) {
				case SQHelper.TYPE_POMODORO:
					pomodoroT += c.getInt(2);
				case SQHelper.TYPE_LONGBREAK:
					breakT += c.getInt(2);
				case SQHelper.TYPE_SHORTBREAK:
					breakT += c.getInt(2);
				case SQHelper.TYPE_TRACKING:
					gameT += c.getInt(2);
				}
				
				if (pomodoroT > maxValue)
					maxValue = pomodoroT;
				if (breakT > maxValue)
					maxValue = breakT;
				if (gameT > maxValue)
					maxValue = gameT;
			}

			weekdays.add(8);
			pomodoroTime.add(pomodoroT);
			breakTime.add(breakT);
			gameTime.add(gameT);
		}

		// if (weekdays.size() != howManyDays) {
		// DateTime dt = DateTime.now().minusDays(howManyDays);
		// int start_day = dt.getDayOfWeek();
		// if (weekdays.get(0) != start_day) {
		// for (int i1 = 0; i1 < getDayDistance(weekdays.get(0), start_day);
		// i1++) {
		// weekdays.add(0, weekdays.get(i1) + 1);
		// pomodoroTime.add(0, 0);
		// breakTime.add(0, 0);
		// }
		// }
		//
		// for (int i2 = 0; i2 < howManyDays; i2++) {
		// if (getDayDistance(weekdays.get(i2), weekdays.get(i2 + 1)) != 1) {
		// for (int i3 = 0; i3 < getDayDistance(weekdays.get(i2),
		// weekdays.get(i2 + 1)); i3++) {
		// weekdays.add(i2, weekdays.get(i2) + 1);
		// pomodoroTime.add(0, 0);
		// breakTime.add(0, 0);
		// }
		// }
		// }
		// }

		for (int i = 0; i < weekdays.size(); i++) {
			LinearLayout ll = new LinearLayout(this);
			ll.setOrientation(LinearLayout.VERTICAL);

			TextView text = new TextView(this);
			text.setText(convertIntoWeekday(weekdays.get(i)));
			text.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);

			LinearLayout l1 = new LinearLayout(this);

			Bar pomodoroBar = new Bar(null, this, pomodoroTime.get(i),
					"#fdf700", maxValue);
			l1.addView(pomodoroBar, barParams);
			Bar breakBar = new Bar(null, this, breakTime.get(i), "#04B404",
					maxValue);
			l1.addView(breakBar, barParams);
			Bar gameBar = new Bar(null, this, gameTime.get(i), "#B452CD",
					maxValue);
			l1.addView(gameBar, barParams);

			ll.addView(text);
			ll.addView(l1);
			digramLayout.addView(ll, barParams);
		}
	}

	// @Override
	// public boolean onCreateOptionsMenu(Menu menu) {
	// getMenuInflater().inflate(R.menu.activity_digram, menu);
	// return true;
	// }

	private String convertIntoWeekday(int day) {
		switch (day) {
		case 1:
			return "Monday";
		case 2:
			return "Tuesday";
		case 3:
			return "Wednesday";
		case 4:
			return "Thursday";
		case 5:
			return "Friday";
		case 6:
			return "Saturday";
		case 7:
			return "Sunday";
		default:
			return "Today";
		}
	}

	public int getDayDistance(int start_day, int end_day) {
		int[] week = new int[] { 1, 2, 3, 4, 5, 6, 7 };
		int i = start_day - 1;
		int distance = 0;
		while (week[i] != end_day) {
			distance++;
			if (i > week.length)
				i = 0;
		}
		return distance;
	}
}
