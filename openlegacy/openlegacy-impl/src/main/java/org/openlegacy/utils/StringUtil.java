package org.openlegacy.utils;

public class StringUtil {

	public static String ignoreChars(String s, char[] ignoreChars) {
		char[] chars = ignoreChars;
		for (char c : chars) {
			s = s.replaceAll("\\" + Character.toString(c), "");
		}
		return s;
	}
}
