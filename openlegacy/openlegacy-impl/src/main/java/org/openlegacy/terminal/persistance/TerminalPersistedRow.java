package org.openlegacy.terminal.persistance;

import org.openlegacy.terminal.TerminalField;
import org.openlegacy.terminal.TerminalRow;
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
		return fields;
	}

	public TerminalField getField(int column) {
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

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof TerminalRow)) {
			return false;
		}
		TerminalRow otherRow = (TerminalRow)obj;
		return TerminalEqualsHashcodeUtil.rowEquals(this, otherRow);
	}

	@Override
	public int hashCode() {
		return TerminalEqualsHashcodeUtil.rowHashCode(this);
	}

}
