/*******************************************************************************
 * Copyright (c) 2014 OpenLegacy Inc.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     OpenLegacy Inc. - initial API and implementation
 *******************************************************************************/
package org.openlegacy.terminal.mock;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.openlegacy.terminal.ConnectionProperties;
import org.openlegacy.terminal.TerminalConnection;
import org.openlegacy.terminal.TerminalOutgoingSnapshot;
import org.openlegacy.terminal.TerminalSendAction;
import org.openlegacy.terminal.TerminalSnapshot;
import org.openlegacy.terminal.TerminalSnapshot.SnapshotType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MockStateMachineTerminalConnectionFactory extends AbstractMockTerminalConnectionFactory {

	private Map<SnapshotAndSendAction, TerminalSnapshot> snapshotsGraph = new HashMap<SnapshotAndSendAction, TerminalSnapshot>();
	private TerminalSnapshot firstSnapshot = null;

	@Override
	public TerminalConnection getConnection(ConnectionProperties connectionProperties) {
		initStateMachine();
		return new MockStateMachineTerminalConnection(firstSnapshot, snapshotsGraph);
	}

	@Override
	public void disconnect(TerminalConnection terminalConnection) {
		((MockStateMachineTerminalConnection)terminalConnection).setCurrentSnapshot(firstSnapshot);
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

		@Override
		public TerminalSnapshot getNode() {
			return terminalSnapshot;
		}

		@Override
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
