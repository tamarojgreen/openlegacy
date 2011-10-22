package org.openlegacy.trail;

import java.io.OutputStream;

public interface TrailWriter<ST extends SessionTrail> {

	void write(ST trail, OutputStream out);
}
