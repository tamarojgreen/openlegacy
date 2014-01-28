/*******************************************************************************
 * Copyright (c) 2014 OpenLegacy Inc.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     OpenLegacy Inc. - initial API and implementation
 *******************************************************************************/
package org.openlegacy.support;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openlegacy.FieldFormatter;
import org.openlegacy.utils.StringUtil;

import java.io.Serializable;
import java.text.MessageFormat;

/**
 * A field formatter implementation which:
 * <ul>
 * <li>Removes chars configured via spring configuration</li>
 * <li>Can convert to lower/upper case via spring configuration</li>
 * <li>Can be configure to trim content</li>
 * 
 */
public class SimpleFieldFormatter implements FieldFormatter, Serializable {

	private static final long serialVersionUID = 1L;

	private char[] chars = new char[0];

	private boolean trim;
	private boolean leftTrim;
	private boolean rightTrim;
	private boolean uppercase;
	private boolean lowercase;

	private final static Log logger = LogFactory.getLog(SimpleFieldFormatter.class);

	public String format(String s) {

		if (StringUtils.isEmpty(s)) {
			return s;
		}

		String result = trim ? s.trim() : s;

		if (!trim && leftTrim) {
			result = StringUtil.leftTrim(result);
		}

		if (!trim && rightTrim) {
			result = StringUtil.rightTrim(result);
		}

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
			if (!s.equals(result)) {
				logger.trace(MessageFormat.format("Formatted content \''{0}\'' to \''{1}\''", s, result));
			}
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

	public void setLeftTrim(boolean leftTrim) {
		this.leftTrim = leftTrim;
	}

	public void setRightTrim(boolean rightTrim) {
		this.rightTrim = rightTrim;
	}
}
