package org.openlegacy.terminal.mock;

import org.apache.commons.lang.SerializationUtils;
import org.openlegacy.exceptions.SessionEndedException;
import org.openlegacy.terminal.TerminalConnection;
import org.openlegacy.terminal.TerminalSnapshot;
import org.openlegacy.terminal.TerminalSnapshot.SnapshotType;
import org.openlegacy.terminal.spi.TerminalSendAction;

import java.util.List;

public class MockTerminalConnection extends AbstractMockTerminalConnection {

	private List<TerminalSnapshot> snapshots;
	private int currentIndex = 0;

	public MockTerminalConnection(List<TerminalSnapshot> snapshots) {
		this.snapshots = snapshots;
	}

	public TerminalSnapshot getSnapshot() {
		if (currentIndex >= snapshots.size()) {
			throw (new SessionEndedException("Mock session has been finished"));
		}
		TerminalSnapshot snapshot = snapshots.get(currentIndex);
		return (TerminalSnapshot)SerializationUtils.clone(snapshot);
	}

	public TerminalConnection doAction(TerminalSendAction terminalSendAction) {
		currentIndex++;
		if (currentIndex >= snapshots.size()) {
			throw (new SessionEndedException("Mock session has been finished"));
		}

		TerminalSnapshot currentSnapshot = snapshots.get(currentIndex);
		if (currentSnapshot.getSnapshotType() == SnapshotType.OUTGOING) {
			if (terminalSendAction.getModifiedFields().size() > 0) {
				MockSendValidationUtils.validateSendAction(currentSnapshot, terminalSendAction);
			}
			currentIndex++;
		}

		return this;
	}

	public Object getDelegate() {
		throw (new UnsupportedOperationException("Mock terminal session has not delegate"));
	}

	public void reset() {
		currentIndex = 0;
	}

	public void setCurrentIndex(int currentIndex) {
		this.currentIndex = currentIndex;
	}

	public TerminalSnapshot fetchSnapshot() {
		return getSnapshot();
	}

	public List<TerminalSnapshot> getSnapshots() {
		return snapshots;
	}

	public boolean isConnected() {
		return currentIndex > 0;
	}

	public void disconnect() {
		reset();
	}
}
