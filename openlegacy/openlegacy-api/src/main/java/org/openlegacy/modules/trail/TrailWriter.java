package org.openlegacy.modules.trail;

import org.openlegacy.terminal.TerminalSnapshot;

import java.io.OutputStream;

public interface TrailWriter {

	void write(SessionTrail<TerminalSnapshot> trail, OutputStream out);
}
