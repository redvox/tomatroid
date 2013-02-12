package com.example.tomatroid.sql;

import org.joda.time.DateTime;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SQHelper extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "pomodorodroid";
	private static final int DATABASE_VERSION = 1;

	public static final String TABLE_DATES = "dates";
	public static final String KEY_ROWID = "_id";
	public static final String KEY_DATE_DAY = "day";
	public static final String KEY_DATE_MONTH = "month";
	public static final String KEY_DATE_YEAR = "year";
	public static final String KEY_DATE_WEEKDAY = "weekday";
	public static final String KEY_DATE_WEEKNUM = "weeknum";
	public static final String KEY_DATE_START_TIME = "starttime";
	public static final String KEY_DATE_END_TIME = "endtime";
	public static final String KEY_DURATION = "duration";
	public static final String KEY_STARS = "stars";
	public static final String KEY_THEME = "theme";

	static final String CREATE_DATES_TABLE = "create table " 
			+ TABLE_DATES + "("
			+ KEY_ROWID + " integer primary key autoincrement, "
			+ KEY_DATE_DAY + " integer not null, " 
			+ KEY_DATE_MONTH + " integer not null,"
			+ KEY_DATE_YEAR + " integer not null," 
			+ KEY_DATE_WEEKDAY + " integer not null," 
			+ KEY_DATE_WEEKNUM + " integer not null," 
			+ KEY_DATE_START_TIME + " integer not null,"
			+ KEY_DATE_END_TIME + " integer not null," 
			+ KEY_DURATION + " integer not null," 
			+ KEY_STARS + " integer," 
			+ KEY_THEME + " integer,"  
			+ ");";

	public static final String TABLE_THEME = "themes";
	// ROW ID
	public static final String KEY_NAME = "name";

	static final String CREATE_THEME_TABLE = "create table " 
			+ TABLE_THEME + "("
			+ KEY_ROWID + " integer primary key autoincrement, "
			+ KEY_NAME + " text not null, " 
			+ ");";
	
	DateTime time = new DateTime();

	public SQHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		database.execSQL(CREATE_DATES_TABLE);
		database.execSQL(CREATE_THEME_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(SQHelper.class.getName(), "Upgrading database from version "
				+ oldVersion + " to " + newVersion
				+ ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_DATES);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_THEME);
		onCreate(db);
	}

}
