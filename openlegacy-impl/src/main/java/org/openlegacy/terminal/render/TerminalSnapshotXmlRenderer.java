package org.openlegacy.terminal.render;

import org.openlegacy.exceptions.OpenLegacyRuntimeException;
import org.openlegacy.terminal.TerminalSnapshot;
import org.openlegacy.terminal.persistance.SnapshotPersistanceDTO;
import org.openlegacy.terminal.persistance.TerminalPersistedSnapshot;
import org.openlegacy.utils.XmlSerializationUtil;

import java.io.OutputStream;

import javax.xml.bind.JAXBException;

public class TerminalSnapshotXmlRenderer implements TerminalSnapshotRenderer {

	private static TerminalSnapshotRenderer instance = new TerminalSnapshotXmlRenderer();

	public static TerminalSnapshotRenderer instance() {
		return instance;
	}

	public void render(TerminalSnapshot terminalSnapshot, OutputStream outputStream) {
		TerminalPersistedSnapshot persistedSnapshot = null;
		if (terminalSnapshot instanceof TerminalPersistedSnapshot) {
			persistedSnapshot = (TerminalPersistedSnapshot)terminalSnapshot;
		} else {
			persistedSnapshot = SnapshotPersistanceDTO.transformSnapshot(terminalSnapshot);
		}
		try {
			XmlSerializationUtil.serialize(TerminalPersistedSnapshot.class, persistedSnapshot, outputStream);
		} catch (JAXBException e) {
			throw (new OpenLegacyRuntimeException(e));
		}
	}

	public String getFileFormat() {
		return "xml";
	}

}
