package com.example.tomatroid;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.example.tomatroid.sql.SQHelper;
import com.example.tomatroid.util.NavigationBarManager;

import android.os.Bundle;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;

public class DatabaseList extends ListActivity {

	final int ACTIVITYNUMBER = 3; 
	
	SQHelper sqHelper = new SQHelper(this);
	LayoutInflater mInflater;
	DatabaseCursorAdapter databaseCursorAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_database_list);
		mInflater = LayoutInflater.from(this);

		NavigationBarManager navi = new NavigationBarManager(this, ACTIVITYNUMBER);
		
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
		databaseCursorAdapter = new DatabaseCursorAdapter(this, c, 0);
		setListAdapter(databaseCursorAdapter);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		getActionBar().setSelectedNavigationItem(ACTIVITYNUMBER);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_database_list, menu);
		return true;
	}

	class DatabaseCursorAdapter extends CursorAdapter {

		int timeStart;
		int timeEnd;
		int theme;
		int type;
		int duration;
		DateTimeFormatter fmt = DateTimeFormat.forPattern("dd.MM.yy - HH:mm:ss");

		public DatabaseCursorAdapter(Context context, Cursor c, int flags) {
			super(context, c, flags);
			timeStart = c.getColumnIndex(SQHelper.KEY_STARTTIMEMILLIES);
			timeEnd = c.getColumnIndex(SQHelper.KEY_ENDTIMEMILLIES);
			theme = c.getColumnIndex(SQHelper.KEY_THEME);
			type = c.getColumnIndex(SQHelper.KEY_TYPE);
			duration = c.getColumnIndex(SQHelper.KEY_DURATION);
		}

		@Override
		public void bindView(View view, Context context, Cursor c) {
			final int rowId = c.getInt(c.getColumnIndex(SQHelper.KEY_ROWID));
			
			view.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					showDeleteDialog(rowId);
				}
			});
			
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
			tv1.setText(start.toString(fmt));
			tv1.append("   (" + c.getInt(duration) +") min");
			tv2.setText(sqHelper.getTheme(c.getInt(theme)));
		}

		@Override
		public View newView(Context context, Cursor cursor, ViewGroup parent) {
			return mInflater.inflate(R.layout.database_list_row, parent, false);
		}
		}

	private void showDeleteDialog(final int rowId){
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("Do you want to delete this entry?");
		builder.setCancelable(true);
		builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				sqHelper.deleteEntry(rowId);
				databaseCursorAdapter.getCursor().requery();
				databaseCursorAdapter.notifyDataSetChanged();
			}
		});
		builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
	}
		});
		AlertDialog dialog = builder.create();
		dialog.show();
	}
}
