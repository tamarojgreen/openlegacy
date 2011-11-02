package org.openlegacy.tools;

import org.openlegacy.terminal.TerminalScreen;
import org.openlegacy.terminal.TerminalSnapshot;
import org.openlegacy.terminal.persistance.SnapshotPersistanceDTO;
import org.openlegacy.terminal.persistance.TerminalPersistedSnapshot;
import org.openlegacy.terminal.support.TerminalIncomingSnapshot;

import java.io.ByteArrayOutputStream;
import java.text.MessageFormat;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

public class ScreensXmlDumper extends AbstractScreensDumper {

	public static void main(String[] args) {
		try {
			new ScreensXmlDumper().iterate();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	@Override
	protected String getContent(TerminalScreen snapshot) throws Exception {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		JAXBContext context = JAXBContext.newInstance(TerminalPersistedSnapshot.class);
		Marshaller m = context.createMarshaller();
		m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		TerminalIncomingSnapshot incomingSnapshot = new TerminalIncomingSnapshot(snapshot);
		TerminalSnapshot persistedSnapshot = SnapshotPersistanceDTO.transformIncomingSnapshot(incomingSnapshot);
		m.marshal(persistedSnapshot, baos);
		String s = new String(baos.toByteArray());
		return s;
	}

	@Override
	protected String getRelativeFilePath(int count) {
		return "src/test/resources/screens_xml/" + MessageFormat.format("screen{0}.xml", count);
	}
}
