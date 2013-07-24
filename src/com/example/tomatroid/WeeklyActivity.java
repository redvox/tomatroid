package com.example.tomatroid;

import com.example.tomatroid.digram.DayBarChart;
import com.example.tomatroid.sql.SQHelper;
import com.example.tomatroid.util.NavigationBarManager;
import com.example.tomatroid.util.Util;

import android.os.Bundle;
import android.util.Log;
import android.app.Activity;
import android.database.Cursor;

public class WeeklyActivity extends Activity {

	final int ACTIVITYNUMBER = 4;
	int days = 14;
	int maxValue = 1440;
	String[] weekday_shortcut;
	DayBarChart dayBarChart;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Util.switchToNightMode(this);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_weekly);

		new NavigationBarManager(this, ACTIVITYNUMBER);

		weekday_shortcut = getResources().getStringArray(R.array.weekday_shortcut);
		
		SQHelper sqh = new SQHelper(this);
		
		int[][][] barValues = new int[days][][];
		String[] columnLables = new String[days];
		int[][] dates = Util.getLastXDatesArray(days+1);
		
		Cursor previusDay = sqh.getCursorForDay(
				dates[0][0], 
				dates[0][1],
				dates[0][2]);
		
		int column_type = previusDay.getColumnIndex(SQHelper.KEY_TYPE);
		int column_startHour = previusDay.getColumnIndex(SQHelper.KEY_DATE_START_HOUR);
		int column_startMinute = previusDay.getColumnIndex(SQHelper.KEY_DATE_START_MINUTE);
		int column_endHour = previusDay.getColumnIndex(SQHelper.KEY_DATE_END_HOUR);
		int column_endMinute = previusDay.getColumnIndex(SQHelper.KEY_DATE_END_MINUTE);
		int column_weekday = previusDay.getColumnIndex(SQHelper.KEY_DATE_WEEKDAY);
		
		for(int d = 0; d < days; d++){
			int today = d+1; 
			Cursor todayCursor = sqh.getCursorForDay(
					dates[today][0], 
					dates[today][1],
					dates[today][2]);
			
			// Adding previus Day
			int i = 0;
			if(previusDay.moveToLast()){
				if(previusDay.getInt(column_startHour) > previusDay.getInt(column_endHour)){
					barValues[d] = new int[todayCursor.getCount()+1][4];
					
					barValues[d][0][0] = previusDay.getInt(column_type);
					barValues[d][0][1] = 0;
					barValues[d][0][2] = (previusDay.getInt(column_endHour) * 60) + previusDay.getInt(column_endMinute);
					
					i++;
				} else {
					barValues[d] = new int[todayCursor.getCount()][4];
				}
			} else {
				barValues[d] = new int[todayCursor.getCount()][4];
			}
			
			// Column Lable
			columnLables[d] = weekday_shortcut[dates[today][3]-1]+" "+dates[today][0];
			
			// Adding Today
			if (todayCursor.moveToFirst()) {
				do {
					barValues[d][i][0] = todayCursor.getInt(column_type);
					barValues[d][i][1] = (todayCursor.getInt(column_startHour) * 60) + todayCursor.getInt(column_startMinute);
					barValues[d][i][2] = (todayCursor.getInt(column_endHour) * 60) + todayCursor.getInt(column_endMinute);
					if(barValues[d][i][1] > barValues[d][i][2])
						barValues[d][i][2] = maxValue;
					
					barValues[d][0][3] = todayCursor.getInt(column_weekday);
					
					i++;
				} while (todayCursor.moveToNext());
			}
			
			previusDay = todayCursor;
		}
		
		dayBarChart = (DayBarChart) findViewById(R.id.dayBarChart);
		dayBarChart.setValues(barValues, columnLables);
		dayBarChart.setOnTouchListener(dayBarChart);
	}

	@Override
	protected void onResume() {
		dayBarChart.refreshNowLine();
		super.onResume();
	}
	
	// @Override
	// public boolean onCreateOptionsMenu(Menu menu) {
	// // Inflate the menu; this adds items to the action bar if it is present.
	// getMenuInflater().inflate(R.menu.weekly, menu);
	// return true;
	// }

}
