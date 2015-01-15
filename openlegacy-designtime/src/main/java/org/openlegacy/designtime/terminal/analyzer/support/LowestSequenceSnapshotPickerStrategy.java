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
package org.openlegacy.designtime.terminal.analyzer.support;

import java.util.Set;

import org.openlegacy.terminal.SnapshotPickerStrategy;
import org.openlegacy.terminal.TerminalSnapshot;
import org.openlegacy.terminal.TerminalSnapshot.SnapshotType;

public class LowestSequenceSnapshotPickerStrategy implements SnapshotPickerStrategy<TerminalSnapshot> {

	@Override
	public TerminalSnapshot pickRepresenter(Set<TerminalSnapshot> group) {
		TerminalSnapshot[] snapshots = group.toArray(new TerminalSnapshot[group.size()]);

		TerminalSnapshot lowestSequenceSnapshot = null;

		int lowestSequence = Integer.MAX_VALUE;

		for (TerminalSnapshot terminalSnapshot : snapshots) {

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
