package com.example.tomatroid;

import java.util.ArrayList;

import com.example.tomatroid.sql.SQHelper;
import com.example.tomatroid.util.StoredAnimation;

import android.R.array;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleCursorAdapter;

import android.widget.TextView;
import android.widget.ViewFlipper;

public class ControlListener implements OnClickListener, OnItemClickListener {

	int activeButton = -1;
	MainActivity mA;
	SQHelper sqHelper;

	Button[] bA;
	ArrayList<String> commands = new ArrayList<String>();
	TextView themeText;

	ViewFlipper viewFlipper;
	LinearLayout controlLayout;
	ListView themeListView;
	ArrayList<String> themeList;

	public ControlListener(MainActivity mA, SQHelper sqHelper) {
		this.mA = mA;
		this.sqHelper = sqHelper;
		viewFlipper = (ViewFlipper) mA.findViewById(R.id.viewFlipper);

		controlLayout = (LinearLayout) mA.findViewById(R.id.control);

		TextView textview = new TextView(mA);
		textview.setText("Theme:");
		textview.setTextSize(10);
		controlLayout.addView(textview);

		LayoutInflater mInflater = (LayoutInflater) mA
				.getSystemService(mA.LAYOUT_INFLATER_SERVICE);

		View line1 = mInflater.inflate(R.layout.horizontal_line, controlLayout,
				false);
		controlLayout.addView(line1);

		themeText = new TextView(mA);
		themeText.setText("Hier Thema auswählen");
		themeText.setClickable(true);
		themeText.setOnClickListener(this);
		themeText.setTextSize(20);
		themeText.setTag(99);
		controlLayout.addView(themeText);

		View line2 = mInflater.inflate(R.layout.horizontal_line, controlLayout,
				false);
		controlLayout.addView(line2);

		// Controlls
		commands.add("Pomodoro!");
		commands.add("Pause Kurz!");
		commands.add("Pause Lang!");
		commands.add("Tracking!");
		commands.add("Sleeping!");

		RelativeLayout.LayoutParams relativeParams = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		relativeParams.addRule(RelativeLayout.CENTER_VERTICAL);

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

			rL.addView(iV, relativeParams);
			rL.addView(bA[i]);

			controlLayout.addView(rL);
		}

		// Theme List
		themeListView = (ListView) mA.findViewById(R.id.themeList);
		themeList = new ArrayList<String>();
		Cursor c = sqHelper.getAllThemes();
		if (c.moveToFirst()) {
			do {
				themeList.add(c.getString(0));
				c.moveToNext();
			} while (c.moveToNext());
		}
		c.close();
		// themeListView.setAdapter(new ArrayAdapter<String>(mA,
		// R.layout.theme_list_row, themeList));
		themeListView.setAdapter(new SimpleCursorAdapter(mA,
				R.layout.theme_list_row, sqHelper.getAllThemes(),
				new String[] { SQHelper.KEY_NAME }, new int[] { R.id.name }));
		themeListView.setOnItemClickListener(this);
	}

	@Override
	public void onClick(View v) {
		int tag = (Integer) v.getTag();
		if (tag == 99) {
			viewFlipper.showNext();
		} else {
			start(tag);
		}
	}

	@Override
	public void onItemClick(AdapterView adapterView, View view, int position,
			long arg3) {
		viewFlipper.showNext();
		// Object o = view;
		// Cursor cursor = ((SimpleCursorAdapter) o).getCursor();
		// cursor.moveToPosition(position);
		// String theme = cursor.getString(cursor
		// .getColumnIndex(SQHelper.KEY_NAME));
		String theme = (String) ((TextView) view).getText();
		themeText.setText(theme);
	}

	public void start(int tag) {
		if (activeButton != -1) {
			// Stop Other or Own
			mA.end(activeButton);
			bA[activeButton]
					.startAnimation(StoredAnimation.slideHorizontal(55));
			bA[activeButton].setTranslationX(0);
		}

		if (activeButton != tag) {
			bA[tag].startAnimation(StoredAnimation.slideHorizontal(-55));
			bA[tag].setTranslationX(55);
			// Start Own
			mA.start(tag);
			activeButton = tag;
		} else {
			activeButton = -1;
		}
	}

	public void restart() {
		mA.end(activeButton);
		mA.start(activeButton);
	}

	public void stop() {
		if (activeButton != -1) {
			bA[activeButton]
					.startAnimation(StoredAnimation.slideHorizontal(50));
			bA[activeButton].setTranslationX(0);
			mA.stop();
			activeButton = -1;
		}
	}

	public void toogle(int i) {
		activeButton = i;
//		bA[tag].startAnimation(StoredAnimation.slideHorizontal(-55));
		bA[activeButton].setTranslationX(55);
	}
}
