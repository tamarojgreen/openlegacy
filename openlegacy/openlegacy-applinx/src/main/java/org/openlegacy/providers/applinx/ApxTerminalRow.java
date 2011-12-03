package org.openlegacy.providers.applinx;

import org.openlegacy.terminal.RowPart;
import org.openlegacy.terminal.TerminalField;
import org.openlegacy.terminal.TerminalRow;
import org.openlegacy.terminal.support.SnapshotUtils;
import org.openlegacy.terminal.utils.TerminalEqualsHashcodeUtil;

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

	public List<RowPart> getRowParts() {
		return SnapshotUtils.getRowParts(this);
	}

}
