package org.openlegacy.trail;

import java.io.OutputStream;

public interface TrailWriter {

	void write(SessionTrail trail, OutputStream out);
}
