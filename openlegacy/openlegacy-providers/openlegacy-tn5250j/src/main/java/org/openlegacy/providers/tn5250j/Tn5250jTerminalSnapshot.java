package org.openlegacy.providers.tn5250j;

import org.openlegacy.terminal.ScreenSize;
import org.openlegacy.terminal.TerminalField;
import org.openlegacy.terminal.TerminalPosition;
import org.openlegacy.terminal.support.AbstractSnapshot;
import org.openlegacy.terminal.support.SimpleScreenSize;
import org.openlegacy.terminal.support.SimpleTerminalPosition;
import org.openlegacy.terminal.support.SnapshotUtils;
import org.tn5250j.TN5250jConstants;
import org.tn5250j.framework.tn5250.Screen5250;
import org.tn5250j.framework.tn5250.ScreenField;

import java.util.ArrayList;
import java.util.List;

public class Tn5250jTerminalSnapshot extends AbstractSnapshot {

	private static final long serialVersionUID = 1L;

	private Screen5250 screen;

	private ScreenDataContainer screenData;

	private List<TerminalPosition> fieldSeperators;

	private int sequence;

	public Tn5250jTerminalSnapshot(Screen5250 screen, int sequence) {
		this.screen = screen;
		this.sequence = sequence;
	}

	@Override
	protected TerminalPosition initCursorPosition() {
		return new SimpleTerminalPosition(screen.getCurrentRow(), screen.getCurrentCol());
	}

	@Override
	protected ScreenSize initScreenSize() {
		return new SimpleScreenSize(screen.getRows(), screen.getColumns());
	}

	@Override
	protected List<TerminalField> initFields() {

		List<TerminalField> fields = new ArrayList<TerminalField>();

		List<TerminalPosition> fieldSeperators = getFieldSeperators();
		TerminalPosition currentPosition = null;
		TerminalPosition previousPosition = new SimpleTerminalPosition(1, 1); // assume 1,1 always has attribute

		for (TerminalPosition terminalPosition : fieldSeperators) {
			boolean previousIsAttributePosition = true;
			boolean currentIsAttributePosition = true;
			if (currentPosition == null) {
				currentPosition = terminalPosition;
			} else if (terminalPosition.getRow() != previousPosition.getRow()) {
				TerminalPosition tempPosition = currentPosition;
				currentPosition = new SimpleTerminalPosition(previousPosition.getRow(), getSize().getColumns());
				previousPosition = tempPosition;
				// add field from previous to end of line
				if (previousPosition.getColumn() < getSize().getColumns()) {
					addField(fields, previousPosition, currentPosition, true, false);
				}

				previousPosition = new SimpleTerminalPosition(terminalPosition.getRow(), 1);
				currentPosition = terminalPosition;
				previousIsAttributePosition = false;
				currentIsAttributePosition = true;
			} else {
				previousPosition = currentPosition;
				currentPosition = terminalPosition;
			}
			addField(fields, previousPosition, currentPosition, previousIsAttributePosition, currentIsAttributePosition);
		}

		return fields;
	}

	private void addField(List<TerminalField> fields, TerminalPosition startPosition, TerminalPosition endPosition,
			boolean startIsAttributePosition, boolean endIsAttributePosition) {
		int endAbsolutePosition = SnapshotUtils.toAbsolutePosition(endPosition, getSize()); // 1 based
		int startAbsolutePosition = SnapshotUtils.toAbsolutePosition(startPosition, getSize()); // 1 based
		int startColumn = startPosition.getColumn();
		if (startIsAttributePosition) {
			startAbsolutePosition++;
			startColumn++;
		}
		if (endIsAttributePosition) {
			endAbsolutePosition--;
		}
		String value = grabText(screenData.text, startAbsolutePosition, endAbsolutePosition);

		Tn5250jTerminalField field = null;
		if (screenData.field[startAbsolutePosition] != 0) {
			int fieldAttributes = screenData.attr[startAbsolutePosition];
			ScreenField screenField = screen.getScreenFields().findByPosition(startAbsolutePosition);
			if (screenField != null) {
				field = createEditableField(screenField, value, false, fieldAttributes);
			}
		} else {
			int fieldAttributes = screenData.attr[startAbsolutePosition];
			field = createReadOnlyField(value, startPosition.getRow(), startColumn, fieldAttributes);
		}
		if (field != null) {
			fields.add(field);
		}
	}

	private static String grabText(char[] text, int startPosition, int endPosition) {

		StringBuilder sb = new StringBuilder();
		for (int i = startPosition; i <= endPosition; i++) {
			sb.append(text[i]);
		}
		return sb.toString();
	}

	private static Tn5250jTerminalField createEditableField(ScreenField screenField, String value, boolean hidden,
			int fieldAttributes) {
		Tn5250jTerminalField field = new Tn5250jTerminalEditableField(screenField, fieldAttributes);
		field.setHidden(hidden);
		return field;
	}

	private static Tn5250jTerminalField createReadOnlyField(String value, int row, int column, int fieldAttributes) {
		Tn5250jTerminalField field = new Tn5250jTerminalField(value, new SimpleTerminalPosition(row, column), value.length(),
				fieldAttributes);
		return field;
	}

	@Override
	public List<TerminalPosition> initFieldSeperators() {
		init();

		if (fieldSeperators != null) {
			return fieldSeperators;
		}

		fieldSeperators = new ArrayList<TerminalPosition>();
		char[] chars = screenData.isAttr;
		for (int i = 0; i < chars.length; i++) {
			if (chars[i] != 0) {
				fieldSeperators.add(SnapshotUtils.newTerminalPosition(i, getSize().getColumns()));
			}
		}
		return fieldSeperators;
	}

	@Override
	public String initText() {
		init();
		return new String(screenData.text);
	}

	private void init() {
		if (screenData == null) {
			screenData = new ScreenDataContainer(1, 1, getSize().getRows(), getSize().getColumns(), screen);
		}
	}

	public Object getDelegate() {
		return screen;
	}

	public Integer getSequence() {
		return sequence;
	}

	public String getCommand() {
		return null;
	}

	private class ScreenDataContainer implements TN5250jConstants {

		public ScreenDataContainer(int startRow, int startCol, int endRow, int endCol, Screen5250 screen) {

			int size = ((endCol - startCol) + 1) * ((endRow - startRow) + 1);

			text = new char[size];
			attr = new char[size];
			isAttr = new char[size];
			color = new char[size];
			extended = new char[size];
			graphic = new char[size];
			field = new char[size];

			if (size == screen.getScreenLength()) {
				screen.GetScreen(text, size, PLANE_TEXT);
				screen.GetScreen(attr, size, PLANE_ATTR);
				screen.GetScreen(isAttr, size, PLANE_IS_ATTR_PLACE);
				screen.GetScreen(color, size, PLANE_COLOR);
				screen.GetScreen(extended, size, PLANE_EXTENDED);
				screen.GetScreen(graphic, size, PLANE_EXTENDED_GRAPHIC);
				screen.GetScreen(field, size, PLANE_FIELD);
			} else {
				screen.GetScreenRect(text, size, startRow, startCol, endRow, endCol, PLANE_TEXT);
				screen.GetScreenRect(attr, size, startRow, startCol, endRow, endCol, PLANE_ATTR);
				screen.GetScreenRect(isAttr, size, startRow, startCol, endRow, endCol, PLANE_IS_ATTR_PLACE);
				screen.GetScreenRect(color, size, startRow, startCol, endRow, endCol, PLANE_COLOR);
				screen.GetScreenRect(extended, size, startRow, startCol, endRow, endCol, PLANE_EXTENDED);
				screen.GetScreenRect(graphic, size, startRow, startCol, endRow, endCol, PLANE_EXTENDED_GRAPHIC);
				screen.GetScreenRect(field, size, startRow, startCol, endRow, endCol, PLANE_FIELD);
			}
		}

		public char[] text;
		public char[] attr;
		public char[] isAttr;
		public char[] color;
		public char[] extended;
		public final char[] graphic;
		public final char[] field;
	}

}
