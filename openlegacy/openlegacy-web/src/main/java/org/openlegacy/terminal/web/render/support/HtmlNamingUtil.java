package org.openlegacy.terminal.web.render.support;

import org.apache.commons.lang.math.NumberUtils;
import org.openlegacy.terminal.TerminalField;
import org.openlegacy.terminal.TerminalPosition;
import org.openlegacy.terminal.support.SimpleTerminalPosition;

import java.text.MessageFormat;

public class HtmlNamingUtil {

	public static final String TERMINAL_ACTION = "terminalAction";
	public static final String TERMINAL_CURSOR = "terminalCursor";
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
