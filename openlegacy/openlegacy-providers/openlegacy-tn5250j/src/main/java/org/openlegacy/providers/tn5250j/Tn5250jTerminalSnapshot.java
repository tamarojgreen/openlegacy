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
package org.openlegacy.providers.tn5250j;

import org.openlegacy.terminal.ScreenSize;
import org.openlegacy.terminal.TerminalField;
import org.openlegacy.terminal.TerminalPosition;
import org.openlegacy.terminal.persistance.TerminalPersistedSnapshot;
import org.openlegacy.terminal.support.AbstractSnapshot;
import org.openlegacy.terminal.support.SimpleScreenSize;
import org.openlegacy.terminal.support.SimpleTerminalPosition;
import org.openlegacy.terminal.support.SnapshotUtils;
import org.openlegacy.utils.BidiUtil;
import org.openlegacy.utils.StringUtil;
import org.tn5250j.TN5250jConstants;
import org.tn5250j.framework.tn5250.Screen5250;
import org.tn5250j.framework.tn5250.ScreenField;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Snapshot implementation for TN5250J connector
 * 
 * Originally based on Servlet5250 from web5250.com
 * 
 * @author Roi Mor
 * 
 */
public class Tn5250jTerminalSnapshot extends AbstractSnapshot {

	private static final long serialVersionUID = 1L;

	private Screen5250 screen;

	private ScreenDataContainer screenData;

	private List<TerminalPosition> fieldSeperators;

	private int sequence;

	private boolean convertToLogical;

	/*
	 * for serialization purpose only
	 */
	public Tn5250jTerminalSnapshot() {}

	public Tn5250jTerminalSnapshot(Screen5250 screen, int sequence, boolean convertToLogical) {
		this.screen = screen;
		this.sequence = sequence;
		this.convertToLogical = convertToLogical;
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
			boolean lastPartAdded = false;
			if (currentPosition == null) {
				currentPosition = terminalPosition;
			} else if (terminalPosition.getRow() != previousPosition.getRow()) {
				TerminalPosition tempPosition = currentPosition;
				currentPosition = new SimpleTerminalPosition(previousPosition.getRow(), getSize().getColumns());
				previousPosition = tempPosition;
				// add field from previous to end of line
				if (previousPosition.getColumn() < getSize().getColumns()) {
					addField(fields, previousPosition, currentPosition, true, false);
					lastPartAdded = true;
				}

				// fill empty rows
				for (int currentRow = previousPosition.getRow() + 1; currentRow < terminalPosition.getRow(); currentRow++) {
					addField(fields, new SimpleTerminalPosition(currentRow, 1), new SimpleTerminalPosition(currentRow,
							getSize().getColumns()), false, false);
					lastPartAdded = false;
				}
				previousPosition = new SimpleTerminalPosition(terminalPosition.getRow(), 1);
				currentPosition = terminalPosition;
				previousIsAttributePosition = false;
				currentIsAttributePosition = true;
			} else {
				previousPosition = currentPosition;
				currentPosition = terminalPosition;
			}
			if (!lastPartAdded) {
				addField(fields, previousPosition, currentPosition, previousIsAttributePosition, currentIsAttributePosition);
			}
		}
		// fill last empty rows
		if (currentPosition != null) {
			for (int currentRow = currentPosition.getRow() + 1; currentRow <= getSize().getRows(); currentRow++) {
				addField(fields, new SimpleTerminalPosition(currentRow, 1), new SimpleTerminalPosition(currentRow,
						getSize().getColumns()), false, false);
			}
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
		value = StringUtil.nullsToSpaces(value);

		String visualValue = null;

		Tn5250jTerminalField field = null;
		boolean isEditable = screenData.field[startAbsolutePosition] != 0;

		if (convertToLogical == true) {
			visualValue = value;
			value = BidiUtil.convertToLogical(value, true);
		}

		boolean hidden = false;
		if ((screenData.extended[startAbsolutePosition] & TN5250jConstants.EXTENDED_5250_NON_DSP) != 0) {
			hidden = true;
		}
		if (isEditable) {
			int fieldAttributes = screenData.attr[startAbsolutePosition];
			ScreenField screenField = screen.getScreenFields().findByPosition(startAbsolutePosition);
			if (screenField != null) {
				if (screenField.isBypassField()) {
					if (screenData.attr[startAbsolutePosition] == 39) {
						hidden = true;
					}
					field = createReadOnlyField(value, startPosition.getRow(), startColumn, fieldAttributes, hidden);
				} else {
					// avoid creating duplicate fields from multy line field
					if (screenField.startRow() + 1 == startPosition.getRow()) {
						// re-grab the all field text
						value = grabText(screenData.text, startAbsolutePosition, startAbsolutePosition + screenField.getLength()
								- 1);
						if (convertToLogical == true) {
							visualValue = value;
							value = BidiUtil.convertToLogical(value, screenField.isRightToLeft());
						}
						field = createEditableField(screenField, value, hidden, fieldAttributes);
					}
				}
			}
		} else {
			int fieldAttributes = screenData.attr[startAbsolutePosition];
			field = createReadOnlyField(value, startPosition.getRow(), startColumn, fieldAttributes, hidden);
		}

		if (visualValue != null && !value.equals(visualValue) && field != null) {
			field.setVisualValue(visualValue);
			field.setLength(visualValue.length());
		}

		if (field != null) {
			fields.add(field);
		}
	}

	private String grabText(char[] text, int startPosition, int endPosition) {

		StringBuilder sb = new StringBuilder();
		for (int i = startPosition; i <= endPosition; i++) {
			char ch = text[i];
			if (ch == 0) {
				ch = ' ';
			}
			if (i > startPosition && i % getSize().getColumns() == 0) {
				sb.append('\n');
			} else {
				sb.append(ch);
			}
		}
		return StringUtil.nullsToSpaces(sb.toString());
	}

	private Tn5250jTerminalField createEditableField(ScreenField screenField, String value, boolean hidden, int fieldAttributes) {
		// copy the field value - tn5250j implementation may re-use ScreenField in other screen
		value = StringUtil.rightTrim(value);
		int length = screenField.getFieldLength();
		Tn5250jTerminalField field = new Tn5250jTerminalEditableField(screenField, length, fieldAttributes, value);
		if (field.getPosition().getColumn() + length > getSize().getColumns()) {
			// length = getSize().getColumns() - field.getPosition().getColumn() + 1;
		}
		field.setEndPosition(SnapshotUtils.moveBy(field.getPosition(), length - 1, getSize()));
		field.setHidden(hidden);
		return field;
	}

	private static Tn5250jTerminalField createReadOnlyField(String value, int row, int column, int fieldAttributes, boolean hidden) {
		Tn5250jTerminalField field = new Tn5250jTerminalField(value, new SimpleTerminalPosition(row, column), value.length(),
				fieldAttributes, hidden);
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
		parseFieldsBuffer();
		return fieldSeperators;
	}

	/**
	 * Special fix for case where field has mask (e.g date field), and has no attribute to seperate the field into multiple fields
	 */
	private void parseFieldsBuffer() {
		char[] chars;
		chars = screenData.field;
		// whether the current char is a start of an editable field. Null means no field. False mean editable but not 1st position
		Boolean firstEditableFieldPosition = null;
		for (int i = 0; i < chars.length; i++) {
			// unprotected
			if (chars[i] == 'C') {
				if (firstEditableFieldPosition == null) {
					firstEditableFieldPosition = true;
				}
				TerminalPosition newTerminalPosition = SnapshotUtils.newTerminalPosition(i - 1, getSize().getColumns());
				if (firstEditableFieldPosition == true) {
					if (!fieldSeperators.contains(newTerminalPosition)) {
						fieldSeperators.add(newTerminalPosition);
					}
					firstEditableFieldPosition = false;
				}
			} else {
				firstEditableFieldPosition = null;
			}
		}
		Collections.sort(fieldSeperators);
	}

	@Override
	public String initText() {
		init();

		String result = StringUtil.nullsToSpaces(screenData.text);
		return result;
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.openlegacy.terminal.support.AbstractSnapshot#readExternal(org.openlegacy.terminal.persistance.TerminalPersistedSnapshot
	 * )
	 */
	@Override
	protected void readExternal(TerminalPersistedSnapshot persistedSnapshot) {
		this.sequence = persistedSnapshot.getSequence();
		this.fieldSeperators = persistedSnapshot.getFieldSeperators();
	}

	@Override
	public String getLogicalText(TerminalPosition position, int length) {
		String text = super.getText(position, length);
		if (convertToLogical) {
			text = BidiUtil.convertToLogical(text, true);
		}
		return text;
	}

}
