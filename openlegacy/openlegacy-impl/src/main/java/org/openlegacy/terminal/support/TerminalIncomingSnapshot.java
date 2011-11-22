package org.openlegacy.terminal.support;

import org.openlegacy.terminal.ScreenPosition;
import org.openlegacy.terminal.ScreenSize;
import org.openlegacy.terminal.TerminalField;
import org.openlegacy.terminal.TerminalRow;
import org.openlegacy.terminal.TerminalSnapshot;
import org.openlegacy.terminal.utils.TerminalEqualsHashcodeUtil;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

public class TerminalIncomingSnapshot implements TerminalSnapshot, Serializable {

	private static final long serialVersionUID = 1L;

	private TerminalSnapshot terminalSnapshot;

	public TerminalIncomingSnapshot(TerminalSnapshot terminalSnapshot) {
		this.terminalSnapshot = terminalSnapshot;
	}

	public SnapshotType getSnapshotType() {
		return SnapshotType.INCOMING;
	}

	public TerminalSnapshot getTerminalSnapshot() {
		return terminalSnapshot;
	}

	public ScreenSize getSize() {
		return terminalSnapshot.getSize();
	}

	public List<TerminalRow> getRows() {
		return terminalSnapshot.getRows();
	}

	public Collection<TerminalField> getFields() {
		return terminalSnapshot.getFields();
	}

	public List<ScreenPosition> getFieldSeperators() {
		return terminalSnapshot.getFieldSeperators();
	}

	public ScreenPosition getCursorPosition() {
		return terminalSnapshot.getCursorPosition();
	}

	@Override
	public int hashCode() {
		return TerminalEqualsHashcodeUtil.snapshotHashcode(this);
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof TerminalSnapshot)) {
			return false;
		}
		return TerminalEqualsHashcodeUtil.snapshotsEquals(this, (TerminalSnapshot)obj);
	}

	public TerminalField getField(ScreenPosition position) {
		return ScreenUtils.getField(terminalSnapshot.getRows(), position);
	}

	public Object getDelegate() {
		return terminalSnapshot.getDelegate();
	}

	public String getText() {
		return terminalSnapshot.getText();
	}

	public String getText(ScreenPosition position, int length) {
		return terminalSnapshot.getText(position, length);
	}

}
