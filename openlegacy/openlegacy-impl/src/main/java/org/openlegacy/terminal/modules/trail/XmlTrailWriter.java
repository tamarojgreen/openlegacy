package org.openlegacy.terminal.modules.trail;

import org.openlegacy.modules.trail.SessionTrail;
import org.openlegacy.modules.trail.TrailWriter;
import org.openlegacy.terminal.TerminalSnapshot;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

public class XmlTrailWriter implements TrailWriter {

	public void write(SessionTrail<TerminalSnapshot> trail, OutputStream out) {

		TerminalPersistedTrail unifiedTerminalTrail = new TerminalPersistedTrail();
		List<TerminalSnapshot> snapshots = trail.getSnapshots();
		for (TerminalSnapshot snapshot : snapshots) {
			unifiedTerminalTrail.appendSnapshot(snapshot);
		}

		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			JAXBContext context = JAXBContext.newInstance(TerminalPersistedTrail.class);
			Marshaller m = context.createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			m.marshal(unifiedTerminalTrail, baos);
			String s = new String(baos.toByteArray());
			System.out.println(s);
		} catch (Exception e) {
			throw (new RuntimeException(e));
		}

	}
}
