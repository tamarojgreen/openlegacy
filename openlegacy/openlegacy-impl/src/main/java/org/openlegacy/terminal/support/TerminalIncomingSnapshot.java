package org.openlegacy.terminal.support;

import org.openlegacy.terminal.ScreenPosition;
import org.openlegacy.terminal.ScreenSize;
import org.openlegacy.terminal.TerminalRow;
import org.openlegacy.terminal.TerminalScreen;
import org.openlegacy.terminal.TerminalSnapshot;
import org.openlegacy.terminal.utils.TerminalEqualsHashcodeUtil;

import java.io.Serializable;
import java.util.List;

public class TerminalIncomingSnapshot implements TerminalSnapshot, Serializable {

	private static final long serialVersionUID = 1L;

	private TerminalScreen terminalScreen;

	public TerminalIncomingSnapshot(TerminalScreen terminalScreen) {
		this.terminalScreen = terminalScreen;
	}

	public SnapshotType getSnapshotType() {
		return SnapshotType.INCOMING;
	}

	public TerminalScreen getTerminalScreen() {
		return terminalScreen;
	}

	public ScreenSize getSize() {
		return terminalScreen.getSize();
	}

	public List<TerminalRow> getRows() {
		return terminalScreen.getRows();
	}

	public List<ScreenPosition> getFieldSeperators() {
		return terminalScreen.getFieldSeperators();
	}

	public ScreenPosition getCursorPosition() {
		return terminalScreen.getCursorPosition();
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

}
