package org.openlegacy.support;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openlegacy.FieldFormatter;
import org.openlegacy.utils.StringUtil;

import java.text.MessageFormat;

public class SimpleFieldFormatter implements FieldFormatter {

	private char[] chars = new char[0];

	private boolean trim;
	private boolean uppercase;
	private boolean lowercase;

	private final static Log logger = LogFactory.getLog(SimpleFieldFormatter.class);

	public String format(String s) {
		if (s.length() == 0) {
			return s;
		}

		String result = trim ? s.trim() : s;

		if (result.length() == 0) {
			return result;
		}

		result = StringUtil.ignoreChars(result, chars);

		if (uppercase && lowercase) {
			throw (new IllegalArgumentException("Can't define both uppercase and lower case formatting"));
		}

		result = uppercase ? result.toUpperCase() : result;
		result = lowercase ? result.toLowerCase() : result;

		if (logger.isTraceEnabled()) {
			logger.trace(MessageFormat.format("Formatted content \''{0}\'' to \''{1}\''", s, result));
		}
		return result;
	}

	public void setRemoveChars(char[] chars) {
		this.chars = chars;
	}

	public void setTrim(boolean trim) {
		this.trim = trim;
	}

	public void setLowercase(boolean lowercase) {
		this.lowercase = lowercase;
	}

	public void setUppercase(boolean uppercase) {
		this.uppercase = uppercase;
	}
}
