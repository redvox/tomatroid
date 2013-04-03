package com.example.tomatroid.util;

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
}
