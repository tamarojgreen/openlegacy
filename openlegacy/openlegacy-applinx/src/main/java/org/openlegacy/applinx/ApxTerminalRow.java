package org.openlegacy.applinx;

import org.openlegacy.terminal.TerminalField;
import org.openlegacy.terminal.TerminalRow;

import java.util.ArrayList;
import java.util.List;

public class ApxTerminalRow implements TerminalRow {

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
}
