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
			if (lastSnapshot.getSequence() == transformSnapshot.getSequence()) {
				// verify snapshots are persisted ALWAYS by at least +1 from the last snapshot
				transformSnapshot.setSequence(transformSnapshot.getSequence() + 1);
			}
		}
		snapshots.add(transformSnapshot);
	}

	public void clear() {
		snapshots.clear();
	}

}
