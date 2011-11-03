package org.openlegacy.tools;

import org.openlegacy.terminal.TerminalScreen;
import org.openlegacy.terminal.persistance.SnapshotPersistanceDTO;
import org.openlegacy.terminal.persistance.TerminalPersistedSnapshot;
import org.openlegacy.terminal.support.TerminalIncomingSnapshot;
import org.openlegacy.utils.JaxbUtil;

import java.io.ByteArrayOutputStream;
import java.io.File;

public class ScreensXmlDumper extends AbstractScreensDumper {

	public static void main(String[] args) {
		if (args.length == 0) {
			System.out.println("Usage:\njava ScreensXmlDumper java-resource-folder");
			return;
		}
		String root = args[0];

		try {
			new ScreensXmlDumper().dumpSession(new File(root), true);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	@Override
	protected String getDumpContent(TerminalScreen snapshot) throws Exception {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		TerminalIncomingSnapshot incomingSnapshot = new TerminalIncomingSnapshot(snapshot);
		TerminalPersistedSnapshot persistedSnapshot = SnapshotPersistanceDTO.transformIncomingSnapshot(incomingSnapshot);
		JaxbUtil.marshal(TerminalPersistedSnapshot.class, persistedSnapshot, baos);
		return new String(baos.toByteArray());
	}

	@Override
	protected String getDumpFileExtension() {
		return "xml";
	}
}
