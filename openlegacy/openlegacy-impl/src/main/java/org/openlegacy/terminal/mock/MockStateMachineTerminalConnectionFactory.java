package org.openlegacy.terminal.mock;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.openlegacy.terminal.TerminalConnection;
import org.openlegacy.terminal.TerminalOutgoingSnapshot;
import org.openlegacy.terminal.TerminalSnapshot;
import org.openlegacy.terminal.TerminalSnapshot.SnapshotType;
import org.openlegacy.terminal.spi.TerminalSendAction;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MockStateMachineTerminalConnectionFactory extends AbstractMockTerminalConnectionFactory {

	private Map<SnapshotAndSendAction, TerminalSnapshot> snapshotsGraph = new HashMap<SnapshotAndSendAction, TerminalSnapshot>();
	private TerminalSnapshot firstSnapshot = null;

	public TerminalConnection getConnection() {
		initStateMachine();
		return new MockStateMachineTerminalConnection(firstSnapshot, snapshotsGraph);
	}

	private void initStateMachine() {
		List<TerminalSnapshot> snapshots = fetchSnapshots();

		TerminalSnapshot lastIncomingSnapshot = null;
		SnapshotAndSendAction lastNodeAndArc = null;
		for (TerminalSnapshot terminalSnapshot : snapshots) {
			if (lastIncomingSnapshot == null) {
				lastIncomingSnapshot = terminalSnapshot;
				firstSnapshot = terminalSnapshot;
			}
			if (terminalSnapshot.getSnapshotType() == SnapshotType.INCOMING && lastNodeAndArc != null) {
				snapshotsGraph.put(lastNodeAndArc, terminalSnapshot);
				lastNodeAndArc = null;
				lastIncomingSnapshot = terminalSnapshot;
			}

			if (terminalSnapshot.getSnapshotType() == SnapshotType.OUTGOING) {
				TerminalSendAction terminalSendAction = ((TerminalOutgoingSnapshot)terminalSnapshot).getTerminalSendAction();
				SnapshotAndSendAction nodeAndArc = new SnapshotAndSendAction(lastIncomingSnapshot, terminalSendAction);
				if (!snapshotsGraph.containsKey(nodeAndArc)) {
					lastNodeAndArc = nodeAndArc;
				}
			}
		}
	}

	public static interface NodeAndArc<N, A> {

		N getNode();

		A getArc();
	}

	public static class SnapshotAndSendAction implements NodeAndArc<TerminalSnapshot, TerminalSendAction> {

		private TerminalSnapshot terminalSnapshot;
		private TerminalSendAction terminalSendAction;

		public SnapshotAndSendAction(TerminalSnapshot terminalSnapshot, TerminalSendAction terminalSendAction) {
			this.terminalSnapshot = terminalSnapshot;
			this.terminalSendAction = terminalSendAction;
		}

		public TerminalSnapshot getNode() {
			return terminalSnapshot;
		}

		public TerminalSendAction getArc() {
			return terminalSendAction;
		}

		@Override
		public boolean equals(Object obj) {
			return EqualsBuilder.reflectionEquals(this, obj);
		}

		@Override
		public int hashCode() {
			return HashCodeBuilder.reflectionHashCode(this);
		}

		@Override
		public String toString() {
			return ToStringBuilder.reflectionToString(this);
		}
	}
}
