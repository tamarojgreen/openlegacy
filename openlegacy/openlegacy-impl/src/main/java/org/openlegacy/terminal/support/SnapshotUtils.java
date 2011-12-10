package org.openlegacy.terminal.support;

import org.openlegacy.terminal.RowPart;
import org.openlegacy.terminal.ScreenSize;
import org.openlegacy.terminal.TerminalField;
import org.openlegacy.terminal.TerminalPosition;
import org.openlegacy.terminal.TerminalRow;
import org.openlegacy.terminal.TerminalSnapshot;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SnapshotUtils {

	public static int toAbsolutePosition(TerminalPosition position, ScreenSize screenSize) {
		int rowStart = (position.getRow() - 1) * screenSize.getColumns();
		return rowStart + position.getColumn() - 1;
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

		String value = terminalField.getValue();
		if (value.length() == 0) {
			return;
		}
		placeContentOnBuffer(buffer, beginIndex, value);
	}

	public static void placeContentOnBuffer(StringBuilder buffer, int beginIndex, String value) {
		for (int i = 0; i < value.length(); i++) {
			buffer.setCharAt(beginIndex + i, value.charAt(i));
		}
	}

	public static TerminalField getField(TerminalSnapshot snapshot, TerminalPosition position) {
		TerminalRow row = snapshot.getRow(position.getRow());
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
	public static String initSnapshot(List<TerminalRow> rows, ScreenSize screenSize, List<TerminalPosition> fieldsSeperators) {

		StringBuilder buffer = SnapshotUtils.initEmptyBuffer(screenSize);

		for (TerminalRow terminalRow : rows) {
			List<TerminalField> rowFields = terminalRow.getFields();
			for (TerminalField terminalField : rowFields) {
				SnapshotUtils.placeContentOnBuffer(buffer, terminalField, screenSize);

				TerminalPosition fieldPosition = terminalField.getPosition();
				TerminalPosition beforeStartPosition = new SimpleTerminalPosition(fieldPosition.getRow(),
						fieldPosition.getColumn() - 1);
				fieldsSeperators.add(beforeStartPosition);

				TerminalPosition afterEndPosition = new SimpleTerminalPosition(fieldPosition.getRow(), fieldPosition.getColumn()
						+ terminalField.getLength());
				// check if the position right after the field is empty, and not exceed right bound, is so add a field separator
				if (terminalRow.getField(afterEndPosition.getColumn()) == null
						&& afterEndPosition.getColumn() < screenSize.getColumns()) {
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

	public static int comparePositions(TerminalPosition position1, TerminalPosition position2) {
		if (position1 == null || position2 == null) {
			return 0;
		}

		if (position1.getRow() != position2.getRow()) {
			return position1.getRow() - position2.getRow();
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
		TerminalPosition position = field.getPosition();
		return SimpleTerminalPosition.newInstance(position.getRow(), position.getColumn() + field.getLength() - 1);
	}

}
