package org.openlegacy.designtime.terminal.analyzer.support;

import org.openlegacy.designtime.terminal.analyzer.SnapshotPickerStrategy;
import org.openlegacy.terminal.TerminalSnapshot;
import org.openlegacy.terminal.TerminalSnapshot.SnapshotType;

import java.util.Set;

public class LowestSequenceSnapshotPickerStrategy implements SnapshotPickerStrategy<TerminalSnapshot> {

	public TerminalSnapshot pickRepresenter(Set<TerminalSnapshot> group) {
		TerminalSnapshot[] snapshots = group.toArray(new TerminalSnapshot[group.size()]);

		TerminalSnapshot lowestSequenceSnapshot = null;

		for (TerminalSnapshot terminalSnapshot : snapshots) {

			int lowestSequence = Integer.MAX_VALUE;

			// pick only incoming snapshot as representer
			if (terminalSnapshot.getSnapshotType() == SnapshotType.OUTGOING) {
				continue;
			}

			if (lowestSequenceSnapshot == null) {
				lowestSequenceSnapshot = terminalSnapshot;
				lowestSequence = lowestSequenceSnapshot.getSequence() != null ? lowestSequenceSnapshot.getSequence() : 0;
			}
			if (terminalSnapshot.getSequence() == null) {
				continue;
			}

			if (terminalSnapshot.getSequence() < lowestSequence) {
				lowestSequenceSnapshot = terminalSnapshot;
				lowestSequence = lowestSequenceSnapshot.getSequence();
			}
		}
		return lowestSequenceSnapshot;
	}

}
