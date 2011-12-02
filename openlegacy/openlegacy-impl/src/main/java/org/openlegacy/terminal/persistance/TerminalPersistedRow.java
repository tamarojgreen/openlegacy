package org.openlegacy.terminal.persistance;

import org.openlegacy.terminal.TerminalField;
import org.openlegacy.terminal.TerminalRow;
import org.openlegacy.terminal.support.ScreenUtils;
import org.openlegacy.terminal.utils.TerminalEqualsHashcodeUtil;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlType
@XmlAccessorType(XmlAccessType.FIELD)
public class TerminalPersistedRow implements TerminalRow {

	@XmlAttribute
	private int rowNumber;

	@XmlElement(name = "field", type = TerminalPersistedField.class)
	private List<TerminalField> fields = new ArrayList<TerminalField>();

	public List<TerminalField> getFields() {
		initFieldsRow();
		return fields;
	}

	public TerminalField getField(int column) {
		initFieldsRow();
		for (TerminalField field : fields) {
			int startColumn = field.getPosition().getColumn();
			int endColumn = startColumn + field.getLength() - 1;
			if (startColumn <= column && endColumn >= column) {
				return field;
			}
		}
		return null;
	}

	public int getRowNumber() {
		return rowNumber;
	}

	public void setRowNumber(int rowNumber) {
		this.rowNumber = rowNumber;
	}

	/**
	 * To avoid replication between field row and it's parent row, TerminalPersistentField is not serializing row to XML. This
	 * function purpose is to fix the row of the field according to it's TerminalRow container
	 */
	private void initFieldsRow() {
		// row was initialize
		if (fields.size() > 0 && fields.get(0).getPosition().getRow() > 0) {
			return;
		}
		// position row wasn't initialized, set with rowNumber
		for (TerminalField field : fields) {
			((TerminalPersistedField)field).setRow(rowNumber);
		}

	}

	@Override
	public boolean equals(Object obj) {
		initFieldsRow();
		if (!(obj instanceof TerminalRow)) {
			return false;
		}
		TerminalRow otherRow = (TerminalRow)obj;
		return TerminalEqualsHashcodeUtil.rowEquals(this, otherRow);
	}

	@Override
	public int hashCode() {
		initFieldsRow();
		return TerminalEqualsHashcodeUtil.rowHashCode(this);
	}

	@Override
	public String toString() {
		initFieldsRow();
		return ScreenUtils.rowToString(this);
	}
}
