package org.openlegacy.terminal.modules.trail;

import org.openlegacy.terminal.ScreenSize;
import org.openlegacy.terminal.TerminalRow;
import org.openlegacy.terminal.TerminalScreen;
import org.openlegacy.terminal.TerminalSnapshot;
import org.openlegacy.terminal.spi.TerminalSendAction;

import java.util.List;

public class TerminalOutgoingSnapshot implements TerminalSnapshot {

	private TerminalScreen terminalScreen;
	private TerminalSendAction terminalSendAction;

	public TerminalOutgoingSnapshot(TerminalScreen terminalScreen, TerminalSendAction terminalSendAction) {
		this.terminalScreen = terminalScreen;
		this.terminalSendAction = terminalSendAction;
	}

	public SnapshotType getSnapshotType() {
		return SnapshotType.OUTGOING;
	}

	public TerminalScreen getTerminalScreen() {
		return terminalScreen;
	}

	public TerminalSendAction getTerminalSendAction() {
		return terminalSendAction;
	}

	public ScreenSize getSize() {
		return terminalScreen.getSize();
	}

	public List<TerminalRow> getRows() {
		return terminalScreen.getRows();
	}
}
