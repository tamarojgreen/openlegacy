package org.openlegacy.terminal.support;

import org.openlegacy.terminal.ScreenPosition;
import org.openlegacy.terminal.ScreenSize;
import org.openlegacy.terminal.TerminalField;

public class ScreenUtils {

	public static int toAbsolutePosition(ScreenPosition screenPosition, ScreenSize screenSize) {
		int rowStart = (screenPosition.getRow() - 1) * screenSize.getColumns();
		return rowStart + screenPosition.getColumn() - 1;
	}

	public static StringBuilder initEmptyBuffer(ScreenSize size) {
		int capacity = size.getRows() * size.getColumns();
		StringBuilder buffer = new StringBuilder(capacity);
		for (int i = 0; i < capacity; i++) {
			buffer.append(' ');
		}
		return buffer;
	}

	public static void placeContentOnBuffer(StringBuilder buffer, TerminalField terminalField, ScreenSize screenSize) {
		int beginIndex = ScreenUtils.toAbsolutePosition(terminalField.getPosition(), screenSize);

		String value = terminalField.getValue();
		if (value.length() == 0) {
			return;
		}
		for (int i = 0; i < value.length(); i++) {
			buffer.setCharAt(beginIndex + i, value.charAt(i));
		}
	}
}
