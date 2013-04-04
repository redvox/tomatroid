package com.example.tomatroid.util;

import org.joda.time.DateMidnight;

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
		int[][] dates = new int[days][3];
		DateMidnight dm = new DateMidnight();
		dm = dm.minusDays(days);
		for (int i = 0; i < days; i++) {
			dm = dm.plusDays(1);
			dates[i][0] = dm.getDayOfMonth();
			dates[i][1] = dm.getMonthOfYear();
			dates[i][2] = dm.getYear();
		}
		return dates;
	}
}
