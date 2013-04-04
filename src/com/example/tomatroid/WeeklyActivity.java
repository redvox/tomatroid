package com.example.tomatroid;

import com.example.tomatroid.digram.DayBarChart;
import com.example.tomatroid.sql.SQHelper;
import com.example.tomatroid.util.NavigationBarManager;
import com.example.tomatroid.util.Util;

import android.os.Bundle;
import android.app.Activity;
import android.database.Cursor;

public class WeeklyActivity extends Activity {

	final int ACTIVITYNUMBER = 4;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_weekly);

		NavigationBarManager navi = new NavigationBarManager(this,
				ACTIVITYNUMBER);

		DayBarChart dayBarChart = (DayBarChart) findViewById(R.id.dayBarChart);
		dayBarChart.setValues(getValuesFromDatabase(7));
	}

	// @Override
	// public boolean onCreateOptionsMenu(Menu menu) {
	// // Inflate the menu; this adds items to the action bar if it is present.
	// getMenuInflater().inflate(R.menu.weekly, menu);
	// return true;
	// }

	public int[][][] getValuesFromDatabase(int days) {
		SQHelper sqh = new SQHelper(this);
		
		int[][][] barValues = new int[days][][];
		int[][] dates = Util.getLastXDatesArray(days+1);
		
		Cursor previusDay = sqh.getCursorForDay(
				dates[0][0], 
				dates[0][1],
				dates[0][2]);
		
		for(int d = 0; d < days; d++){
			Cursor today = sqh.getCursorForDay(
					dates[d+1][0], 
					dates[d+1][1],
					dates[d+1][2]);
			
			int column_type = today.getColumnIndex(SQHelper.KEY_TYPE);
			int column_startHour = today.getColumnIndex(SQHelper.KEY_DATE_START_HOUR);
			int column_startMinute = today.getColumnIndex(SQHelper.KEY_DATE_START_MINUTE);
			int column_endHour = today.getColumnIndex(SQHelper.KEY_DATE_END_HOUR);
			int column_endMinute = today.getColumnIndex(SQHelper.KEY_DATE_END_MINUTE);
			
			//Adding previus Day
			int i = 0;
			if(previusDay.moveToLast()){
				if(previusDay.getInt(column_startHour) > previusDay.getInt(column_endHour)){
					barValues[d] = new int[today.getCount()+1][3];
					
					barValues[d][0][0] = previusDay.getInt(column_type);
					barValues[d][0][1] = 0;
					barValues[d][0][2] = (previusDay.getInt(column_endHour) * 60) + previusDay.getInt(column_endMinute);
					
					i++;
				} else {
					barValues[d] = new int[today.getCount()][3];
				}
			} else {
				barValues[d] = new int[today.getCount()][3];
			}
			
			//Adding Today
			if (today.moveToFirst()) {
				do {
					barValues[d][i][0] = today.getInt(column_type);
					barValues[d][i][1] = (today.getInt(column_startHour) * 60) + today.getInt(column_startMinute);
					barValues[d][i][2] = (today.getInt(column_endHour) * 60) + today.getInt(column_endMinute);
					if(barValues[d][i][1] > barValues[d][i][2])
						barValues[d][i][2] = 1440;
					i++;
				} while (today.moveToNext());
			}
			
			previusDay = today;
		}
		
		return barValues;
	}

}
