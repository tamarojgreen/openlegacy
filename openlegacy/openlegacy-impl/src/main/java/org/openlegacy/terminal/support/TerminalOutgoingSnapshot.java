package org.openlegacy.terminal.support;

import org.openlegacy.terminal.ScreenPosition;
import org.openlegacy.terminal.ScreenSize;
import org.openlegacy.terminal.TerminalRow;
import org.openlegacy.terminal.TerminalScreen;
import org.openlegacy.terminal.TerminalSnapshot;
import org.openlegacy.terminal.spi.TerminalSendAction;

import java.io.Serializable;
import java.util.List;

public class TerminalOutgoingSnapshot implements TerminalSnapshot, Serializable {

	private static final long serialVersionUID = 1L;

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

	public List<ScreenPosition> getFieldSeperators() {
		return terminalScreen.getFieldSeperators();
	}

	public ScreenPosition getCursorPosition() {
		if (terminalSendAction.getCursorPosition() != null) {
			return terminalSendAction.getCursorPosition();
		}
		return terminalScreen.getCursorPosition();
	}
}
