package com.example.tomatroid.util;

import android.app.ActionBar;
import android.app.ActionBar.OnNavigationListener;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import com.example.tomatroid.DatabaseList;
import com.example.tomatroid.MainActivity;
import com.example.tomatroid.R;
import com.example.tomatroid.StatisicActivity;
import com.example.tomatroid.ThemeList;
import com.example.tomatroid.WeeklyActivity;

public class NavigationBarManager implements OnNavigationListener {

	boolean fistHit = true;
	Activity activity;
	int activitsnumber;

	public NavigationBarManager(Activity activity, int activitsnumber) {
		this.activity = activity;
		this.activitsnumber = activitsnumber;
		SpinnerAdapter mSpinnerAdapter = ArrayAdapter.createFromResource(
				activity, R.array.action_list, R.layout.choose_theme_row);

		ActionBar actionBar = activity.getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
		actionBar.setListNavigationCallbacks(mSpinnerAdapter, this);
		actionBar.setSelectedNavigationItem(activitsnumber);
	}

	@Override
	public boolean onNavigationItemSelected(int itemPosition, long itemId) {
		if (!fistHit && itemPosition != activitsnumber) {
			Log.e("NaviBar", "Act " + activitsnumber + " Pos:" + itemPosition);
			switch (itemPosition) {
			case 0:
				Intent i1 = new Intent(activity, MainActivity.class);
				i1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				activity.startActivity(i1);
				break;
			case 1:
				Intent i2 = new Intent(activity, StatisicActivity.class);
				i2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				activity.startActivity(i2);
				break;
			case 2:
				Intent i3 = new Intent(activity, ThemeList.class);
				i3.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				activity.startActivity(i3);
				break;
			case 3:
				Intent i4 = new Intent(activity, DatabaseList.class);
				i4.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				activity.startActivity(i4);
				break;
			case 4:
				Intent i5 = new Intent(activity, WeeklyActivity.class);
				i5.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				activity.startActivity(i5);
				break;
			}

		}
		fistHit = false;
		return false;
	}
}