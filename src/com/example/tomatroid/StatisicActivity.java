package com.example.tomatroid;

import java.util.ArrayList;

import org.joda.time.DateMidnight;

import com.example.tomatroid.digram.BarChart;
import com.example.tomatroid.digram.DayBarChart;
import com.example.tomatroid.digram.LineChart;
import com.example.tomatroid.digram.PieChart;
import com.example.tomatroid.sql.SQHelper;
import com.example.tomatroid.util.NavigationBarManager;
import com.example.tomatroid.util.StoredAnimation;
import com.example.tomatroid.util.Util;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

public class StatisicActivity extends Activity {

	final int ACTIVITYNUMBER = 1;

	LinearLayout overview;
	ViewFlipper statistic_flipper;
	ImageButton prev;
	LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT,
			LayoutParams.WRAP_CONTENT);
	LayoutParams barParams = new TableLayout.LayoutParams(
			LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 1f);
	SQHelper sqHelper = new SQHelper(this);

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_statictic);

		NavigationBarManager navi = new NavigationBarManager(this,
				ACTIVITYNUMBER);

		overview = (LinearLayout) findViewById(R.id.statistic_overview);
		statistic_flipper = (ViewFlipper) findViewById(R.id.statistic_flipper);
		statistic_flipper.setAnimateFirstView(false);

		prev = (ImageButton) findViewById(R.id.statistic_prev);
		prev.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				statistic_flipper.setAnimation(StoredAnimation
						.inFromRightAnimation(1));
				statistic_flipper.showPrevious();
				Log.e("Image Button", "clicked");
			}
		});

		int totalPomodoro = sqHelper.getSum(SQHelper.KEY_DURATION, new String[][]{{SQHelper.KEY_TYPE}}, new int[][]{{SQHelper.TYPE_POMODORO}});
		int totalBreak = sqHelper.getSum(SQHelper.KEY_DURATION, new String[][]{{SQHelper.KEY_TYPE},{SQHelper.KEY_TYPE}}, new int[][]{{SQHelper.TYPE_LONGBREAK},{SQHelper.TYPE_SHORTBREAK}});
		int totalTracking = sqHelper.getSum(SQHelper.KEY_DURATION, new String[][]{{SQHelper.KEY_TYPE}}, new int[][]{{SQHelper.TYPE_TRACKING}});
		
		// Pomodoro Count
		TextView pomodoroCount = (TextView) findViewById(R.id.statistic_pomodoroCount);
		pomodoroCount.setText("" + sqHelper.getCount(SQHelper.KEY_ROWID, new String[][]{{SQHelper.KEY_TYPE}}, new int[][]{{SQHelper.TYPE_POMODORO}}));

		// Pomodoro Overview
		TextView pomodoroInfo = (TextView) findViewById(R.id.statistic_pomodoroInfo);
		pomodoroInfo.setText(prepareInfoText(totalPomodoro, SQHelper.TYPE_POMODORO));

		// Long Break Overview
		TextView breakInfo = (TextView) findViewById(R.id.statistic_breakInfo);
		breakInfo.setText(prepareInfoText(totalBreak, SQHelper.TYPE_LONGBREAK));

		PieChart breakPieChart = (PieChart) findViewById(R.id.breakPieChart);
		breakPieChart.setValues(new float[] { 0 });

		// Sleep Overview;
		TextView sleepInfo = (TextView) findViewById(R.id.statistic_sleepInfo);
		sleepInfo.setText(prepareInfoText(totalTracking, SQHelper.TYPE_SLEEPING));

		// Overview Pie Chart
		PieChart overviewPieChart = (PieChart) findViewById(R.id.overviewPieChart);
		overviewPieChart.setValuesWithColor(
				new float[] { 
						totalPomodoro, 
						totalBreak,
						totalTracking },
				new int[] {
						MainActivity.COLOR_POMODORO,
						MainActivity.COLOR_BREAK,
						MainActivity.COLOR_TRACKING });

		// Theme List Overview
		ArrayList<String> valueList = new ArrayList<String>();
		Cursor c = sqHelper.getThemeCursor();
		if (c.moveToFirst()) {
			do {
				valueList.add(c.getString(c.getColumnIndex(SQHelper.KEY_NAME)));
			} while (c.moveToNext());
		}
		c.close();
		ListView themelist = (ListView) findViewById(R.id.statistic_themelist);
		themelist.setAdapter(new ThemeListAdapter(getApplicationContext(),
				R.layout.theme_statistic_list_row, R.id.themeText, valueList));

		// int[] barValues = new int[] { 3, 10, 4, 5, 6, 7, 8 };
		// BarChart multiBar = new BarChart(this, barValues);+
		// statistic_flipper.addView(multiBar);

		DayBarChart daychart = new DayBarChart(this);
		statistic_flipper.addView(daychart);

		int[][] barValues1 = new int[][] { new int[] { 3, 10, 4, 5, 6, 7, 8 },
				new int[] { 3, 3, 3, 3, 3, 3, 3 },
				new int[] { 1, 1, 1, 1, 1, 1, 1 }, };
		String[] axisLables = new String[] { "Mo", "Di", "Mi", "Do", "Fr",
				"Sa", "So" };
		String[] lineNames = new String[] { "Pomodoro", "Breaks", "Sleep" };
		LineChart multiline = new LineChart(this, barValues1, axisLables,
				lineNames);
		statistic_flipper.addView(multiline);

		// float values[] = { 3456, 5734, 5735, 5477, 9345, 3477 };
		// PieChart pie = new PieChart(this, values);
		// overview.addView(pie, 0, barParams);

		// themelist.addView(pie);
		//

		// TextView t1 = new TextView(this);
		// t1.setText("Total Pomodoro");
		// overview.addView(t1, params);

	}

	@Override
	protected void onResume() {
		super.onResume();
		getActionBar().setSelectedNavigationItem(ACTIVITYNUMBER);
	}

	public String prepareInfoText(int totalDuration, int type) {
		int daysAmount = sqHelper.getGroupCount(new String[]{SQHelper.KEY_DATE_DAY, SQHelper.KEY_DATE_MONTH, SQHelper.KEY_DATE_YEAR}, new String[][]{{SQHelper.KEY_TYPE}}, new int[][]{{type}});
		int average = 0;
		if(daysAmount > 0){
			average = totalDuration / daysAmount;
		}
		
		
		if (totalDuration != 0) {
			return Util.generateTimeText(totalDuration) + " total\n"
					+ Util.generateTimeText(average) + " per Day\n"
					+ "(over " + daysAmount + " days)";
		} else {
			return "nothing recorded yet";
		}
	}

	class ThemeListAdapter extends ArrayAdapter<String> {

		ArrayList<String> values;
		int totalWithoutSleep ;
		
		public ThemeListAdapter(Context context, int layoutViewResourceId,
				int textViewResourceId, ArrayList<String> values) {
			super(context, layoutViewResourceId, textViewResourceId, values);
			this.values = values;
			totalWithoutSleep = sqHelper.getTotalDurationWithoutSleep(); 
		}

		int rank = 1;

		@Override
		public View getView(int position, View v, ViewGroup parent) {
			LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.theme_statistic_list_row, null);

			int themeId = sqHelper.getTheme(values.get(position));

			TextView rankText = (TextView) v.findViewById(R.id.rankText);
			rankText.setText("" + rank);
			rankText.append(". ");

			int totalDuration = sqHelper.getSum(SQHelper.KEY_DURATION, new String[][]{{SQHelper.KEY_THEME}}, new int[][]{{themeId}});
			
			TextView infoText = (TextView) v.findViewById(R.id.infoText);
			infoText.setText(prepareInfoText(totalDuration, themeId));
			
			DateMidnight dm = new DateMidnight();
			dm = dm.minusDays(7);
			int[] barValues = new int[7];
			for (int i = 0; i < 7; i++) {
				dm = dm.plusDays(1);
				barValues[i] = sqHelper.getSum(SQHelper.KEY_DURATION, new String[][]{{
					SQHelper.KEY_THEME, 
					SQHelper.KEY_DATE_DAY, 
					SQHelper.KEY_DATE_MONTH, 
					SQHelper.KEY_DATE_YEAR}}, 
					new int[][]{{
						themeId,
						dm.getDayOfMonth(), 
						dm.getMonthOfYear(), 
						dm.getYear()}}); 
			}
			BarChart bars = new BarChart(getContext(), barValues);

			// LinearLayout ll = (LinearLayout) v.findViewById(R.id.infoLayout);
			// ll.addView(bars, 100, 100);

			BarChart bars2 = (BarChart) v.findViewById(R.id.barChart);
			bars2.setValues(barValues);

			PieChart pieChart = (PieChart) v.findViewById(R.id.pieChart);
			int pomodoroDuration = sqHelper.getSum(SQHelper.KEY_DURATION, new String[][]{{SQHelper.KEY_TYPE, SQHelper.KEY_THEME}}, new int[][]{{0, themeId}});
			int breakDuration = sqHelper.getSum(SQHelper.KEY_DURATION, new String[][]{{SQHelper.KEY_TYPE, SQHelper.KEY_THEME}}, new int[][]{{2, themeId}});
			int trackDuration = sqHelper.getSum(SQHelper.KEY_DURATION, new String[][]{{SQHelper.KEY_TYPE, SQHelper.KEY_THEME}}, new int[][]{{3, themeId}});
			
			pieChart.setValuesWithColor(
					new float[] { 
							pomodoroDuration,
							breakDuration,
							trackDuration,
							totalWithoutSleep  - (pomodoroDuration + breakDuration + trackDuration)
					}, 
					new int[] { 
					MainActivity.COLOR_POMODORO,
					MainActivity.COLOR_BREAK,
					MainActivity.COLOR_TRACKING,
					MainActivity.COLOR_SLEEP
					});
			
			rank++;
			return super.getView(position, v, parent);
		}
	}
}
