package com.example.tomatroid.sql;

import java.util.ArrayList;

import org.joda.time.DateTime;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SQHelper extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "pomodorodroid";
	private static final int DATABASE_VERSION = 11;

	// TABLE Dates
	public static final String TABLE_DATES = "dates";
	public static final String KEY_ROWID = "_id";
	public static final String KEY_DATE_DAY = "day";
	public static final String KEY_DATE_MONTH = "month";
	public static final String KEY_DATE_YEAR = "year";
	public static final String KEY_DATE_WEEKDAY = "weekday";
	public static final String KEY_DATE_WEEKNUM = "weeknum";
	public static final String KEY_DATE_START_HOUR = "starttime_hour";
	public static final String KEY_DATE_START_MINUTE = "starttime_minute";
	public static final String KEY_DATE_END_HOUR = "endtime_hour";
	public static final String KEY_DATE_END_MINUTE = "endtime_minute";
	public static final String KEY_DURATION = "duration";
	public static final String KEY_STARTTIMEMILLIES = "starttimemillies";
	public static final String KEY_ENDTIMEMILLIES = "endtimemillies";
	public static final String KEY_TYPE = "type";
	public static final String KEY_THEME = "theme";

	static final String CREATE_DATES_TABLE = "create table " + TABLE_DATES
			+ "(" 
			+ KEY_ROWID + " integer primary key autoincrement, "
			+ KEY_DATE_DAY + " integer not null, " 
			+ KEY_DATE_MONTH + " integer not null," 
			+ KEY_DATE_YEAR + " integer not null,"
			+ KEY_DATE_WEEKDAY + " integer not null," 
			+ KEY_DATE_WEEKNUM + " integer not null," 
			+ KEY_DATE_START_HOUR + " integer not null,"
			+ KEY_DATE_START_MINUTE + " integer not null," 
			+ KEY_DATE_END_HOUR + " integer not null," 
			+ KEY_DATE_END_MINUTE + " integer not null,"
			+ KEY_DURATION + " integer not null," 
			+ KEY_TYPE + " integer not null," 
			+ KEY_THEME + " integer,"
			+ KEY_STARTTIMEMILLIES + " text not null," 
			+ KEY_ENDTIMEMILLIES + " text not null" 
			+ ");";

	// TABLE Theme
	public static final String TABLE_THEME = "themes";
	// ROW ID
	public static final String KEY_NAME = "name";
	public static final String KEY_ITEMOF = "itemof";
	public static final String KEY_OVERALLTIME = "overalltime";
	public static final String KEY_OVERALLTIME_W_CHILDREN = "overalltimewithchildren";
	public static final String KEY_HIDE = "hide";

	static final String CREATE_THEME_TABLE = "create table " + TABLE_THEME
			+ "(" + KEY_ROWID + " integer primary key autoincrement, "
			+ KEY_ITEMOF + " integer," + KEY_NAME + " text not null, "
			+ KEY_OVERALLTIME + " integer not null, "
			+ KEY_OVERALLTIME_W_CHILDREN + " integer not null, "
			+ KEY_HIDE + " integer not null "
			+ ");";

	public static final int TYPE_POMODORO = 0;
	public static final int TYPE_SHORTBREAK = 1;
	public static final int TYPE_LONGBREAK = 2;
	public static final int TYPE_TRACKING = 3;
	public static final int TYPE_SLEEPING = 4;

	SQLiteDatabase db;

	public SQHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		Log.e("SQHepler", "constructor DATABASE_VERSION: "+DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		db = database;
		Log.e("SQHepler", "onCreate");
		createTables(database);
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
		db = database;
		Log.e(SQHelper.class.getName(), "Upgrading database from version " + oldVersion +" to "+newVersion);
		
		if(oldVersion <= 5){
			Log.e(SQHelper.class.getName(), "++ Datebase Upgrade 5 ++");
			String sql2 = "ALTER TABLE "+TABLE_THEME+" "
			+"ADD "+KEY_OVERALLTIME_W_CHILDREN+" integer not null default 0";
			db.execSQL(sql2);
		}
		
		if(oldVersion <= 11){
			Log.e(SQHelper.class.getName(), "++ Datebase Upgrade 11 ++");
			Cursor c1 = getThemeCursor();
			if (c1.moveToFirst()) {
				int column_id = c1.getColumnIndex(KEY_ROWID);
				int column_overalltime = c1.getColumnIndex(KEY_OVERALLTIME);
				do{
					int id = c1.getInt(column_id);
					int overalltime = c1.getInt(column_overalltime);
					incrementColumn(id, KEY_OVERALLTIME_W_CHILDREN, TABLE_THEME, overalltime);
					incrementParentTime(id, overalltime);
					} while(c1.moveToNext());
			} 
		}
	}

	public void createTables(SQLiteDatabase database) {
		database.execSQL(CREATE_DATES_TABLE);
		database.execSQL(CREATE_THEME_TABLE);
		addTheme("Default", -1);
		addTheme("Gaming", -1);
		addTheme("Pomodoro App", -1);
	}

	public Cursor getDatesCursor() {
		openDatabase();
		return db.query(TABLE_DATES, null, null, null, null, null, 
				KEY_ROWID+ " DESC");
	}

	public Cursor getThemeCursor() {
		openDatabase();
		return db.query(TABLE_THEME, null, null, null, null, null,
				KEY_OVERALLTIME+ " DESC");
	}
	
	public Cursor getThemeCursor(int hideStatus, boolean withChildren) {
		openDatabase();
		String sortby;
		if(withChildren){
			sortby = KEY_OVERALLTIME_W_CHILDREN;
		} else {
			sortby = KEY_OVERALLTIME;
		}
		return db.query(TABLE_THEME, null, KEY_HIDE +" = "+hideStatus, null, null, null,
				sortby+ " DESC");
	}
	
	public Cursor getCursorForDay(int day, int month, int year){
		openDatabase();
		return db.query(TABLE_DATES, null, KEY_DATE_DAY +" = "+day+" AND "+KEY_DATE_MONTH +" = "+month+" AND "+KEY_DATE_YEAR +" = "+year, null, null, null,
				null);
	}

	public int getTodayCountOf(int type) {		
		openDatabase();
		DateTime dt = new DateTime();

		return getCount(KEY_ROWID, new String[][]{{
			KEY_DATE_DAY, 
			KEY_DATE_MONTH, 
			KEY_DATE_YEAR, 
			KEY_TYPE}}, 
			new int[][]{{
				dt.getDayOfMonth(), 
				dt.getMonthOfYear(), 
				dt.getYear(), 
				type
				}});
	}
	
	public int getTodaySumOf(int type) {
		openDatabase();
		DateTime dt = new DateTime();
		
		return getSum(KEY_DURATION, new String[][]{{
			KEY_DATE_DAY, 
			KEY_DATE_MONTH, 
			KEY_DATE_YEAR, 
			KEY_TYPE}}, 
			new int[][]{{
				dt.getDayOfMonth(), 
				dt.getMonthOfYear(), 
				dt.getYear(), 
				type
				}});
	}
	
	public int getTotalDurationWithoutSleep() {
		openDatabase();

		Cursor cursor = db
				.rawQuery("SELECT SUM("+ KEY_DURATION +") FROM "+ TABLE_DATES +" WHERE "+ KEY_TYPE +" != "+ TYPE_SLEEPING, null);
		if (cursor.moveToFirst()) {
			return cursor.getInt(0);
		} else {
			return 0;
		}
	}

	public int getSum(String field, String[][] keys, int[][] values) {
		openDatabase();
		
		String where = buildDNFWhereSQLStatement(keys, values);
		Cursor cursor = db
				.rawQuery("SELECT SUM("+ field +") FROM "+ TABLE_DATES +" WHERE "+ where, null);
		if (cursor.moveToFirst()) {
			return cursor.getInt(0);
		} else {
			return 0;
		}
	}
	
	public int getCount(String field, String[][] keys, int[][] values) {
		openDatabase();
		
		String where = buildDNFWhereSQLStatement(keys, values);
		Cursor cursor = db
				.rawQuery("SELECT COUNT("+ field +") FROM "+ TABLE_DATES +" WHERE "+ where, null);
		if (cursor.moveToFirst()) {
			return cursor.getInt(0);
		} else {
			return 0;
		}
	}
	
	public int getGroupCount(String[] groupe, String[][] keys, int[][] values){
		openDatabase();
	
		String where = buildDNFWhereSQLStatement(keys, values);
		StringBuffer sb = new StringBuffer();
		
		for (int k = 0; k < groupe.length - 1; k++) {
			sb.append(groupe[k] + ", ");
		}
		sb.append(groupe[groupe.length - 1]);
		
		Cursor cursor = db
				.rawQuery("SELECT * FROM "+ TABLE_DATES +" WHERE "+ where + " GROUP BY "+sb.toString(), null);
		if (cursor.moveToFirst()) {
			return cursor.getCount();
		} else {
			return 0;
		}
	}
	
	public int getDaysCount(String[][] keys, int[][] values){
		openDatabase();
	
		String where = buildDNFWhereSQLStatement(keys, values);
		Cursor cursor = db.query(TABLE_DATES, null, where, null, null, null, null);
		
		if (cursor.moveToFirst()) {
			int daysCount = 0;
			int oldDayNumber = 0;
			int oldMonthNumber = 0;
			int oldYearNumber = 0;
			
			int column_day = cursor.getColumnIndex(KEY_DATE_DAY);
			int column_month = cursor.getColumnIndex(KEY_DATE_MONTH);
			int column_year = cursor.getColumnIndex(KEY_DATE_YEAR);
			
			do{
				if( (oldDayNumber != cursor.getInt(column_day)) || (oldMonthNumber != cursor.getInt(column_month)) || (oldYearNumber != cursor.getInt(column_year)) ){
					oldDayNumber = cursor.getInt(column_day);
					oldMonthNumber = cursor.getInt(column_month);
					oldYearNumber = cursor.getInt(column_year);
					daysCount++;
				}
			} while(cursor.moveToNext());
			return daysCount;
		} else {
			return 0;
		}
	}
	
	public String buildDNFWhereSQLStatement(String[][] keys, int[][] values) {

		ArrayList<StringBuffer> bufferArray = new ArrayList<StringBuffer>();

		for (int i = 0; i < keys.length; i++) {
			StringBuffer sb = new StringBuffer();
			for (int k = 0; k < keys[i].length - 1; k++) {
				sb.append(keys[i][k] + " = " + values[i][k] + " AND ");
			}
			sb.append(keys[i][keys[i].length - 1] + " = " + values[i][keys[i].length - 1]);
			bufferArray.add(sb);
		}

		StringBuffer finalwhere = new StringBuffer();
		for (int h = 0; h < bufferArray.size() - 1; h++) {
			finalwhere.append(bufferArray.get(h)+" OR ");
		}
		finalwhere.append(bufferArray.get(bufferArray.size()-1));

		return finalwhere.toString();
	}

	public void addTheme(String name, int parentId) {
		openDatabase();
		ContentValues newContent = new ContentValues();
		newContent.put(KEY_NAME, name);
		newContent.put(KEY_ITEMOF, parentId);
		newContent.put(KEY_OVERALLTIME, 0);
		newContent.put(KEY_OVERALLTIME_W_CHILDREN, 0);
		newContent.put(KEY_HIDE, 0);
		db.insert(TABLE_THEME, null, newContent);
	}
	
	public void changeTheme(int id, String name, int parentId){
		openDatabase();		
		Cursor cursor = db.query(TABLE_THEME, new String[] { KEY_OVERALLTIME },  KEY_ROWID +" = " + id, null, null, null,
				KEY_OVERALLTIME+ " DESC");
		int totalTime = 0;
		if(cursor.moveToFirst()){
		totalTime = cursor.getInt(0);
		}
		
		decrementParentTime(id, totalTime);
		
		ContentValues newContent = new ContentValues();
		newContent.put(KEY_NAME, name);
		newContent.put(KEY_ITEMOF, parentId);
		db.update(TABLE_THEME, newContent, KEY_ROWID+" = "+id, null);
		
		incrementParentTime(id, totalTime);
	}
	
	public void deleteTheme(int id, String name, int newId){
		openDatabase();
		Cursor cursor = db.query(TABLE_THEME, new String[] { KEY_OVERALLTIME },  KEY_ROWID +" = " + id, null, null, null,
				KEY_OVERALLTIME+ " DESC");
		int totalTime = 0;
		if(cursor.moveToFirst()){
		totalTime = cursor.getInt(0);
		}
		
		ContentValues newContent = new ContentValues();
		newContent.put(KEY_THEME, newId);
		db.update(TABLE_DATES, newContent, KEY_THEME+" = "+id, null);		
		db.delete(TABLE_THEME, KEY_ROWID+" = "+id, null);
		
		incrementParentTime(id, totalTime);
	}
	
	public void changeThemeStatus(int id, int hide){
		openDatabase();
		ContentValues newContent = new ContentValues();
		newContent.put(KEY_HIDE, hide);		
		db.update(TABLE_THEME, newContent, KEY_ROWID+" = "+id, null);
	}
	
	public String getTheme(int id) {
		if(id == -1) 
			return "";
		openDatabase();
		Cursor c = db.query(TABLE_THEME, new String[] { KEY_NAME }, KEY_ROWID
				+ " = ?", new String[] { id + "" }, null, null, null);
		String name = "";
		if (c.moveToFirst()) {
			name = c.getString(0);
		}
		return name;
	}

	public int getTheme(String name) {
		openDatabase();
		Cursor c = db.query(TABLE_THEME, new String[] { KEY_ROWID }, KEY_NAME
				+ " = ?", new String[] { name }, null, null, null);
		int id = -99;
		if (c.moveToFirst()) {
			id = c.getInt(0);
		}
		return id;
	}
	
	public int getParent(int id) {
		Cursor c = db.query(TABLE_THEME, new String[] { KEY_ITEMOF }, KEY_ROWID
				+ " = ?", new String[] { id + "" }, null, null, null);
		int parentid = -1;
		if (c.moveToFirst()) {
			parentid = c.getInt(0);
		}
		return parentid;
	}

	
	public ArrayList<Integer> getAllThemeChrildren(int id){
		ArrayList<Integer> list = new ArrayList<Integer>();
		openDatabase();
		Cursor c = db.query(TABLE_THEME, new String[] { KEY_ROWID }, KEY_ITEMOF
				+ " = ?", new String[] { ""+id }, null, null, null);
		if(c.moveToFirst()){
			do{
				list.add(c.getInt(0));
			} while(c.moveToNext());
		}
		return list;
	}
	
	public void deleteEntry(int id){
		db.delete(TABLE_DATES, KEY_ROWID+" = "+id, null);
	}
	
	public void changeEntry(int id, int type, int themeId){
		openDatabase();
		ContentValues newContent = new ContentValues();
		newContent.put(KEY_TYPE, type);
		if(type == TYPE_SLEEPING || type == TYPE_SHORTBREAK)
			themeId = -99;
		newContent.put(KEY_THEME, themeId);
		db.update(TABLE_DATES, newContent, KEY_ROWID+" = "+id, null);
	}

	public void renewTables() {
		openDatabase();
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_DATES);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_THEME);
		createTables(db);
	}
	
	public void insertDate(int tag, int minutesPast, String theme) {
		openDatabase();

		DateTime endDate = new DateTime();
		DateTime startDate = endDate.minusMinutes(minutesPast);
		ContentValues newContent = new ContentValues();
		newContent.put(KEY_DATE_DAY, startDate.getDayOfMonth());
		newContent.put(KEY_DATE_MONTH, startDate.getMonthOfYear());
		newContent.put(KEY_DATE_YEAR, startDate.getYear());
		newContent.put(KEY_DATE_WEEKDAY, startDate.getDayOfWeek());
		newContent.put(KEY_DATE_WEEKNUM, startDate.getWeekOfWeekyear());
		newContent.put(KEY_DATE_START_HOUR, startDate.getHourOfDay());
		newContent.put(KEY_DATE_START_MINUTE, startDate.getMinuteOfHour());
		newContent.put(KEY_DATE_END_HOUR, endDate.getHourOfDay());
		newContent.put(KEY_DATE_END_MINUTE, endDate.getMinuteOfHour());
		newContent.put(KEY_TYPE, tag);
		newContent.put(KEY_DURATION, minutesPast);
		newContent.put(KEY_THEME, getTheme(theme));
		newContent.put(KEY_STARTTIMEMILLIES, startDate.getMillis());
		newContent.put(KEY_ENDTIMEMILLIES, endDate.getMillis());
		db.insert(TABLE_DATES, null, newContent);
		
		int themeid = getTheme(theme);
		incrementColumn(themeid, KEY_OVERALLTIME, TABLE_THEME, minutesPast);
		incrementColumn(themeid, KEY_OVERALLTIME_W_CHILDREN, TABLE_THEME, minutesPast);
		incrementParentTime(themeid, minutesPast);
		
//		Log.e("SQHepler", "## Inspect Start ##");
//		Cursor c1 = getThemeCursor();
//		if (c1.moveToFirst()) {
//			int column_id = c1.getColumnIndex(SQHelper.KEY_ROWID);
//			int column_overalltime = c1.getColumnIndex(SQHelper.KEY_OVERALLTIME);
//			int column_overalltime_w_children = c1.getColumnIndex(SQHelper.KEY_OVERALLTIME_W_CHILDREN);
//			int column_itemof = c1.getColumnIndex(SQHelper.KEY_ITEMOF);
//			do{
//				int id = c1.getInt(column_id);
//				int overalltime = c1.getInt(column_overalltime);
//				int overalltime_w_children = c1.getInt(column_overalltime_w_children);
//				int itemof = c1.getInt(column_itemof);
//				Log.e(SQHelper.class.getName(), "id: "+getTheme(id)+" itemof: "+itemof+" overalltime: " + overalltime +" overalltime_w_children: "+overalltime_w_children);
//			} while(c1.moveToNext());
//		}
//		Log.e("SQHepler", "######## Inspect End #########");		
	}
	
	public void incrementColumn(int where, String column, String table, int value){
		db.execSQL("UPDATE " + table + " SET " 
				+ column + " = "+ column + " + " + value 
				+ " WHERE " + KEY_ROWID+ "=" + where);
	}
	
	public void incrementParentTime(int themeid, int minutesPast){
		int parentid = getParent(themeid);
		
		if(parentid != -1){
			Log.e(SQHelper.class.getName(), " insertChilddrenTime parentid "+getTheme(parentid)+" of themeid "+getTheme(themeid)+" value: "+minutesPast);
			incrementColumn(parentid, KEY_OVERALLTIME_W_CHILDREN, TABLE_THEME, minutesPast);
			incrementParentTime(parentid, minutesPast);
		} else {
			Log.e(SQHelper.class.getName(), " insertChilddrenTime "+getTheme(themeid)+" has no parent");
		}
	}
	
	public void decrementColumn(int where, String column, String table, int value){
		db.execSQL("UPDATE " + table + " SET " 
				+ column + " = "+ column + " - " + value 
				+ " WHERE " + KEY_ROWID+ "=" + where);
	}
	
	public void decrementParentTime(int themeid, int minutesPast){
		int parentid = getParent(themeid);
		
		if(parentid != -1){
			Log.e(SQHelper.class.getName(), " insertChilddrenTime parentid "+getTheme(parentid)+" of themeid "+getTheme(themeid)+" value: "+minutesPast);
			decrementColumn(parentid, KEY_OVERALLTIME_W_CHILDREN, TABLE_THEME, minutesPast);
			decrementParentTime(parentid, minutesPast);
		} else {
			Log.e(SQHelper.class.getName(), " insertChilddrenTime "+getTheme(themeid)+" has no parent");
		}
	}

	private void openDatabase() {
		if (db == null){
			db = getWritableDatabase();
		}
	}

}
