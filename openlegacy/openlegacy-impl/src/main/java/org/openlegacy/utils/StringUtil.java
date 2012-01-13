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

	/**
	 * Might be use in the future for additional field formatting ("_", etc)
	 */
	public static boolean isEmpty(String text) {
		return StringUtils.isBlank(text);
	}

	public static String toSampleValue(String value) {
		return StringUtils.trim(value);
	}

	public static int startOfNonBlank(String text) {
		return startOfNonChar(text, ' ');
	}

	private static int startOfNonChar(String text, char ch) {
		char[] chars = text.toCharArray();
		for (int i = 0; i < chars.length; i++) {
			if (chars[i] != ch) {
				return i;
			}
		}
		return 0;
	}

	public static final String leftTrim(String string) {
		return leftTrim(string, ' ');
	}

	public static final String leftTrim(String string, char ch) {
		int len = string.length(), i;
		for (i = 0; i < len; i++) {
			if (string.charAt(i) != ch) {
				break;
			}
		}
		if (i == len) {
			return StringUtils.EMPTY;
		}
		return string.substring(i);
	}

	public static final String rightTrim(String s) {
		return rightTrim(s, ' ');
	}

	public static final String rightTrim(String string, char ch) {
		int len = string.length(), i;
		for (i = len - 1; i >= 0; i--) {
			if (string.charAt(i) != ch) {
				break;
			}
		}
		if (i < 0) {
			return StringUtils.EMPTY;
		}
		return string.substring(0, i + 1);
	}

}
