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
import org.openlegacy.exceptions.OpenLegacyRuntimeException;
import org.openlegacy.terminal.TerminalSendAction;
import org.openlegacy.terminal.TerminalSnapshot;
import org.openlegacy.terminal.mock.MockStateMachineTerminalConnectionFactory.SnapshotAndSendAction;

import java.io.Serializable;
import java.util.Map;

public class MockStateMachineTerminalConnection extends AbstractMockTerminalConnection implements Serializable {

	private static final long serialVersionUID = 1L;

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

	public void doAction(TerminalSendAction terminalSendAction) {
		SnapshotAndSendAction graphKey = new SnapshotAndSendAction(currentSnapshot, terminalSendAction);
		TerminalSnapshot targetSnapshot = snapshotsGraph.get(graphKey);
		if (targetSnapshot == null) {
			throw (new OpenLegacyRuntimeException("No target screen found"));
		}
		currentSnapshot = targetSnapshot;
	}

	public Object getDelegate() {
		return null;
	}

	public void setCurrentSnapshot(TerminalSnapshot currentSnapshot) {
		this.currentSnapshot = currentSnapshot;
	}

	public TerminalSnapshot fetchSnapshot() {
		return getSnapshot();
	}

	public boolean isConnected() {
		return true;
	}

	public void disconnect() {
		// do nothing (mock)
	}

	public Integer getSequence() {
		return currentSnapshot.getSequence();
	}
}
