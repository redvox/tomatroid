package com.example.tomatroid.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.joda.time.DateMidnight;

import android.content.Context;
import android.text.GetChars;

public class Util {

	
	static public String generateTimeText(float value) {
		int hours = (int) value / 60;
		int minutes = (int) value % 60;

		StringBuffer stb = new StringBuffer();

		if (hours > 0) {
			stb.append(hours);
			stb.append(":");
			if (minutes < 10)
				stb.append("0");
		}
		stb.append(minutes);

		return stb.toString();
	}
	
	static public int[][] getLastXDatesArray(int days){
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
}
