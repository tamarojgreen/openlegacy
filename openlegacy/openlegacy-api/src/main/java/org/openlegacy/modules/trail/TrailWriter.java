package org.openlegacy.modules.trail;

import org.openlegacy.Snapshot;

import java.io.OutputStream;

/**
 * Define a session trail writer. Write the given session trail to the given output stream
 * 
 */
public interface TrailWriter {

	void write(SessionTrail<? extends Snapshot> trail, OutputStream out);
}
