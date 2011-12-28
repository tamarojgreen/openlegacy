package org.openlegacy.terminal.mock;

import org.apache.commons.lang.SerializationUtils;
import org.openlegacy.exceptions.OpenLegacyRuntimeException;
import org.openlegacy.terminal.TerminalConnection;
import org.openlegacy.terminal.TerminalSnapshot;
import org.openlegacy.terminal.mock.MockStateMachineTerminalConnectionFactory.SnapshotAndSendAction;
import org.openlegacy.terminal.spi.TerminalSendAction;

import java.util.Map;

public class MockStateMachineTerminalConnection extends AbstractMockTerminalConnection {

	private Map<SnapshotAndSendAction, TerminalSnapshot> snapshotsGraph;

	private TerminalSnapshot currentSnapshot;

	public MockStateMachineTerminalConnection(TerminalSnapshot firstSnapshot,
			Map<SnapshotAndSendAction, TerminalSnapshot> snapshotsGraph) {
		this.currentSnapshot = firstSnapshot;
		this.snapshotsGraph = snapshotsGraph;
	}

	public TerminalSnapshot getSnapshot() {
		TerminalSnapshot clonedSnapshot = (TerminalSnapshot)SerializationUtils.clone(currentSnapshot);
		return clonedSnapshot;
	}

	public TerminalConnection doAction(TerminalSendAction terminalSendAction) {
		SnapshotAndSendAction graphKey = new SnapshotAndSendAction(currentSnapshot, terminalSendAction);
		TerminalSnapshot targetSnapshot = snapshotsGraph.get(graphKey);
		if (targetSnapshot == null) {
			throw (new OpenLegacyRuntimeException("No target screen found"));
		}
		currentSnapshot = targetSnapshot;
		return this;
	}

	public Object getDelegate() {
		return null;
	}

	public void setCurrentSnapshot(TerminalSnapshot currentSnapshot) {
		this.currentSnapshot = currentSnapshot;
	}
}
