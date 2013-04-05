package com.example.tomatroid;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;

public class SettingsActivity extends Activity {
	
	LayoutInflater inflater;
	SharedPreferences settings;
	
	ArrayList<Object> input = new ArrayList<Object>();
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);

		ScrollView scroll = (ScrollView) findViewById(R.id.scroll);
		LinearLayout options = (LinearLayout) scroll.findViewById(R.id.options);
		inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		settings = getSharedPreferences(MainActivity.PREFS_NAME, 0);
		options.addView(addEditTextOption(getString(R.string.pomodoro_length), getString(R.string.pomodoro_length_description), ""+settings.getInt(MainActivity.KEY_POMODOROTIME, 25)));
		options.addView(addEditTextOption(getString(R.string.shortbreak_length), getString(R.string.shortbreak_length_description), ""+settings.getInt(MainActivity.KEY_SHORTBREAKTIME, 5)));
		options.addView(addEditTextOption(getString(R.string.longbreak_length), getString(R.string.longbreak_length_description), ""+settings.getInt(MainActivity.KEY_LONGBREAKTIME, 35)));
		options.addView(addEditTextOption(getString(R.string.pomodoro_until_break), getString(R.string.pomodoro_until_break_description), ""+settings.getInt(MainActivity.KEY_POMODORO_UNTIL_BREAK, 4)));
		options.addView(addEditTextOption(getString(R.string.rememberTime_length), getString(R.string.rememberTime_length_description), ""+settings.getInt(MainActivity.KEY_REMEMBERTIME, 10)));
		
		options.addView(addCheckboxOption(getString(R.string.vibrate), getString(R.string.vibrate_description), settings.getBoolean(MainActivity.KEY_VIBRATE, true)));
		options.addView(addCheckboxOption(getString(R.string.sound), getString(R.string.sound_description), settings.getBoolean(MainActivity.KEY_PLAYSOUND, true)));
		options.addView(addCheckboxOption(getString(R.string.airplanemode), getString(R.string.airplanemode_description), settings.getBoolean(MainActivity.KEY_AIRAIRPLANEMODE, false)));
	}
	
	private View addEditTextOption(String name, String description, String defaultText){
		View v = inflater.inflate(R.layout.settings_row_edittext, null);
		((TextView) v.findViewById(R.id.name)).setText(name);
		((TextView) v.findViewById(R.id.description)).setText(description);
		EditText e = (EditText) v.findViewById(R.id.editText);
		e.setText(defaultText);
		input.add(e);
		return v;
	}
	
	private View addCheckboxOption(String name, String description, boolean defaultState){
		View v = inflater.inflate(R.layout.settings_row_checkbox, null);
		((TextView) v.findViewById(R.id.name)).setText(name);
		((TextView) v.findViewById(R.id.description)).setText(description);
		CheckBox c = (CheckBox) v.findViewById(R.id.checkbox);
		c.setChecked(defaultState);
		input.add(c);
		return v;
	}
	
	public void save(View view){
		boolean b = true;
		try {
			SharedPreferences.Editor editor = settings.edit();
			editor.putInt(MainActivity.KEY_POMODOROTIME, Integer.parseInt(((EditText) input.get(0)).getText().toString()));
			editor.putInt(MainActivity.KEY_SHORTBREAKTIME, Integer.parseInt(((EditText) input.get(1)).getText().toString()));
			editor.putInt(MainActivity.KEY_LONGBREAKTIME, Integer.parseInt(((EditText) input.get(2)).getText().toString()));
			editor.putInt(MainActivity.KEY_POMODORO_UNTIL_BREAK, Integer.parseInt(((EditText) input.get(3)).getText().toString()));
			editor.putInt(MainActivity.KEY_REMEMBERTIME, Integer.parseInt(((EditText) input.get(4)).getText().toString()));
			editor.putBoolean(MainActivity.KEY_VIBRATE, ( (CheckBox) input.get(5) ).isChecked()  );
			editor.putBoolean(MainActivity.KEY_PLAYSOUND, ( (CheckBox) input.get(6) ).isChecked()  );
			editor.putBoolean(MainActivity.KEY_AIRAIRPLANEMODE, ( (CheckBox) input.get(7) ).isChecked()  );
			editor.commit();
			finish();
		} catch (Exception e) {
			e.printStackTrace();
			b = false;
			
		}
		if(b){
			Toast.makeText(getApplicationContext(), getString(R.string.dataSaves), Toast.LENGTH_LONG).show();
		} else {
			Toast.makeText(getApplicationContext(), getString(R.string.could_not_save_description), Toast.LENGTH_LONG).show();
		}
	}
	
	public void back(View view){
		finish();
	}
}
