package com.example.tomatroid;

import java.util.ArrayList;

import com.example.tomatroid.sql.SQHelper;
import com.example.tomatroid.util.StoredAnimation;
import com.example.tomatroid.util.Util;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Color;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;

import android.widget.TextView;
import android.widget.ViewFlipper;

public class ControlListener implements OnClickListener, OnItemClickListener {

	int activeButton = -1;
	MainActivity mA;
	SQHelper sqHelper;
	LayoutInflater mInflater;

	Button[] bA;
	TextView themePomodoroText;
	TextView themeBreakText;
	Button newTheme;
	int chooseThemeSwitch;

	ViewFlipper viewFlipper;
	LinearLayout controlLayout;
	ListView themeListView;
	SimpleCursorAdapter themeListAdapter;
	ArrayList<String> themeList;

	int backgroundColor = Color.WHITE;
	
	public ControlListener(MainActivity mA, SQHelper sqHelper) {
		this.mA = mA;
		this.sqHelper = sqHelper;
		viewFlipper = (ViewFlipper) mA.findViewById(R.id.viewFlipper);
		newTheme = (Button) mA.findViewById(R.id.newtheme);
		newTheme.setTag(999);
		newTheme.setOnClickListener(this);

		controlLayout = (LinearLayout) mA.findViewById(R.id.control);

		mInflater = (LayoutInflater) mA
				.getSystemService(mA.LAYOUT_INFLATER_SERVICE);
		
		
		RelativeLayout.LayoutParams relativeLayoutForTheme = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		relativeLayoutForTheme.addRule(RelativeLayout.CENTER_VERTICAL);
		relativeLayoutForTheme.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		//######################################

		themePomodoroText = (TextView) mA.findViewById(R.id.themePomodoroText);
		themePomodoroText.setClickable(true);
		themePomodoroText.setOnClickListener(this);
		themePomodoroText.setTextSize(20);
		themePomodoroText.setTag(90);
		themePomodoroText.setBackgroundColor(backgroundColor);

		//######################################

		themeBreakText = (TextView) mA.findViewById(R.id.themeBreakText);
		themeBreakText.setClickable(true);
		themeBreakText.setOnClickListener(this);
		themeBreakText.setTextSize(20);
		themeBreakText.setTag(91);

		//######################################

		bA = new Button[5];
		bA[0] = (Button) mA.findViewById(R.id.pomodoroButton);
		bA[1] = (Button) mA.findViewById(R.id.shortbreakButton);
		bA[2] = (Button) mA.findViewById(R.id.longbreakButton);
		bA[3] = (Button) mA.findViewById(R.id.trackingButton);
		bA[4] = (Button) mA.findViewById(R.id.sleepButton);
				
		for (int i = 0; i < bA.length; i++) {
			bA[i].setTag(i);
			bA[i].setOnClickListener(this);
		}

		// Theme List
		themeListAdapter = new SimpleCursorAdapter(mA,
				R.layout.choose_theme_row, sqHelper.getThemeCursor(0, false),
				new String[] { SQHelper.KEY_NAME }, new int[] { R.id.name }, 0);
		themeListView = (ListView) mA.findViewById(R.id.themeList);
		themeListView.setAdapter(themeListAdapter);
		themeListView.setOnItemClickListener(this);
	}

	@Override
	public void onClick(View v) {
		int tag = (Integer) v.getTag();
		if (tag == 90) {
			chooseThemeSwitch = 0;
			viewFlipper.showNext();
		} else if (tag == 91) {
			chooseThemeSwitch = 1;
			viewFlipper.showNext();
		} else if (tag == 999) {
			showNewThemeDialog();
		} else {
			if(Util.DEBUG){
				Util.writeLog(mA, "Button pressed (Tag: "+tag+")");
			}
			
			start(tag);
		}
	}

	@Override
	public void onItemClick(AdapterView adapterView, View view, int position,
			long arg3) {
		viewFlipper.showNext();
		String theme = (String) ((TextView) view).getText();
		switch (chooseThemeSwitch) {
		case 0:
			themePomodoroText.setText(theme);
			mA.pomodoroTheme = theme;
			break;
		case 1:
			themeBreakText.setText(theme);
			mA.breakTheme = theme;
			break;
		}
	}

	public void start(int tag) {
		if (activeButton != -1) {
			// Stop Other or Own
			mA.end(activeButton);
			slideLeft(bA[activeButton]);
			
			if(activeButton == MainActivity.TYPE_POMODORO){
				slideLeft(themePomodoroText);
			} else if(activeButton == MainActivity.TYPE_LONGBREAK || activeButton == MainActivity.TYPE_TRACKING){
				slideLeft(themeBreakText);
			}
		}

		if (activeButton != tag) {
			slideRight(bA[tag]);
			if(tag == MainActivity.TYPE_POMODORO){
				slideRight(themePomodoroText);
			} else if( tag == MainActivity.TYPE_LONGBREAK || tag == MainActivity.TYPE_TRACKING){
				slideRight(themeBreakText);
			}
			
			// Start Own
			mA.start(tag);
			activeButton = tag;
		} else {
			activeButton = -1;
		}
	}
	
	public void end(int tag) {
		if (activeButton != -1) {
			slideLeft(bA[activeButton]);
			if(activeButton == MainActivity.TYPE_POMODORO){
				slideLeft(themePomodoroText);
			} else if( activeButton == MainActivity.TYPE_LONGBREAK || activeButton == MainActivity.TYPE_TRACKING){
				slideLeft(themeBreakText);
			}
			
			mA.end(activeButton);
			activeButton = -1;
		}
	}
	
	public void slideRight(View view){
		view.startAnimation(StoredAnimation.slideHorizontal(-55));
		view.setTranslationX(55);
	}
	
	public void slideLeft(View view){
		view.startAnimation(StoredAnimation.slideHorizontal(55));
		view.setTranslationX(0);
	}

	public void restart() {
		mA.end(activeButton);
		mA.start(activeButton);
	}

	public void stop() {
		if (activeButton != -1) {
			slideLeft(bA[activeButton]);
			if(activeButton == MainActivity.TYPE_POMODORO){
				slideLeft(themePomodoroText);
			} else if( activeButton == MainActivity.TYPE_LONGBREAK || activeButton == MainActivity.TYPE_TRACKING){
				slideLeft(themeBreakText);
			}
			
			mA.stop();
			activeButton = -1;
		}
	}

	public void toogle(int i) {
		if(i != -1){
			activeButton = i;
			bA[activeButton].setTranslationX(55);
			if(activeButton == MainActivity.TYPE_POMODORO){
				themePomodoroText.setTranslationX(55);
			} else if( activeButton == MainActivity.TYPE_LONGBREAK || activeButton == MainActivity.TYPE_TRACKING){
				themeBreakText.setTranslationX(55);
			}
		}
	}

	public void showNewThemeDialog() {
		View dialogView = mInflater.inflate(R.layout.dialog_newtheme, null);
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(new ContextThemeWrapper(mA, R.style.myDialog));
		alertDialogBuilder.setView(dialogView);
		alertDialogBuilder.setTitle(mA.getString(R.string.new_theme));

		final EditText userInput = (EditText) dialogView
				.findViewById(R.id.editTextDialogUserInput);

		final Spinner parentSpinner = (Spinner) dialogView.findViewById(R.id.parentspinner);
		parentSpinner.setAdapter(themeListAdapter);
		
		// set dialog message
		alertDialogBuilder
				.setPositiveButton(mA.getString(R.string.insert), new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						Cursor cc = (Cursor) parentSpinner.getSelectedItem();
						int parentId = cc.getInt(cc.getColumnIndex(SQHelper.KEY_ROWID));
						if(parentId == 1) 
							parentId = -1;
						sqHelper.addTheme(userInput.getText().toString(), parentId);
						themeListAdapter.getCursor().requery();
						themeListAdapter.notifyDataSetChanged();
					}
				})
				.setNegativeButton(mA.getString(R.string.cancel), new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				});

		// create alert dialog
		AlertDialog alertDialog = alertDialogBuilder.create();

		// show it
		alertDialog.show();
	}
}
