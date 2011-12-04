package org.openlegacy.providers.applinx;

import org.openlegacy.terminal.TerminalField;
import org.openlegacy.terminal.support.AbstractTerminalRow;

import java.util.ArrayList;
import java.util.List;

public class ApxTerminalRow extends AbstractTerminalRow {

	private int rowNumber;
	private List<TerminalField> fields = new ArrayList<TerminalField>();

	public ApxTerminalRow(int rowNumber) {
		this.rowNumber = rowNumber;
	}

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

	/**
	 * Row text is calculated by the row length. Assuming row is fully populated with fields
	 */
	public String getText() {
		TerminalField lastField = fields.get(fields.size() - 1);
		String value = getText(lastField.getPosition().getColumn() + lastField.getLength() + 1);
		return value;
	}
}
