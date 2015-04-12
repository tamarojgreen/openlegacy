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
package org.openlegacy.terminal.support;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.SystemUtils;
import org.openlegacy.terminal.RowPart;
import org.openlegacy.terminal.ScreenSize;
import org.openlegacy.terminal.TerminalField;
import org.openlegacy.terminal.TerminalPosition;
import org.openlegacy.terminal.TerminalRow;
import org.openlegacy.terminal.TerminalSnapshot;
import org.openlegacy.utils.BidiUtil;

import java.awt.Color;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SnapshotUtils {

	/**
	 * 0 based
	 */
	public static int toAbsolutePosition(TerminalPosition position, ScreenSize screenSize) {
		return toAbsolutePosition(position.getRow(), position.getColumn(), screenSize);
	}

	/**
	 * 0 based
	 */
	public static int toAbsolutePosition(int row, int column, ScreenSize screenSize) {
		int rowStart = (row - 1) * screenSize.getColumns();
		return rowStart + column - 1;
	}

	public static int toRow(int absolutePosition, int columns) {
		return absolutePosition / columns + 1;
	}

	public static int toColumn(int absolutePosition, int columns) {
		return absolutePosition % columns + 1;
	}

	public static TerminalPosition newTerminalPosition(int absolutePosition, int columns) {
		return new SimpleTerminalPosition(toRow(absolutePosition, columns), toColumn(absolutePosition, columns));
	}

	public static StringBuilder initEmptyBuffer(ScreenSize size) {
		int capacity = size.getRows() * size.getColumns();
		return initEmptyBuffer(capacity);
	}

	public static StringBuilder initEmptyBuffer(int capacity) {
		StringBuilder buffer = new StringBuilder(capacity);
		for (int i = 0; i < capacity; i++) {
			buffer.append(' ');
		}
		return buffer;
	}

	public static void placeContentOnBuffer(StringBuilder buffer, TerminalField terminalField, ScreenSize screenSize) {
		int beginIndex = SnapshotUtils.toAbsolutePosition(terminalField.getPosition(), screenSize);

		// use the visual content for buffer if available
		String value = terminalField.getVisualValue() != null ? terminalField.getVisualValue() : terminalField.getValue();
		if (StringUtils.isEmpty(value)) {
			return;
		}
		placeContentOnBuffer(buffer, beginIndex, value);
	}

	/**
	 * beginIndex is 0 based!
	 */
	public static void placeContentOnBuffer(StringBuilder buffer, int beginIndex, String value) {
		for (int i = 0; i < value.length(); i++) {
			buffer.setCharAt(beginIndex + i, value.charAt(i));
		}
	}

	public static TerminalField getField(TerminalSnapshot snapshot, TerminalPosition position) {
		if (position == null) {
			return null;
		}
		TerminalRow row = snapshot.getRow(position.getRow());
		if (row == null) {
			return null;
		}
		return row.getField(position.getColumn());
	}

	/**
	 * Populates a snapshot from the given rows and screen size. Builds the fields separators and screen text
	 * 
	 * @param rows
	 * @param screenSize
	 * @param fieldsSeperators
	 * @return screen text content
	 */
	public static String initSnapshot(List<TerminalRow> rows, ScreenSize screenSize, List<TerminalPosition> fieldsSeperators) {

		StringBuilder buffer = SnapshotUtils.initEmptyBuffer(screenSize);

		for (TerminalRow terminalRow : rows) {
			List<TerminalField> rowFields = terminalRow.getFields();
			for (TerminalField terminalField : rowFields) {
				placeContentOnBuffer(buffer, terminalField, screenSize);

				TerminalPosition fieldPosition = terminalField.getPosition();
				TerminalPosition beforeStartPosition = new SimpleTerminalPosition(fieldPosition.getRow(),
						fieldPosition.getColumn() - 1);
				if (beforeStartPosition.getColumn() > 0 && !fieldsSeperators.contains(beforeStartPosition)) {
					fieldsSeperators.add(beforeStartPosition);
				}

				TerminalPosition afterEndPosition = new SimpleTerminalPosition(fieldPosition.getRow(), fieldPosition.getColumn()
						+ terminalField.getLength() - 1);
				// check if the position right after the field is empty, and not exceed right bound, is so add a field separator
				if (terminalRow.getField(afterEndPosition.getColumn()) == null
						&& afterEndPosition.getColumn() < screenSize.getColumns() && afterEndPosition.getColumn() > 0
						&& !fieldsSeperators.contains(afterEndPosition)) {
					fieldsSeperators.add(afterEndPosition);
				}
			}
		}
		String screenText = buffer.toString();

		return screenText;
	}

	public static String getText(String screenText, ScreenSize screenSize, TerminalPosition position, int length) {
		int beginIndex = SnapshotUtils.toAbsolutePosition(position, screenSize);
		return screenText.substring(beginIndex, beginIndex + length);
	}

	public static List<RowPart> getRowParts(TerminalRow row) {

		Collection<TerminalField> fields = row.getFields();
		List<RowPart> rowParts = new ArrayList<RowPart>();
		SimpleRowPart rowPart = null;
		for (TerminalField terminalField : fields) {
			if (rowPart == null) {
				rowPart = new SimpleRowPart(terminalField);
			} else {
				if (terminalField.isEditable() != rowPart.isEditable()) {
					rowParts.add(rowPart);
					rowPart = new SimpleRowPart(terminalField);
				} else {
					rowPart.appendField(terminalField);
				}
			}

		}
		rowParts.add(rowPart);
		return rowParts;
	}

	public static String fieldToString(TerminalField terminalField) {
		return positionTextToString(terminalField.getPosition(), terminalField.getValue());
	}

	public static String rowToString(TerminalRow terminalRow) {
		return MessageFormat.format("Row number:{0}, Fields:{1}", terminalRow.getRowNumber(), terminalRow.getFields());
	}

	public static String positionTextToString(TerminalPosition position, String text) {
		return MessageFormat.format("{0}:{1}", position, text);
	}

	public static int comparePositions(TerminalPosition position1, TerminalPosition position2, boolean rightToLeft) {
		if (position1 == null || position2 == null) {
			return 0;
		}

		if (position1.getRow() != position2.getRow()) {
			return position1.getRow() - position2.getRow();
		}
		if (rightToLeft) {
			return position2.getColumn() - position1.getColumn();

		}
		return position1.getColumn() - position2.getColumn();
	}

	public static TerminalField getField(TerminalRow row, int column) {
		for (TerminalField field : row.getFields()) {
			int startColumn = field.getPosition().getColumn();
			int endColumn = startColumn + field.getLength() - 1;
			if (startColumn <= column && endColumn >= column) {
				return field;
			}
		}
		return null;
	}

	public static TerminalPosition getEndPosition(TerminalField field) {
		return getEndPosition(field.getPosition(), field.getLength());
	}

	public static TerminalPosition getEndPosition(TerminalPosition position, int length) {
		return SimpleTerminalPosition.newInstance(position.getRow(), position.getColumn() + length - 1);
	}

	public static String getText(TerminalSnapshot snapshot, TerminalPosition position, int length) {
		int beginIndex = ((position.getRow() - 1) * snapshot.getSize().getColumns()) + (position.getColumn() - 1);
		String result = snapshot.getText().substring(beginIndex, beginIndex + length);
		return result;
	}

	public static String getRowText(TerminalRow row) {
		if (row.getFields().size() == 0) {
			return null;
		}
		TerminalField lastField = row.getFields().get(row.getFields().size() - 1);
		int lastColumn = lastField.getPosition().getColumn() + lastField.getLength() - 1;
		StringBuilder rowContent = SnapshotUtils.initEmptyBuffer(lastColumn);
		List<TerminalField> fields = row.getFields();
		for (TerminalField terminalField : fields) {
			int startPosition = terminalField.getPosition().getColumn();
			String value = terminalField.getVisualValue() != null ? terminalField.getVisualValue() : terminalField.getValue();
			SnapshotUtils.placeContentOnBuffer(rowContent, startPosition - 1, value);
		}
		String value = rowContent.toString();
		return value;
	}

	public static String getRowText(TerminalRow row, int column, int length) {
		if (row.getText() == null) {
			return "";
		}
		String text = row.getText().substring(column - 1, column + length - 1);
		boolean isVisual = false;
		List<TerminalField> fields = row.getFields();
		for (TerminalField terminalField : fields) {
			if (terminalField.getVisualValue() != null) {
				isVisual = true;
				break;
			}
		}
		if (isVisual) {
			text = BidiUtil.convertToLogical(text, true);
		}
		return text;
	}

	public static Color convertColor(org.openlegacy.terminal.Color color) {
		if (color == null) {
			return Color.GREEN;
		}
		switch (color) {
			case BLUE:
				return new Color(0x0586F7);
			case RED:
			case LIGHT_RED:
				return Color.RED;
			case LIGHT_WHITE:
				return Color.WHITE;
			default:
				return Color.GREEN;
		}
	}

	public static TerminalPosition moveBy(TerminalPosition terminalPosition, int columns, ScreenSize screenSize) {
		int column = (terminalPosition.getColumn() + columns) % screenSize.getColumns();
		int extraRows = (terminalPosition.getColumn() + columns) / screenSize.getColumns();
		int row = terminalPosition.getRow() + extraRows;
		if (row > screenSize.getRows()) {
			throw (new IllegalStateException(MessageFormat.format("Row {0} exceeds screen size {1}", row, screenSize)));
		}
		if (column > screenSize.getColumns()) {
			throw (new IllegalStateException(MessageFormat.format("Column {0} exceeds screen size {1}", column, screenSize)));
		}
		return SimpleTerminalPosition.newInstance(row, column);
	}

	public static boolean contains(ScreenSize screenSize, TerminalPosition terminalPosition) {
		if (terminalPosition.getRow() < 1) {
			return false;
		}
		if (terminalPosition.getColumn() < 1) {
			return false;
		}

		if (screenSize.getRows() < terminalPosition.getRow()) {
			return false;
		}
		if (screenSize.getColumns() < terminalPosition.getColumn()) {
			return false;
		}
		return true;
	}

	public static TerminalPosition toPosition(int absolutePosition, int columns) {
		return new SimpleTerminalPosition(toRow(absolutePosition, columns), toColumn(absolutePosition, columns));
	}

	public static String getRenderedText(TerminalSnapshot snapshot, TerminalPosition startPosition, TerminalPosition endPosition) {
		int beginIndex = ((startPosition.getRow() - 1) * snapshot.getSize().getColumns()) + (startPosition.getColumn() - 1);
		int rows = endPosition.getRow() - startPosition.getRow() + 1;
		int length = endPosition.getColumn() - startPosition.getColumn() + 1;
		StringBuilder sb = new StringBuilder();
		String snapshotText = snapshot.getText();
		if (StringUtils.isEmpty(snapshotText)) {
			return sb.toString();
		}
		for (int i = 0; i < rows; i++) {
			if ((snapshotText.length() < beginIndex) || (snapshotText.length() < (beginIndex + length))) {
				return sb.toString();
			}
			String text = snapshotText.substring(beginIndex, beginIndex + length);
			sb.append(text);
			if ((i + 1) < rows) {
				sb.append(SystemUtils.LINE_SEPARATOR);
			}
			beginIndex += snapshot.getSize().getColumns();
		}
		return sb.toString();
	}
}
