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
import org.openlegacy.SnapshotsOrganizer;
import org.openlegacy.SnapshotsSimilarityChecker;
import org.openlegacy.terminal.ConnectionProperties;
import org.openlegacy.terminal.TerminalConnection;
import org.openlegacy.terminal.TerminalOutgoingSnapshot;
import org.openlegacy.terminal.TerminalSendAction;
import org.openlegacy.terminal.TerminalSnapshot;
import org.openlegacy.terminal.TerminalSnapshot.SnapshotType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

public class MockStateMachineTerminalConnectionFactory extends AbstractMockTerminalConnectionFactory {

	private Map<TerminalSnapshot, List<SnapshotAndSendAction>> snapshotsGraph = new HashMap<TerminalSnapshot, List<SnapshotAndSendAction>>();

	@Inject
	private SnapshotsOrganizer<TerminalSnapshot> snapshotsOrganizer;

	private TerminalSnapshot firstSnapshot;

	@Inject
	private SnapshotsSimilarityChecker<TerminalSnapshot> snapshotsSimilarityChecker;

	private boolean exactCursor = false;

	private boolean exactFields = true;

	private boolean exactCommand = true;

	private int similarityPercentage = 95;

	@Override
	public TerminalConnection getConnection(ConnectionProperties connectionProperties) {
		List<TerminalSnapshot> snapshots = fetchSnapshots();

		initStateMachine(snapshots);
		MockStateMachineTerminalConnection mockupConnection = new MockStateMachineTerminalConnection(snapshotsGraph, snapshots,
				exactCursor, exactFields, exactCommand, similarityPercentage);
		mockupConnection.setSnapshotsSimilarityChecker(snapshotsSimilarityChecker);
		return mockupConnection;

	}

	@Override
	public void disconnect(TerminalConnection terminalConnection) {
		((MockStateMachineTerminalConnection)terminalConnection).setCurrentSnapshot(firstSnapshot);
	}

	private void initStateMachine(List<TerminalSnapshot> snapshots) {
		snapshotsOrganizer.add(snapshots);

		Collection<Set<TerminalSnapshot>> groups = snapshotsOrganizer.getGroups();

		firstSnapshot = snapshots.get(0);
		for (Set<TerminalSnapshot> group : groups) {
			TerminalSnapshot firstGroupSnapshot = group.iterator().next();
			List<SnapshotAndSendAction> target = new ArrayList<SnapshotAndSendAction>();
			snapshotsGraph.put(firstGroupSnapshot, target);
			int count = 0;
			for (TerminalSnapshot snapshot : snapshots) {
				if (snapshot.getSnapshotType() == SnapshotType.OUTGOING
						&& snapshotsSimilarityChecker.similarityPercent(snapshot, firstGroupSnapshot) >= similarityPercentage) {
					TerminalSendAction terminalSendAction = ((TerminalOutgoingSnapshot)snapshot).getTerminalSendAction();
					if (snapshots.size() > count + 1) {
						TerminalSnapshot nextSnapShot = snapshots.get(count + 1);
						SnapshotAndSendAction snapshotAndAction = new SnapshotAndSendAction(nextSnapShot, terminalSendAction);
						target.add(snapshotAndAction);
					}

				}
				count++;
			}
		}

	}

	public void setExactCommand(boolean exactCommand) {
		this.exactCommand = exactCommand;
	}

	public void setExactCursor(boolean exactCursor) {
		this.exactCursor = exactCursor;
	}

	public void setExactFields(boolean exactFields) {
		this.exactFields = exactFields;
	}

	public void setSimilarityPercentage(int similarityPercentage) {
		this.similarityPercentage = similarityPercentage;
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
