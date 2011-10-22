package org.openlegacy.adapter.terminal.trail;

import org.openlegacy.terminal.TerminalField;
import org.openlegacy.terminal.TerminalRow;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;

public class TerminalTrailRow implements TerminalRow {

	private int rowNumber;

	@XmlElement(name = "field", type = TerminalTrailField.class)
	private List<TerminalField> fields = new ArrayList<TerminalField>();

	public List<TerminalField> getFields() {
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
