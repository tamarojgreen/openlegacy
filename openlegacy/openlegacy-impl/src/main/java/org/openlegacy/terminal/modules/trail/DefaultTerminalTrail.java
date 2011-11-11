package org.openlegacy.terminal.modules.trail;

import org.openlegacy.modules.trail.SessionTrail;
import org.openlegacy.terminal.TerminalSnapshot;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

public class DefaultTerminalTrail implements SessionTrail<TerminalSnapshot>, Serializable {

	private static final long serialVersionUID = 1L;

	private LinkedList<TerminalSnapshot> snapshots = new LinkedList<TerminalSnapshot>();

	private int historyCount = 5;

	public List<TerminalSnapshot> getSnapshots() {
		return snapshots;
	}

	public void appendSnapshot(TerminalSnapshot snapshot) {
		snapshots.add(snapshot);

		if (snapshots.size() > historyCount) {
			snapshots.removeFirst();
		}
	}

	public int getHistoryCount() {
		return historyCount;
	}

	public void setHistoryCount(int historyCount) {
		this.historyCount = historyCount;
	}
}
