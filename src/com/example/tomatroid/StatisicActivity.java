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
import android.content.SharedPreferences;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
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
	ThemeListAdapter themeListAdapter;
	ListView themelist;
	boolean withChrilden;
	
	final static String KEY_WITHCHILDREN = "withchildren";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		Util.switchToNightMode(this);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_statictic);
		new NavigationBarManager(this, ACTIVITYNUMBER);

		SharedPreferences settings = getSharedPreferences(MainActivity.PREFS_NAME, 0);
		withChrilden = settings.getBoolean(KEY_WITHCHILDREN, true);
		
		Log.e("Statistic Activirty", "checkbox state "+withChrilden);
		
		CheckBox cb = (CheckBox) findViewById(R.id.checkbox);
		cb.setChecked(withChrilden);
		
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
		pomodoroInfo.setText(prepareInfoText(totalPomodoro, SQHelper.KEY_TYPE, SQHelper.TYPE_POMODORO));

		// Long Break Overview
		TextView breakInfo = (TextView) findViewById(R.id.statistic_breakInfo);
		breakInfo.setText(prepareInfoText(totalBreak, SQHelper.KEY_TYPE, SQHelper.TYPE_LONGBREAK));

//		PieChart breakPieChart = (PieChart) findViewById(R.id.breakPieChart);
//		breakPieChart.setValues(new float[] { 0 });

		// Sleep Overview;
		TextView sleepInfo = (TextView) findViewById(R.id.statistic_sleepInfo);
		sleepInfo.setText(prepareInfoText(totalTracking, SQHelper.KEY_TYPE, SQHelper.TYPE_SLEEPING));

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
		createThemeListAdapter();
	}

	@Override
	protected void onResume() {
		super.onResume();
		getActionBar().setSelectedNavigationItem(ACTIVITYNUMBER);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		SharedPreferences settings = getSharedPreferences(MainActivity.PREFS_NAME, 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putBoolean(KEY_WITHCHILDREN, withChrilden);
		editor.commit();
	}
	
	public void createThemeListAdapter(){
		ArrayList<String> nameList = new ArrayList<String>();
		ArrayList<Integer> idList = new ArrayList<Integer>();
		Cursor c = sqHelper.getThemeCursor(0, withChrilden);
		if (c.moveToFirst()) {
			int column_name = c.getColumnIndex(SQHelper.KEY_NAME);
			int column_id = c.getColumnIndex(SQHelper.KEY_ROWID);
			do {
				nameList.add(c.getString(column_name));
				idList.add(c.getInt(column_id));
			} while (c.moveToNext());
		}
		c.close();
		
		themeListAdapter = new ThemeListAdapter(getApplicationContext(),
				R.layout.theme_statistic_list_row, R.id.themeText, nameList, idList);
		
		themelist = (ListView) findViewById(R.id.statistic_themelist);
		themelist.setAdapter(themeListAdapter);
	}

	public String prepareInfoText(int totalDuration, String key, int value) {
		int daysAmount = sqHelper.getDaysCount(
				new String[][]{{
					key}}, 
					new int[][]{{
						value}});
		
		int average = 0;
		if(daysAmount > 0){
			average = totalDuration / daysAmount;
		}
		
		if (totalDuration != 0) {
			return Util.generateTimeText(totalDuration) + "\navg "
					+ Util.generateTimeText(average) + "\n"
					+ "over "+ daysAmount + "d";
		} else {
			return "nothing recorded yet";
		}
	}
	
	public void onCheckboxClicked(View view) {
//	    boolean checked = ((CheckBox) view).isChecked();
		withChrilden = !withChrilden;
		createThemeListAdapter();
//		themeListAdapter.notifyDataSetChanged();
//		themelist.invalidate();
	}

	class ThemeListAdapter extends ArrayAdapter<String> {

		ArrayList<String> nameList;
		ArrayList<Integer> idList;
		int totalWithoutSleep ;
		int[][] dates = Util.getLastXDatesArray(activityDiagramLength);
		
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
			rankText.setText((position+1) + ". ");

			int themeId = idList.get(position);
			
			ArrayList<Integer> iDs = new ArrayList<Integer>();
			iDs.add(themeId);
			if(withChrilden)
				iDs.addAll(sqHelper.getAllThemeChrildren(themeId));
			
			String[][] what1 = new String[iDs.size()][1];
			int[][] who1 = new int[iDs.size()][1];
			
			int n = 0;
			for(Integer k : iDs){
				what1[n][0] = SQHelper.KEY_THEME;
				who1[n][0] = k;
				n++;
			}
			
			int totalDuration = sqHelper.getSum(
					SQHelper.KEY_DURATION, 
					what1, 
					who1);

			TextView infoText = (TextView) v.findViewById(R.id.infoText);
			infoText.setText(prepareInfoText(totalDuration, SQHelper.KEY_THEME, themeId));
			
			PieChart pieChart = (PieChart) v.findViewById(R.id.pieChart);
			
			String[][] what2 = new String[iDs.size()][2];
			int[][] who2 = new int[iDs.size()][2];
			
			n = 0;
			for(Integer k : iDs){
				what2[n][0] = SQHelper.KEY_TYPE;
				what2[n][1] = SQHelper.KEY_THEME;
				who2[n][0] = 0;
				who2[n][1] = k;
				n++;
			}
			int pomodoroDuration = sqHelper.getSum(
					SQHelper.KEY_DURATION, 
					what2,who2);
			
			n = 0;
			for(Integer k : iDs){
				who2[n][0] = 2;
				n++;
			}
			int breakDuration = sqHelper.getSum(
					SQHelper.KEY_DURATION,
					what2,who2);
			
			n = 0;
			for(Integer k : iDs){
				who2[n][0] = 3;
				n++;
			}
			
			int trackDuration = sqHelper.getSum(
					SQHelper.KEY_DURATION, 
					what2,who2);
			
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
			
			return super.getView(position, v, parent);
		}
	}
}
