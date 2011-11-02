package org.openlegacy.terminal.modules.trail;

import org.openlegacy.modules.trail.SessionTrail;
import org.openlegacy.modules.trail.TrailWriter;
import org.openlegacy.terminal.TerminalSnapshot;
import org.openlegacy.utils.JaxbUtil;

import java.io.OutputStream;
import java.util.List;

public class XmlTrailWriter implements TrailWriter {

	public void write(SessionTrail<TerminalSnapshot> trail, OutputStream out) {

		TerminalPersistedTrail persistedTrail = new TerminalPersistedTrail();
		List<TerminalSnapshot> snapshots = trail.getSnapshots();
		for (TerminalSnapshot snapshot : snapshots) {
			persistedTrail.appendSnapshot(snapshot);
		}

		try {
			JaxbUtil.marshal(TerminalPersistedTrail.class, persistedTrail, out);
		} catch (Exception e) {
			throw (new IllegalStateException("Faild writing XML trail", e));
		}

	}
}
