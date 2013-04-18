package com.example.tomatroid;

import java.util.ArrayList;

import com.example.tomatroid.sql.SQHelper;
import com.example.tomatroid.util.StoredAnimation;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
	ArrayList<String> commands = new ArrayList<String>();
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
		View line1 = mInflater.inflate(R.layout.horizontal_line, controlLayout,
				false);
		controlLayout.addView(line1);

		themePomodoroText = new TextView(mA);
		themePomodoroText.setClickable(true);
		themePomodoroText.setOnClickListener(this);
		themePomodoroText.setTextSize(20);
		themePomodoroText.setTag(90);
		themePomodoroText.setBackgroundColor(backgroundColor);
		themePomodoroText.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
//		controlLayout.addView(themePomodoroText);
		
		RelativeLayout rL1 = new RelativeLayout(mA);
		rL1.setBackgroundColor(backgroundColor);
		ImageView iV1 = new ImageView(rL1.getContext());
		iV1.setBackgroundResource(android.R.drawable.ic_media_play);
		rL1.addView(iV1, relativeLayoutForTheme);
		rL1.addView(themePomodoroText);
		controlLayout.addView(rL1);
		//######################################
		View line2 = mInflater.inflate(R.layout.horizontal_line, controlLayout,
				false);
		controlLayout.addView(line2);

		themeBreakText = new TextView(mA);
		themeBreakText.setClickable(true);
		themeBreakText.setOnClickListener(this);
		themeBreakText.setTextSize(20);
		themeBreakText.setTag(91);
		themeBreakText.setBackgroundColor(backgroundColor);
		themeBreakText.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
//		controlLayout.addView(themeBreakText);

		RelativeLayout rL2 = new RelativeLayout(mA);
		rL2.setBackgroundColor(backgroundColor);
		ImageView iV2 = new ImageView(rL1.getContext());
		iV2.setBackgroundResource(android.R.drawable.ic_media_play);
		rL2.addView(iV2, relativeLayoutForTheme);
		rL2.addView(themeBreakText);
		controlLayout.addView(rL2);
		
		//######################################
		View line3 = mInflater.inflate(R.layout.horizontal_line, controlLayout,
				false);
		controlLayout.addView(line3);

		RelativeLayout.LayoutParams relativeLayoutForButtons = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		relativeLayoutForButtons.addRule(RelativeLayout.CENTER_VERTICAL);
		
		// Controlls
		commands.add(mA.getString(R.string.pomodoro));
		commands.add(mA.getString(R.string.shortbreak));
		commands.add(mA.getString(R.string.longbreak));
		commands.add(mA.getString(R.string.tracking));
		commands.add(mA.getString(R.string.sleep));

		bA = new Button[commands.size()];
		for (int i = 0; i < bA.length; i++) {
			RelativeLayout rL = new RelativeLayout(mA);
			ImageView iV = new ImageView(rL.getContext());
			iV.setBackgroundResource(android.R.drawable.ic_media_play);
			iV.setMaxWidth(50);

			bA[i] = new Button(mA);
			bA[i].setText(commands.get(i));
			bA[i].setTag(i);
			bA[i].setOnClickListener(this);
			bA[i].setBackgroundColor(backgroundColor);

			rL.addView(iV, relativeLayoutForButtons);
			rL.addView(bA[i]);

			controlLayout.addView(rL);
		}

		// Theme List
		themeListAdapter = new SimpleCursorAdapter(mA,
				R.layout.choose_theme_row, sqHelper.getThemeCursor(0),
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
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mA);
		alertDialogBuilder.setView(dialogView);

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
