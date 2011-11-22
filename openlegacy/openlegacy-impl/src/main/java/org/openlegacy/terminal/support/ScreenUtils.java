package org.openlegacy.terminal.support;

import org.openlegacy.terminal.ScreenPosition;
import org.openlegacy.terminal.ScreenSize;
import org.openlegacy.terminal.TerminalField;
import org.openlegacy.terminal.TerminalRow;

import java.util.List;

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

	public static TerminalField getField(List<TerminalRow> rows, ScreenPosition position) {
		TerminalRow row = rows.get(position.getRow() - 1);
		return row.getField(position.getColumn());
	}

	/**
	 * Populates a snapshot from the given rows and screen size. Builds the fields separators and screen text
	 * 
	 * @param rows
	 * @param screenSize
	 * @param fieldsSeperators
	 * @return
	 */
	public static String initSnapshot(List<TerminalRow> rows, ScreenSize screenSize, List<ScreenPosition> fieldsSeperators) {

		StringBuilder buffer = ScreenUtils.initEmptyBuffer(screenSize);

		for (TerminalRow terminalRow : rows) {
			List<TerminalField> rowFields = terminalRow.getFields();
			for (TerminalField terminalField : rowFields) {
				ScreenUtils.placeContentOnBuffer(buffer, terminalField, screenSize);

				ScreenPosition fieldPosition = terminalField.getPosition();
				fieldsSeperators.add(new SimpleScreenPosition(fieldPosition.getRow(), fieldPosition.getColumn() - 1));
			}
		}
		String screenText = buffer.toString();

		return screenText;
	}

	public static String getText(String screenText, ScreenSize screenSize, ScreenPosition position, int length) {
		int beginIndex = ScreenUtils.toAbsolutePosition(position, screenSize);
		return screenText.substring(beginIndex, beginIndex + length);
	}

}
