/*******************************************************************************
 * Copyright (c) 2012 OpenLegacy Inc.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     OpenLegacy Inc. - initial API and implementation
 *******************************************************************************/
package org.openlegacy.utils;

import org.apache.commons.lang.CharEncoding;
import org.apache.commons.lang.StringUtils;
import org.openlegacy.exceptions.OpenLegacyRuntimeException;
import org.springframework.util.Assert;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StringUtil {

	/**
	 * this array contains forbidden words from java,c#,vb,javaScript
	 */
	private static final String[] RESERVED_WORDS = new String[] { "abstract", "do", "if", "package", "synchronized",
			"implements", "private", "this", "break", "else", "import", "protected", "throw", "byte", "extends", "instanceof",
			"public", "throws", "case", "false", "return", "transient", "catch", "final", "interface", "short", "int", "float",
			"double", "boolean", "true", "char", "long", "static", "try", "class", "native", "strictfp", "void", "const", "for",
			"new", "super", "volatile", "continue", "goto", "null", "switch", "while", "default", "assert", "exception", "java",
			"menu", "jsp", "context", /** java-script **/
			"action" };

	private static Map<String, String> RESERVERD_WORDS_DICTIONARY = new HashMap<String, String>();

	static {
		for (String element : RESERVED_WORDS) {
			RESERVERD_WORDS_DICTIONARY.put(element, element);
		}
	}

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

	public static String toJavaMethodName(String text) {
		String methodName = toVariableName(text, false);
		return methodName;
	}

	public static String toClassName(String text) {
		if (text == null) {
			return null;
		}
		if (text.endsWith(".class")) {
			text = text.replace(".class", "");
		}
		// remove package
		if (text.contains(".")) {
			text = text.substring(text.lastIndexOf(".") + 1);
		}
		return toVariableName(text, true);
	}

	public static String toVariableName(String text, boolean capFirst) {

		String variableName = null;

		if (!text.contains(" ")) {
			text = text.replaceAll("\\W", "");
			if (!capFirst) {
				variableName = StringUtils.uncapitalize(text);
			} else {
				variableName = StringUtils.capitalize(text);
			}
		} else {
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
			variableName = sb.toString();
		}

		if (RESERVERD_WORDS_DICTIONARY.containsKey(variableName)) {
			variableName = variableName + "_";
		}

		if (!StringUtils.isEmpty(variableName) && variableName.charAt(0) >= '0' && variableName.charAt(0) <= '9') {
			variableName = "_" + variableName;
		}

		return variableName;

	}

	public static String toDisplayName(String text) {
		if (StringUtils.isAllUpperCase(text)) {
			text = text.toLowerCase();
		}
		char[] chars = text.toCharArray();
		StringBuilder sb = new StringBuilder(text.length());
		for (int i = 0; i < chars.length; i++) {
			char c = chars[i];
			if (Character.isLetter(c) || Character.isDigit(c) || c == ' ' || c == '\\' || c == '/') {
				if (i == 0) {
					sb.append(Character.toUpperCase(c));
				} else {
					// add blank current char is upper case, previous char is blank and not upper case
					if (Character.isUpperCase(c) && chars[i - 1] != ' ' && Character.isLetter(chars[i - 1])
							&& !Character.isUpperCase(chars[i - 1])) {
						sb.append(' ');
					}
					sb.append(c);
				}
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
		value = value.replace('\"', '\'');
		return StringUtils.trim(value);
	}

	public static int startOfNonBlank(String text) {
		return startOfNonChar(text, ' ');
	}

	public static int endOfNonBlank(String text) {
		return endOfNonChar(text, ' ');
	}

	private static int startOfNonChar(String text, char ch) {
		Assert.notNull(text, "text must not be empty");
		char[] chars = text.toCharArray();
		for (int i = 0; i < chars.length; i++) {
			if (chars[i] != ch) {
				return i;
			}
		}
		return 0;
	}

	private static int endOfNonChar(String value, char ch) {
		char[] chars = value.toCharArray();
		int rightOffset = 0;
		for (int i = chars.length - 1; i >= 0; i--) {
			if (chars[i] != ch) {
				rightOffset = chars.length - i;
				break;
			}
		}
		return rightOffset;
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
		String str = rightTrim(s, ' ');
		str = rightTrim(str, 0);
		return str;
	}

	private static final String rightTrim(String string, int ch) {
		int len = string.length();
		int i;
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

	/**
	 * Return Java type according to string content
	 */
	public static Class<?> getTypeByValue(String value) {
		if (value == null) {
			return String.class;
		}
		value = value.trim();
		if (value.matches("\\d+")) {
			return Integer.class;
		}
		if (value.matches("\\d+\\.\\d+")) {
			return Double.class;
		}
		return String.class;
	}

	public static String toSetterMethodName(String propertyName) {
		return "set" + toVariableName(propertyName, true);
	}

	public static String toString(ByteArrayOutputStream baos) {
		try {
			return new String(baos.toByteArray(), CharEncoding.UTF_8);
		} catch (UnsupportedEncodingException e) {
			throw (new OpenLegacyRuntimeException(e));
		}
	}

	/**
	 * Remove namespace from a given class style String: package.class -> class , prefix.suffix -> suffix
	 * 
	 * @param str
	 * @return
	 */
	public static String removeNamespace(String str) {
		if (!str.contains(".")) {
			return str;
		}
		return str.substring(str.lastIndexOf(".") + 1);
	}

	public static String stripQuotes(String text) {
		if (text.startsWith("\"")) {
			text = text.substring(1);
		}
		if (text.endsWith("\"")) {
			text = text.substring(0, text.length() - 1);
		}

		return text;
	}

	/**
	 * Surrounds a given string with quotes if it of type string. Usefull for code generation
	 * 
	 * @param value
	 * @return
	 */
	public static Object surroundStringWithQuotes(String value) {
		Class<?> type = getTypeByValue(value);
		if (type == String.class) {
			return "\"" + value + "\"";
		}
		return value;
	}

	public static String appendLeftZeros(int number, int length) {
		String str = String.valueOf(number);
		for (int i = str.length(); i < length; i++) {
			str = "0" + str;
		}
		return str;
	}

	public static int getDifferentCharsCount(String s) {
		char[] chars = s.toCharArray();
		List<String> charTypes = new ArrayList<String>();
		for (char c : chars) {
			String valueOfChar = String.valueOf(c);
			if (!charTypes.contains(valueOfChar)) {
				charTypes.add(valueOfChar);
			}
		}
		return charTypes.size();
	}

	public static String toString(List<?> elements, char seperator) {
		if (elements == null || elements.size() == 0) {
			return "";
		}

		StringBuilder sb = new StringBuilder();
		for (Object object : elements) {
			if (object != null) {
				sb.append(object.toString());
				sb.append(seperator);
			}
		}
		if (sb.length() > 0 && sb.charAt(sb.length() - 1) == seperator) {
			sb.setLength(sb.length() - 1);
		}
		return sb.toString();
	}

	public static String nullsToSpaces(String value) {
		char[] chars = new char[] { 0 };
		if (StringUtils.containsNone(value, chars)) {
			return value;
		}
		return nullsToSpaces(value.toCharArray());
	}

	public static String nullsToSpaces(char[] chars) {
		for (int i = 0; i < chars.length; i++) {
			if (chars[i] == 0) {
				chars[i] = ' ';
			}
		}
		String value = new String(chars);
		return value;
	}

}
