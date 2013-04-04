package com.example.tomatroid;

import java.util.ArrayList;


import com.example.tomatroid.digram.BarChart;
import com.example.tomatroid.digram.PieChart;
import com.example.tomatroid.sql.SQHelper;
import com.example.tomatroid.util.NavigationBarManager;
import com.example.tomatroid.util.Util;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TextView;

public class StatisicActivity extends Activity {

	final int ACTIVITYNUMBER = 1;

	LinearLayout overview;
	LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT,
			LayoutParams.WRAP_CONTENT);
	LayoutParams barParams = new TableLayout.LayoutParams(
			LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 1f);
	SQHelper sqHelper = new SQHelper(this);
	int activityDiagramLength = 14;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_statictic);

		NavigationBarManager navi = new NavigationBarManager(this,
				ACTIVITYNUMBER);

		int totalPomodoro = sqHelper.getSum(
				SQHelper.KEY_DURATION, 
				new String[][]{{
					SQHelper.KEY_TYPE
					}}, 
					new int[][]{{
						SQHelper.TYPE_POMODORO
						}});
		
		int totalBreak = sqHelper.getSum(
				SQHelper.KEY_DURATION, 
				new String[][]{
						{SQHelper.KEY_TYPE},
						{SQHelper.KEY_TYPE}}, 
						new int[][]{
						{SQHelper.TYPE_LONGBREAK},
						{SQHelper.TYPE_SHORTBREAK}
						});
		
		int totalTracking = sqHelper.getSum(
				SQHelper.KEY_DURATION, 
				new String[][]{{
					SQHelper.KEY_TYPE
					}}, new int[][]{{
							SQHelper.TYPE_TRACKING
							}});
		
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
		
		ArrayList<String> nameList = new ArrayList<String>();
		ArrayList<Integer> idList = new ArrayList<Integer>();
		Cursor c = sqHelper.getThemeCursor();
		if (c.moveToFirst()) {
			int column_name = c.getColumnIndex(SQHelper.KEY_NAME);
			int column_id = c.getColumnIndex(SQHelper.KEY_ROWID);
			do {
				nameList.add(c.getString(column_name));
				idList.add(c.getInt(column_id));
			} while (c.moveToNext());
		}
		c.close();
		ListView themelist = (ListView) findViewById(R.id.statistic_themelist);
		themelist.setAdapter(new ThemeListAdapter(getApplicationContext(),
				R.layout.theme_statistic_list_row, R.id.themeText, nameList, idList));
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

		ArrayList<String> nameList;
		ArrayList<Integer> idList;
		int totalWithoutSleep ;
		int[][] dates = Util.getLastXDatesArray(activityDiagramLength);
		int rank = 1;
		
		public ThemeListAdapter(Context context, int layoutViewResourceId,
				int textViewResourceId, ArrayList<String> nameList, ArrayList<Integer> idList) {
			super(context, layoutViewResourceId, textViewResourceId, nameList);
			this.nameList = nameList;
			this.idList = idList;
			totalWithoutSleep = sqHelper.getTotalDurationWithoutSleep(); 
		}

		@Override
		public View getView(int position, View v, ViewGroup parent) {
			LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.theme_statistic_list_row, null);

			TextView rankText = (TextView) v.findViewById(R.id.rankText);
			rankText.setText(rank + ". ");

			int themeId = idList.get(position);
			int totalDuration = sqHelper.getSum(
					SQHelper.KEY_DURATION, 
					new String[][]{{
						SQHelper.KEY_THEME}}, 
						new int[][]{{
							themeId}});

			TextView infoText = (TextView) v.findViewById(R.id.infoText);
			infoText.setText(prepareInfoText(totalDuration, themeId));
			
			PieChart pieChart = (PieChart) v.findViewById(R.id.pieChart);
			int pomodoroDuration = sqHelper.getSum(
					SQHelper.KEY_DURATION, 
					new String[][]{{
						SQHelper.KEY_TYPE, 
						SQHelper.KEY_THEME}}, 
						new int[][]{{
							0, 
							themeId}});
			int breakDuration = sqHelper.getSum(
					SQHelper.KEY_DURATION,
					new String[][]{{
						SQHelper.KEY_TYPE, 
						SQHelper.KEY_THEME}}, 
						new int[][]{{
							2, 
							themeId}});
			int trackDuration = sqHelper.getSum(
					SQHelper.KEY_DURATION, 
					new String[][]{{
						SQHelper.KEY_TYPE, 
						SQHelper.KEY_THEME}}, 
						new int[][]{{
							3, 
							themeId}});
			
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
			
			
			int[] barValues = new int[activityDiagramLength];
			for (int i = 0; i < activityDiagramLength; i++) {
				barValues[i] = sqHelper.getSum(
						SQHelper.KEY_DURATION, 
						new String[][]{{
					SQHelper.KEY_THEME, 
					SQHelper.KEY_DATE_DAY, 
					SQHelper.KEY_DATE_MONTH, 
					SQHelper.KEY_DATE_YEAR}}, 
					new int[][]{{
						themeId,
						dates[i][0], 
						dates[i][1], 
						dates[i][2]}}); 
			}

			BarChart bars = (BarChart) v.findViewById(R.id.barChart);
			bars.setValues(barValues);
			
			rank++;
			return super.getView(position, v, parent);
		}
	}
}
