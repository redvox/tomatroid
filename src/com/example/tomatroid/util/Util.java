package com.example.tomatroid.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.joda.time.DateMidnight;
import org.joda.time.DateTime;

import com.example.tomatroid.MainActivity;

import android.content.Context;
import android.os.Environment;
import android.text.GetChars;
import android.util.Log;
import android.widget.Toast;

public class Util {
	
	public static boolean DEBUG = true;

	static public String generateTimeText(float rawMinutes) {
		int minutes = (int) rawMinutes % 60;
		int hours = (int) rawMinutes / 60;
		int days = (int) hours / 24;
		// hours = hours % 24;

		StringBuffer stb = new StringBuffer();

		// if(days > 0){
		// stb.append(days);
		// stb.append("d ");
		// if (hours < 10)
		// stb.append("0");
		// }

		if (hours > 0) {
			stb.append(hours);
			stb.append("h ");
			if (minutes < 10)
				stb.append("0");
		}
		stb.append(minutes);
		stb.append("m");

		return stb.toString();
	}

	static public int[][] getLastXDatesArray(int days) {
		int[][] dates = new int[days][4];
		DateMidnight dm = new DateMidnight();
		dm = dm.minusDays(days);
		for (int i = 0; i < days; i++) {
			dm = dm.plusDays(1);
			dates[i][0] = dm.getDayOfMonth();
			dates[i][1] = dm.getMonthOfYear();
			dates[i][2] = dm.getYear();
			dates[i][3] = dm.getDayOfWeek();
		}
		return dates;
	}

	public static String readRawTextFile(Context contex, int id) {
		InputStream inputStream = contex.getResources().openRawResource(id);
		InputStreamReader in = new InputStreamReader(inputStream);
		BufferedReader buf = new BufferedReader(in);
		String line;
		StringBuilder text = new StringBuilder();
		try {
			while ((line = buf.readLine()) != null)
				text.append(line);
		} catch (IOException e) {
			return null;
		}
		return text.toString();
	}

	public static void writeLog(Context contex, String text) {
		String timestamp = Util.getTimeStamp();
		Log.e("Log", timestamp + " " + text);
		
		File root = Environment.getExternalStorageDirectory();
		File file = new File(root, "Tomatroid_Log.txt");
		try {
			if (root.canWrite()) {
				FileWriter filewriter = new FileWriter(file, true);
				BufferedWriter out = new BufferedWriter(filewriter);
				out.append(timestamp + " " + text + "\n");
				out.close();
			}
		} catch (IOException e) {
			Log.e("TAG", "Could not write file " + e.getMessage());
		}
	}
	
	public static String getTimeStamp(){
		DateTime dt = new DateTime();
		StringBuffer sb = new StringBuffer();
		sb.append("[");
		sb.append(getLeadingZero(dt.getHourOfDay()));
		sb.append(":");
		sb.append(getLeadingZero(dt.getMinuteOfHour()));
		sb.append(":");
		sb.append(getLeadingZero(dt.getSecondOfMinute()));
		sb.append("]");
		return sb.toString();
	}
	
	public static String getLeadingZero(int number){
		if(number < 10) return "0"+number;
		return ""+number;
	}
	
	public static String milliesToTimeStamp(long millis){
		StringBuffer sb = new StringBuffer();
		sb.append((millis / (1000 * 60 * 60)) % 24);
		sb.append("h ");
		sb.append((millis / (1000 * 60)) % 60);
		sb.append("m ");
		sb.append((millis / 1000) % 60);
		sb.append("s ");
		return sb.toString();
	}
}
