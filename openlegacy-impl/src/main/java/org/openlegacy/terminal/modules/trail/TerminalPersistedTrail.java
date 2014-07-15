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
package org.openlegacy.terminal.modules.trail;

import org.openlegacy.terminal.TerminalSnapshot;
import org.openlegacy.terminal.module.TerminalSessionTrail;
import org.openlegacy.terminal.persistance.SnapshotPersistanceDTO;
import org.openlegacy.terminal.persistance.TerminalPersistedSnapshot;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "terminal-trail")
@XmlAccessorType(XmlAccessType.FIELD)
public class TerminalPersistedTrail implements TerminalSessionTrail {

	@XmlElement(name = "snapshot", type = TerminalPersistedSnapshot.class)
	private List<TerminalSnapshot> snapshots = new ArrayList<TerminalSnapshot>();

	public List<TerminalSnapshot> getSnapshots() {
		return snapshots;
	}

	public void appendSnapshot(TerminalSnapshot snapshot) {
		TerminalPersistedSnapshot transformSnapshot = SnapshotPersistanceDTO.transformSnapshot(snapshot);
		if (snapshots.size() > 0) {
			TerminalSnapshot lastSnapshot = snapshots.get(snapshots.size() - 1);
			if (lastSnapshot.getSequence() != null
					&& lastSnapshot.getSequence().equals(transformSnapshot.getSequence())) {
				// verify snapshots are persisted ALWAYS by at least +1 from the last snapshot
				transformSnapshot.setSequence(transformSnapshot.getSequence() + 1);
			}
		}
		snapshots.add(transformSnapshot);
	}

	public void clear() {
		snapshots.clear();
	}

	public TerminalSnapshot getCurrent() {
		return getSnapshots().get(snapshots.size() - 1);
	}

	public void advanceCurrent(int steps) {
		// do nothing

	}

}
