package org.openlegacy.terminal.support;

import org.openlegacy.terminal.RowPart;
import org.openlegacy.terminal.TerminalRow;
import org.openlegacy.terminal.utils.TerminalEqualsHashcodeUtil;

import java.util.List;

public abstract class AbstractTerminalRow implements TerminalRow {

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
}
