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
package org.openlegacy.terminal.web.render.support;

import org.apache.commons.lang.math.NumberUtils;
import org.openlegacy.terminal.TerminalField;
import org.openlegacy.terminal.TerminalPosition;
import org.openlegacy.terminal.support.SimpleTerminalPosition;
import org.openlegacy.web.HtmlConstants;

import java.text.MessageFormat;

public class HtmlNamingUtil {

	private static final String FIELD_NAME_FORMAT = "p_{0}_{1}";

	public static String getFieldName(TerminalField field) {
		return getFieldName(field.getPosition());
	}

	public static String getFieldName(TerminalPosition position) {
		return MessageFormat.format(FIELD_NAME_FORMAT, position.getRow(), position.getColumn());
	}

	public static TerminalPosition toPosition(String fieldName) {
		if (fieldName == null) {
			return null;
		}
		String[] parts = fieldName.split("_");
		if (parts.length != 3) {
			return null;
		}
		return new SimpleTerminalPosition(NumberUtils.toInt(parts[1]), NumberUtils.toInt(parts[2]));
	}

	public static String toStyleWidth(int width) {
		return MessageFormat.format("width:{0}{1};", width, HtmlConstants.STYLE_UNIT);
	}

	public static String toStyleHeight(int height) {
		return MessageFormat.format("height:{0}{1};", height, HtmlConstants.STYLE_UNIT);
	}

	public static String toStyleTopLeft(int top, int left) {
		return MessageFormat.format("top:{0}{2};left:{1}{2};", top, left, HtmlConstants.STYLE_UNIT);
	}

}
