package org.openlegacy.utils;

import org.apache.commons.lang.StringUtils;

public class StringUtil {

	public static String ignoreChars(String s, char[] ignoreChars) {
		char[] chars = ignoreChars;
		for (char c : chars) {
			s = s.replaceAll("\\" + Character.toString(c), "");
		}
		return s;
	}

	public static int getLength(String s) {
		if (s == null) {
			return 0;
		}
		return s.length();
	}

	public static String toJavaFieldName(String text) {
		return toVariableName(text, false);
	}

	public static String toClassName(String text) {
		return toVariableName(text, true);
	}

	private static String toVariableName(String text, boolean capFirst) {
		char[] chars = text.toCharArray();
		StringBuilder sb = new StringBuilder(text.length());
		for (char d : chars) {
			char c = d;

			if (capFirst) {
				c = Character.toUpperCase(c);
				capFirst = false;
			} else {
				c = Character.toLowerCase(c);
			}

			if (Character.isLetter(c) || Character.isDigit(c)) {
				sb.append(c);
			}
			if (c == ' ') {
				capFirst = true;
			}

		}
		return sb.toString();

	}

	public static String toDisplayName(String text) {
		char[] chars = text.toCharArray();
		StringBuilder sb = new StringBuilder(text.length());
		for (char c : chars) {
			if (Character.isLetter(c) || Character.isDigit(c) || c == ' ') {
				sb.append(c);
			}

		}
		return sb.toString().trim();
	}

	public static boolean isEmpty(String text) {
		return StringUtils.isBlank(text);
	}
}
