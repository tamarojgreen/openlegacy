package org.openlegacy.terminal.render;

import org.openlegacy.terminal.TerminalSnapshot;

import java.io.OutputStream;

public interface TerminalSnapshotRenderer {

	void render(TerminalSnapshot terminalSnapshot, OutputStream outputStream);

	String getFileFormat();
}