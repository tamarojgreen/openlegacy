package org.openlegacy.terminal.support;

import org.openlegacy.terminal.ScreenPosition;
import org.openlegacy.terminal.ScreenSize;
import org.openlegacy.terminal.TerminalRow;
import org.openlegacy.terminal.TerminalScreen;
import org.openlegacy.terminal.TerminalSnapshot;

import java.util.List;

public class TerminalIncomingSnapshot implements TerminalSnapshot {

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

	public List<ScreenPosition> getAttributes() {
		return terminalScreen.getAttributes();
	}
}
