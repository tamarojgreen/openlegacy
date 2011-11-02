package org.openlegacy.terminal.modules.trail;

import org.openlegacy.modules.trail.SessionTrail;
import org.openlegacy.terminal.TerminalSnapshot;
import org.openlegacy.terminal.persistance.TerminalPersistedSnapshot;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "terminal-trail")
@XmlAccessorType(XmlAccessType.FIELD)
public class TerminalPersistedTrail implements SessionTrail<TerminalSnapshot> {

	@XmlElement(name = "snapshot", type = TerminalPersistedSnapshot.class)
	private List<TerminalSnapshot> snapshots = new ArrayList<TerminalSnapshot>();

	public List<TerminalSnapshot> getSnapshots() {
		return snapshots;
	}

	public void appendSnapshot(TerminalSnapshot snapshot) {
		snapshots.add(SnapshotPersistanceDTO.transformSnapshot(snapshot));
	}

}
