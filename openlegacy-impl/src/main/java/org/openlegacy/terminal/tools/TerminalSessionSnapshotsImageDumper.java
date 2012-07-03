package org.openlegacy.terminal.tools;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.openlegacy.terminal.TerminalSnapshot;
import org.openlegacy.terminal.render.TerminalSnapshotRenderer;

public class TerminalSessionSnapshotsImageDumper implements TerminalSnapshotDumper {

	private TerminalSnapshotRenderer imageRenderer;

	public byte[] getDumpContent(TerminalSnapshot snapshot) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		imageRenderer.render(snapshot, baos);
		return baos.toByteArray();
	}

	public String getDumpFileExtension() {
		return "jpg";
	}

	public void setImageRenderer(TerminalSnapshotRenderer imageRenderer) {
		this.imageRenderer = imageRenderer;
	}
}
