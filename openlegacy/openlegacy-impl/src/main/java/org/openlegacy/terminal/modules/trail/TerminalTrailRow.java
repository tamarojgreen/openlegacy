package org.openlegacy.terminal.modules.trail;

import org.openlegacy.terminal.TerminalField;
import org.openlegacy.terminal.TerminalRow;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlType
@XmlAccessorType(XmlAccessType.FIELD)
public class TerminalTrailRow implements TerminalRow {

	@XmlAttribute
	private int rowNumber;

	@XmlElement(name = "field", type = TerminalTrailField.class)
	private List<TerminalField> fields = new ArrayList<TerminalField>();

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List getFields() {
		return fields;
	}

	public TerminalField getField(int column) {
		for (TerminalField field : fields) {
			if (field.getPosition().getColumn() == column) {
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
}
