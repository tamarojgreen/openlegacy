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

import org.apache.commons.lang.SerializationUtils;
import org.openlegacy.SnapshotsSimilarityChecker;
import org.openlegacy.terminal.TerminalPosition;
import org.openlegacy.terminal.TerminalSendAction;
import org.openlegacy.terminal.TerminalSnapshot;
import org.openlegacy.terminal.mock.MockStateMachineTerminalConnectionFactory.SnapshotAndSendAction;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MockStateMachineTerminalConnection extends AbstractMockTerminalConnection implements Serializable {

	private static final long serialVersionUID = 1L;

	private Map<TerminalSnapshot, List<SnapshotAndSendAction>> snapshotsGraph;

	private TerminalSnapshot currentSnapshot;

	private SnapshotsSimilarityChecker<TerminalSnapshot> snapshotsSimilarityChecker;

	private int similarityPercentage = 100;

	private boolean exactCursor;

	private boolean exactFields;

	private boolean exactCommand;

	private List<TerminalSnapshot> snapshots;

	public MockStateMachineTerminalConnection(Map<TerminalSnapshot, List<SnapshotAndSendAction>> snapshotsGraph,
			List<TerminalSnapshot> snapshots, boolean exactCursor, boolean exactFields, boolean exactCommand,
			int similarityPercentage) {
		this.snapshotsGraph = snapshotsGraph;
		this.currentSnapshot = snapshots.get(0);
		this.snapshots = snapshots;
		this.exactCommand = exactCommand;
		this.exactCursor = exactCursor;
		this.exactFields = exactFields;
	}

	@Override
	public TerminalSnapshot getSnapshot() {
		TerminalSnapshot clonedSnapshot = (TerminalSnapshot)SerializationUtils.clone(currentSnapshot);
		return clonedSnapshot;
	}

	@Override
	public void doAction(TerminalSendAction terminalSendAction) {
		List<SnapshotAndSendAction> group = findGroup(currentSnapshot);

		for (SnapshotAndSendAction snapshotAndSendAction : group) {
			TerminalSendAction comparedSendAction = snapshotAndSendAction.getArc();
			// compare fields , command and cursor
			TerminalPosition cursorPosition = terminalSendAction.getCursorPosition();
			if (Arrays.equals(terminalSendAction.getFields().toArray(), comparedSendAction.getFields().toArray())
					&& terminalSendAction.getCommand().equals(comparedSendAction.getCommand()) && cursorPosition != null
					&& cursorPosition.equals(comparedSendAction.getCursorPosition())) {
				currentSnapshot = snapshotAndSendAction.getNode();
				return;
			}
			// compare fields and command
			if (!exactCursor && Arrays.equals(terminalSendAction.getFields().toArray(), comparedSendAction.getFields().toArray())
					&& terminalSendAction.getCommand().equals(comparedSendAction.getCommand())) {
				currentSnapshot = snapshotAndSendAction.getNode();
				return;
			}
			// compare command only
			if (!exactCursor && !exactFields && terminalSendAction.getCommand().equals(comparedSendAction.getCommand())) {
				currentSnapshot = snapshotAndSendAction.getNode();
				return;
			}
			// compare command only
			if (!exactCursor && !exactFields && !exactCommand) {
				int counter = 0;
				for (TerminalSnapshot snapshot : snapshots) {
					if (snapshot.getSequence().equals(currentSnapshot.getSequence())) {
						if (snapshots.size() < counter + 1) {
							currentSnapshot = snapshots.get(counter + 1);
						} else {
							currentSnapshot = snapshots.get(0);
						}
					}
					counter++;
				}
				return;
			}
		}
	}

	private List<SnapshotAndSendAction> findGroup(TerminalSnapshot currentSnapshot) {
		Set<TerminalSnapshot> snapshots = snapshotsGraph.keySet();
		for (TerminalSnapshot terminalSnapshot : snapshots) {
			if (snapshotsSimilarityChecker.similarityPercent(currentSnapshot, terminalSnapshot) >= similarityPercentage) {
				return snapshotsGraph.get(terminalSnapshot);
			}
		}
		return null;

	}

	@Override
	public Object getDelegate() {
		return null;
	}

	public void setCurrentSnapshot(TerminalSnapshot currentSnapshot) {
		this.currentSnapshot = currentSnapshot;
	}

	@Override
	public TerminalSnapshot fetchSnapshot() {
		return getSnapshot();
	}

	@Override
	public boolean isConnected() {
		return true;
	}

	@Override
	public void disconnect() {
		// do nothing (mock)
	}

	@Override
	public Integer getSequence() {
		return currentSnapshot.getSequence();
	}

	public void setSnapshotsSimilarityChecker(SnapshotsSimilarityChecker<TerminalSnapshot> snapshotsSimilarityChecker) {
		this.snapshotsSimilarityChecker = snapshotsSimilarityChecker;
	}
}
