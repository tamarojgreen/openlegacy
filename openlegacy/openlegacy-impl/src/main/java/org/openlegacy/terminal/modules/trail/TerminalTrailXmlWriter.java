package org.openlegacy.terminal.modules.trail;

import org.openlegacy.Snapshot;
import org.openlegacy.modules.trail.SessionTrail;
import org.openlegacy.modules.trail.TrailWriter;
import org.openlegacy.terminal.TerminalSnapshot;
import org.openlegacy.utils.XmlSerializationUtil;

import java.io.OutputStream;
import java.util.List;

public class TerminalTrailXmlWriter implements TrailWriter {

	public void write(SessionTrail<? extends Snapshot> trail, OutputStream out) {

		TerminalPersistedTrail persistedTrail = new TerminalPersistedTrail();
		List<? extends Snapshot> snapshots = trail.getSnapshots();
		for (Snapshot snapshot : snapshots) {
			persistedTrail.appendSnapshot((TerminalSnapshot)snapshot);
		}

		try {
			XmlSerializationUtil.serialize(TerminalPersistedTrail.class, persistedTrail, out);
		} catch (Exception e) {
			throw (new IllegalArgumentException("Faild writing XML trail", e));
		}

	}

}
