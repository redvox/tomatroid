package com.example.tomatroid;

import com.example.tomatroid.digram.DayBarChart;
import com.example.tomatroid.util.NavigationBarManager;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.Menu;
import android.widget.LinearLayout;

public class WeeklyActivity extends Activity {

	final int ACTIVITYNUMBER = 4;
	LayoutInflater inflater;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_weekly);
		
		NavigationBarManager navi = new NavigationBarManager(this,
				ACTIVITYNUMBER);
		
		inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		LinearLayout ll = (LinearLayout) findViewById(R.id.dayBarChart);
		
		DayBarChart dayBarChat = new DayBarChart(this);
		ll.addView(dayBarChat);
	}

//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.weekly, menu);
//		return true;
//	}

}
