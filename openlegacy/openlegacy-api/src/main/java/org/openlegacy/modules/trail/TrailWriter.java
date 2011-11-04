package org.openlegacy.modules.trail;

import org.openlegacy.Snapshot;

import java.io.OutputStream;

public interface TrailWriter {

	void write(SessionTrail<? extends Snapshot> trail, OutputStream out);
}
