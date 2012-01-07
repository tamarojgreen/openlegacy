package org.openlegacy.terminal.support;

import org.openlegacy.terminal.RowPart;
import org.openlegacy.terminal.TerminalField;
import org.openlegacy.terminal.TerminalRow;
import org.openlegacy.terminal.utils.TerminalEqualsHashcodeUtil;

import java.util.List;

public abstract class AbstractTerminalRow implements TerminalRow {

	protected String getText(int rowLength) {
		StringBuilder rowContent = SnapshotUtils.initEmptyBuffer(rowLength);
		List<TerminalField> fields = getFields();
		for (TerminalField terminalField : fields) {
			int startPosition = terminalField.getPosition().getColumn();
			SnapshotUtils.placeContentOnBuffer(rowContent, startPosition - 1, terminalField.getValue());
		}
		String value = rowContent.toString();
		return value;
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

	@Override
	public String toString() {
		return SnapshotUtils.rowToString(this);
	}

	public List<RowPart> getRowParts() {
		return SnapshotUtils.getRowParts(this);
	}

	public String getText(int column, int length) {
		return getText().substring(column - 1, column + length - 1);
	}
}
