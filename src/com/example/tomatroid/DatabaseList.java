package com.example.tomatroid;

import com.example.tomatroid.sql.SQHelper;

import android.os.Bundle;
import android.app.Activity;
import android.app.ListActivity;
import android.database.Cursor;
import android.util.Log;
import android.view.Menu;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class DatabaseList extends ListActivity {

	SQHelper sqHelper = new SQHelper(this);

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_database_list);

		// ListView list = (ListView) findViewById(R.id.databaselist);

		// THE DESIRED COLUMNS TO BE BOUND
		String[] columns = new String[] { SQHelper.KEY_DATE_DAY,
				SQHelper.KEY_DATE_MONTH, SQHelper.KEY_DATE_YEAR,
				SQHelper.KEY_DURATION, SQHelper.KEY_TYPE, SQHelper.KEY_THEME };
		// THE XML DEFINED VIEWS WHICH THE DATA WILL BE BOUND TO
		int[] to = new int[] { R.id.day_entry, R.id.month_entry,
				R.id.year_entry, R.id.duration_entry, R.id.type_entry,
				R.id.theme_entry };

		Cursor c = sqHelper.getDatesCursor();
		Log.e("DatabaseList", "Count " + c.getCount());

		SimpleCursorAdapter adapter = new SimpleCursorAdapter(
				getApplicationContext(), R.layout.database_list_row, c,
				columns, to, SimpleCursorAdapter.NO_SELECTION);
		setListAdapter(adapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_database_list, menu);
		return true;
	}
}
