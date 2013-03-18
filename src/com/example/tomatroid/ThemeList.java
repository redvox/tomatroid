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

public class ThemeList extends ListActivity {

	SQHelper sqHelper = new SQHelper(this);

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_database_list);
		
		Cursor c = sqHelper.getThemeCursor();
		
		setListAdapter(new ThemeCursorAdapter(this, c, 0));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_theme_list, menu);
		return true;
	}

	class ThemeCursorAdapter extends CursorAdapter {

		LayoutInflater inflater;
		int name;
		int itemof;

		public ThemeCursorAdapter(Context context, Cursor c, int flags) {
			super(context, c, flags);
			inflater = LayoutInflater.from(context);
			name = c.getColumnIndex(SQHelper.KEY_NAME);
			itemof = c.getColumnIndex(SQHelper.KEY_ITEMOF);
		}

		@Override
		public void bindView(View view, Context context, Cursor c) {
			TextView tv1 = (TextView) view.findViewById(R.id.parent);
			TextView tv2 = (TextView) view.findViewById(R.id.name);

			tv1.setText(sqHelper.getTheme(c.getInt(itemof)));
			tv2.setText(c.getString(name));
		}

		@Override
		public View newView(Context context, Cursor cursor, ViewGroup parent) {
			return inflater.inflate(R.layout.theme_list_row, parent, false);
		}

	}
}
