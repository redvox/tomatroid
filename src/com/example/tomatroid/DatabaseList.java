package com.example.tomatroid;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.example.tomatroid.sql.SQHelper;

import android.os.Bundle;
import android.app.ListActivity;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class DatabaseList extends ListActivity {

	SQHelper sqHelper = new SQHelper(this);

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_database_list);

		// ListView list = (ListView) findViewById(R.id.databaselist);

		// THE DESIRED COLUMNS TO BE BOUND
		// String[] columns = new String[] { SQHelper.KEY_DATE_DAY,
		// SQHelper.KEY_DATE_MONTH, SQHelper.KEY_DATE_YEAR,
		// SQHelper.KEY_DURATION, SQHelper.KEY_TYPE, SQHelper.KEY_THEME };
		// // THE XML DEFINED VIEWS WHICH THE DATA WILL BE BOUND TO
		// int[] to = new int[] { R.id.day_entry, R.id.month_entry,
		// R.id.year_entry, R.id.duration_entry, R.id.type_entry,
		// R.id.theme_entry };

		Cursor c = sqHelper.getDatesCursor();
		Log.e("DatabaseList", "Count " + c.getCount());

		// SimpleCursorAdapter adapter = new SimpleCursorAdapter(
		// getApplicationContext(), R.layout.database_list_row, c,
		// columns, to, SimpleCursorAdapter.NO_SELECTION);
		// setListAdapter(adapter);
		setListAdapter(new DatabaseCursorAdapter(this, c, 0));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_database_list, menu);
		return true;
	}

	class DatabaseCursorAdapter extends CursorAdapter {

		LayoutInflater inflater;
		int timeStart;
		int timeEnd;
		int theme;
		int type;
		int duration;
		DateTimeFormatter fmt = DateTimeFormat.forPattern("dd.MM.yy - HH:mm:ss");

		public DatabaseCursorAdapter(Context context, Cursor c, int flags) {
			super(context, c, flags);
			inflater = LayoutInflater.from(context);
			timeStart = c.getColumnIndex(SQHelper.KEY_STARTTIMEMILLIES);
			timeEnd = c.getColumnIndex(SQHelper.KEY_ENDTIMEMILLIES);
			theme = c.getColumnIndex(SQHelper.KEY_THEME);
			type = c.getColumnIndex(SQHelper.KEY_TYPE);
			duration = c.getColumnIndex(SQHelper.KEY_DURATION);
		}

		@Override
		public void bindView(View view, Context context, Cursor c) {
			ImageView image = (ImageView) view.findViewById(R.id.typeIcon);
			switch (c.getInt(type)) {
			case SQHelper.TYPE_POMODORO:
				image.setImageResource(android.R.drawable.ic_dialog_info);
				break;
			case SQHelper.TYPE_LONGBREAK:
				image.setImageResource(android.R.drawable.ic_media_ff);
				break;
			case SQHelper.TYPE_SHORTBREAK:
				image.setImageResource(android.R.drawable.ic_delete);
				break;
			case SQHelper.TYPE_SLEEPING:
				image.setImageResource(android.R.drawable.ic_lock_idle_low_battery);
				break;
			case SQHelper.TYPE_TRACKING:
				image.setImageResource(android.R.drawable.ic_input_add);
				break;
			}

			TextView tv1 = (TextView) view.findViewById(R.id.time);
			TextView tv2 = (TextView) view.findViewById(R.id.theme);

			long startVal = Long.parseLong(c.getString(timeStart));
			DateTime start = new DateTime(startVal);

			Log.e("DatabaseAdapter", "Milliseconds: "+startVal);
			tv1.setText(start.toString(fmt));
			tv1.append("   (" + c.getInt(duration) +") min");
			tv2.setText(sqHelper.getTheme(c.getInt(theme)));
		}

		@Override
		public View newView(Context context, Cursor cursor, ViewGroup parent) {
			return inflater.inflate(R.layout.database_list_row, parent, false);
		}

	}
}
