package org.openlegacy.terminal.tools;

import org.openlegacy.terminal.TerminalSnapshot;

public class TerminalSessionSnapshotsTextDumper implements TerminalSnapshotDumper {

	public byte[] getDumpContent(TerminalSnapshot snapshot) {
		return snapshot.toString().getBytes();
	}

	public String getDumpFileExtension() {
		return "txt";
	}

}
