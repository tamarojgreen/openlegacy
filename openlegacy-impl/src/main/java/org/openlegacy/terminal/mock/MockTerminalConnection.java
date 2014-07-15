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
import org.openlegacy.exceptions.SessionEndedException;
import org.openlegacy.terminal.TerminalSendAction;
import org.openlegacy.terminal.TerminalSnapshot;
import org.openlegacy.terminal.TerminalSnapshot.SnapshotType;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MockTerminalConnection extends AbstractMockTerminalConnection implements Serializable {

	private static final long serialVersionUID = 1L;

	private List<TerminalSnapshot> snapshots = new ArrayList<TerminalSnapshot>();
	private int currentIndex = 0;
	private boolean connected = false;

	public MockTerminalConnection(List<TerminalSnapshot> snapshots) {
		for (TerminalSnapshot terminalSnapshot : snapshots) {
			this.snapshots.add((TerminalSnapshot)SerializationUtils.clone(terminalSnapshot));
		}
	}

	public TerminalSnapshot getSnapshot() {
		setConnected(true);
		if (currentIndex >= snapshots.size()) {
			throw (new SessionEndedException("Mock session has been finished"));
		}
		TerminalSnapshot snapshot = snapshots.get(currentIndex);
		setConnected(true);
		return snapshot;
	}

	void setConnected(boolean connected) {
		this.connected = connected;

	}

	public void doAction(TerminalSendAction terminalSendAction) {
		currentIndex++;
		if (currentIndex >= snapshots.size()) {
			throw (new SessionEndedException("Mock session has been finished"));
		}

		TerminalSnapshot currentSnapshot = snapshots.get(currentIndex);
		if (currentSnapshot.getSnapshotType() == SnapshotType.OUTGOING) {
			MockSendValidationUtils.validateSendAction(currentSnapshot, terminalSendAction);
			currentIndex++;
		}
	}

	public Object getDelegate() {
		throw (new UnsupportedOperationException("Mock terminal session has not delegate"));
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
		return connected;
	}

	public void disconnect() {
		currentIndex = 0;
		setConnected(false);

	}

	public Integer getSequence() {
		return getSnapshot().getSequence();
	}
}
