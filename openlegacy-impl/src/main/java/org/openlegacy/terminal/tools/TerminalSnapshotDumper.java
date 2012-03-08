package org.openlegacy.terminal.tools;

import org.openlegacy.terminal.TerminalSnapshot;

public interface TerminalSnapshotDumper {

	byte[] getDumpContent(TerminalSnapshot snapshot);

	String getDumpFileExtension();
}
