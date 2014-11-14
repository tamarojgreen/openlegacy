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
package org.openlegacy.terminal.persistance;

import org.openlegacy.terminal.ScreenSize;
import org.openlegacy.terminal.TerminalField;
import org.openlegacy.terminal.TerminalOutgoingSnapshot;
import org.openlegacy.terminal.TerminalPosition;
import org.openlegacy.terminal.TerminalRow;
import org.openlegacy.terminal.TerminalSendAction;
import org.openlegacy.terminal.TerminalSnapshot;
import org.openlegacy.terminal.render.DefaultTerminalSnapshotTextRenderer;
import org.openlegacy.terminal.support.ScreenSizeBean;
import org.openlegacy.terminal.support.SimpleTerminalPosition;
import org.openlegacy.terminal.support.SimpleTerminalSendAction;
import org.openlegacy.terminal.support.SnapshotUtils;
import org.openlegacy.terminal.support.TerminalPositionBean;
import org.openlegacy.terminal.utils.FieldsQuery;
import org.openlegacy.terminal.utils.FieldsQuery.AllFieldsCriteria;
import org.openlegacy.terminal.utils.FieldsQuery.ModifiedFieldsCriteria;
import org.openlegacy.terminal.utils.TerminalEqualsHashcodeUtil;
import org.openlegacy.utils.BidiUtil;
import org.openlegacy.utils.FeatureChecker;
import org.openlegacy.utils.StringUtil;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "snapshot")
@XmlType
@XmlAccessorType(XmlAccessType.FIELD)
public class TerminalPersistedSnapshot implements TerminalOutgoingSnapshot {

	private static final long serialVersionUID = 1L;

	@XmlAttribute(name = "type")
	private SnapshotType snapshotType;

	@XmlElement(name = "row", type = TerminalPersistedRow.class)
	private List<TerminalRow> rows = new ArrayList<TerminalRow>();

	@XmlElement(name = "size", type = ScreenSizeBean.class)
	private ScreenSize size;

	@XmlElement(name = "cursor", type = TerminalPositionBean.class)
	private TerminalPosition cursorPosition;

	@XmlTransient
	private String screenText;

	@XmlTransient
	private ArrayList<TerminalPosition> fieldPositions;

	@XmlAttribute
	private String command;

	@XmlTransient
	private List<TerminalField> fields;

	@XmlAttribute
	private Integer sequence = 0;

	@XmlAttribute
	private Boolean rightToLeft = false;

	@Override
	public SnapshotType getSnapshotType() {
		return snapshotType;
	}

	public void setSnapshotType(SnapshotType snapshotType) {
		this.snapshotType = snapshotType;
	}

	@Override
	public List<TerminalRow> getRows() {
		// initialize row length (not persisted as part of row)
		for (TerminalRow terminalRow : rows) {
			((TerminalPersistedRow)terminalRow).setLength(getSize().getColumns());
		}
		return rows;
	}

	@Override
	public ScreenSize getSize() {
		return size;
	}

	public void setSize(ScreenSize size) {
		ScreenSizeBean tempSize = new ScreenSizeBean();
		tempSize.setRows(size.getRows());
		tempSize.setColumns(size.getColumns());
		this.size = tempSize;
	}

	@Override
	public List<TerminalPosition> getFieldSeperators() {
		initContent();
		return fieldPositions;
	}

	@Override
	public TerminalPosition getCursorPosition() {
		return cursorPosition;
	}

	public void setCursorPosition(TerminalPosition cursorPosition) {
		if (cursorPosition == null) {
			return;
		}

		// use an XML serialization position
		TerminalPositionBean newCursorPosition = new TerminalPositionBean();
		newCursorPosition.setRow(cursorPosition.getRow());
		newCursorPosition.setColumn(cursorPosition.getColumn());
		this.cursorPosition = newCursorPosition;
	}

	@Override
	public List<TerminalField> getFields() {
		if (fields == null) {
			fields = FieldsQuery.queryFields(getRows(), AllFieldsCriteria.instance());
		}
		return fields;
	}

	@Override
	public TerminalField getField(TerminalPosition position) {
		return SnapshotUtils.getField(this, position);
	}

	@Override
	public Object getDelegate() {
		return null;
	}

	@Override
	public String getText() {
		initContent();
		return screenText;
	}

	private void initContent() {
		if (screenText == null) {
			fieldPositions = new ArrayList<TerminalPosition>();
			screenText = SnapshotUtils.initSnapshot(getRows(), getSize(), fieldPositions);
		}
	}

	@Override
	public String getText(TerminalPosition position, int length) {
		initContent();
		return SnapshotUtils.getText(screenText, getSize(), position, length);
	}

	@Override
	public TerminalRow getRow(int rowNumber) {
		initContent();
		// persisted snapshot rows may not be fully populated
		for (TerminalRow terminalRow : rows) {
			if (terminalRow.getRowNumber() == rowNumber) {
				return terminalRow;
			}
		}
		TerminalPersistedRow row = new TerminalPersistedRow();
		row.setRowNumber(rowNumber);
		return row;
	}

	@Override
	public Integer getSequence() {
		return sequence;
	}

	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}

	@Override
	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	@Override
	public TerminalSendAction getTerminalSendAction() {
		if (getSnapshotType() != SnapshotType.OUTGOING) {
			return null;
		}

		List<TerminalField> modifiedFields = FieldsQuery.queryFields(this, ModifiedFieldsCriteria.instance());

		SimpleTerminalSendAction sendAction = new SimpleTerminalSendAction(getCommand());
		sendAction.setCursorPosition(getCursorPosition());
		sendAction.getFields().addAll(modifiedFields);
		return sendAction;
	}

	@Override
	public String toString() {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DefaultTerminalSnapshotTextRenderer.instance().render(this, baos);
		return StringUtil.toString(baos);
	}

	@Override
	public int hashCode() {
		return TerminalEqualsHashcodeUtil.snapshotHashcode(this);
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof TerminalSnapshot)) {
			return false;
		}
		return TerminalEqualsHashcodeUtil.snapshotsEquals(this, (TerminalSnapshot)obj);
	}

	@Override
	public TerminalField getField(int row, int column) {
		return getField(SimpleTerminalPosition.newInstance(row, column));
	}

	@Override
	public String getLogicalText(TerminalPosition position, int length) {
		String text = getText(position, length);
		if (FeatureChecker.isSupportBidi()) {
			text = BidiUtil.convertToLogical(text, false);
		}
		return text;
	}

	@Override
	public boolean isRightToLeft() {
		if (rightToLeft == null) {
			return false;
		}
		return rightToLeft;
	}

	public void setRightToLeft(boolean rightToLeft) {
		this.rightToLeft = rightToLeft;
	}
}
