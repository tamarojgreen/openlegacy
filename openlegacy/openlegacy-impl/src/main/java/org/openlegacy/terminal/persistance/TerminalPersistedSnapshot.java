package org.openlegacy.terminal.persistance;

import org.openlegacy.terminal.ScreenPosition;
import org.openlegacy.terminal.ScreenSize;
import org.openlegacy.terminal.TerminalField;
import org.openlegacy.terminal.TerminalRow;
import org.openlegacy.terminal.support.AbstractSnapshot;
import org.openlegacy.terminal.support.ScreenPositionBean;
import org.openlegacy.terminal.support.ScreenSizeBean;
import org.openlegacy.terminal.support.SnapshotUtils;
import org.openlegacy.terminal.utils.FieldsQuery;
import org.openlegacy.terminal.utils.FieldsQuery.AllFieldsCriteria;

import java.util.ArrayList;
import java.util.Collection;
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
public class TerminalPersistedSnapshot extends AbstractSnapshot {

	@XmlAttribute(name = "type")
	private SnapshotType snapshotType;

	@XmlElement(name = "row", type = TerminalPersistedRow.class)
	private List<TerminalRow> rows = new ArrayList<TerminalRow>();

	@XmlElement(name = "size", type = ScreenSizeBean.class)
	private ScreenSize size;

	@XmlElement(name = "cursor", type = ScreenPositionBean.class)
	private ScreenPosition cursorPosition;

	@XmlTransient
	private String screenText;

	@XmlTransient
	private ArrayList<ScreenPosition> fieldPositions;

	@XmlTransient
	private List<TerminalField> fields;

	public SnapshotType getSnapshotType() {
		return snapshotType;
	}

	public void setSnapshotType(SnapshotType snapshotType) {
		this.snapshotType = snapshotType;
	}

	public List<TerminalRow> getRows() {
		return rows;
	}

	public ScreenSize getSize() {
		return size;
	}

	public void setSize(ScreenSize size) {
		ScreenSizeBean tempSize = new ScreenSizeBean();
		tempSize.setRows(size.getRows());
		tempSize.setColumns(size.getColumns());
		this.size = tempSize;
	}

	public List<ScreenPosition> getFieldSeperators() {
		initContent();
		return fieldPositions;
	}

	public ScreenPosition getCursorPosition() {
		return cursorPosition;
	}

	public void setCursorPosition(ScreenPosition cursorPosition) {
		if (cursorPosition == null) {
			return;
		}

		// use an XML serialization ScreenPosition
		ScreenPositionBean newCursorPosition = new ScreenPositionBean();
		newCursorPosition.setRow(cursorPosition.getRow());
		newCursorPosition.setColumn(cursorPosition.getColumn());
		this.cursorPosition = newCursorPosition;
	}

	public Collection<TerminalField> getFields() {
		if (fields == null) {
			fields = FieldsQuery.queryFields(getRows(), AllFieldsCriteria.instance());
		}
		return fields;
	}

	public TerminalField getField(ScreenPosition position) {
		return SnapshotUtils.getField(this, position);
	}

	public Object getDelegate() {
		return null;
	}

	public String getText() {
		initContent();
		return screenText;
	}

	private void initContent() {
		if (screenText == null) {
			fieldPositions = new ArrayList<ScreenPosition>();
			screenText = SnapshotUtils.initSnapshot(getRows(), getSize(), fieldPositions);
		}
	}

	public String getText(ScreenPosition position, int length) {
		initContent();
		return SnapshotUtils.getText(screenText, getSize(), position, length);
	}

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
}
