package org.openlegacy.terminal.tools;

import org.openlegacy.exceptions.OpenLegacyRuntimeException;
import org.openlegacy.terminal.TerminalSnapshot;
import org.openlegacy.terminal.persistance.SnapshotPersistanceDTO;
import org.openlegacy.terminal.persistance.TerminalPersistedSnapshot;
import org.openlegacy.utils.XmlSerializationUtil;

import java.io.ByteArrayOutputStream;

import javax.xml.bind.JAXBException;

public class TerminalSessionSnapshotsXmlDumper implements TerminalSnapshotDumper {

	public byte[] getDumpContent(TerminalSnapshot snapshot) {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		TerminalPersistedSnapshot persistedSnapshot = SnapshotPersistanceDTO.transformIncomingSnapshot(snapshot);
		try {
			XmlSerializationUtil.serialize(TerminalPersistedSnapshot.class, persistedSnapshot, baos);
		} catch (JAXBException e) {
			throw (new OpenLegacyRuntimeException(e));
		}
		return baos.toByteArray();
	}

	public String getDumpFileExtension() {
		return "xml";
	}
}
