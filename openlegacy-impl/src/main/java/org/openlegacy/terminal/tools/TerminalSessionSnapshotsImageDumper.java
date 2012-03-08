package org.openlegacy.terminal.tools;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.openlegacy.terminal.TerminalSnapshot;
import org.openlegacy.terminal.render.TerminalSnapshotImageRenderer;

public class TerminalSessionSnapshotsImageDumper implements TerminalSnapshotDumper {

	public byte[] getDumpContent(TerminalSnapshot snapshot) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		TerminalSnapshotImageRenderer.instance().render(snapshot, baos);
		return baos.toByteArray();
	}

	public String getDumpFileExtension() {
		return "jpg";
	}

}
