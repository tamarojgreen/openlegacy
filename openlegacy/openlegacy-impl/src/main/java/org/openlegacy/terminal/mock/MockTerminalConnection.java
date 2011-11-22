package org.openlegacy.terminal.mock;

import org.openlegacy.exceptions.SessionEndedException;
import org.openlegacy.terminal.TerminalConnection;
import org.openlegacy.terminal.TerminalSnapshot;
import org.openlegacy.terminal.TerminalSnapshot.SnapshotType;
import org.openlegacy.terminal.spi.TerminalSendAction;

import java.util.List;

public class MockTerminalConnection implements TerminalConnection {

	private List<TerminalSnapshot> snapshots;
	private int currentIndex = 0;

	public MockTerminalConnection(List<TerminalSnapshot> snapshots) {
		this.snapshots = snapshots;
	}

	public TerminalSnapshot getSnapshot() {
		if (currentIndex >= snapshots.size()) {
			throw (new SessionEndedException("Mock session has been finished"));
		}
		return snapshots.get(currentIndex);
	}

	public TerminalConnection doAction(TerminalSendAction terminalSendAction) {
		currentIndex++;
		if (currentIndex >= snapshots.size()) {
			throw (new SessionEndedException("Mock session has been finished"));
		}

		TerminalSnapshot currentSnapshot = snapshots.get(currentIndex);
		if (currentSnapshot.getSnapshotType() == SnapshotType.OUTGOING) {
			MockSendValidationUtils.validateSendAction(currentSnapshot, terminalSendAction);
			currentIndex++;
		}

		return this;
	}

	public Object getDelegate() {
		throw (new UnsupportedOperationException("Mock terminal session has not delegate"));
	}

}
