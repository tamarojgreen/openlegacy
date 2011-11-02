package org.openlegacy.modules.trail;

import org.openlegacy.Snapshot;

import java.util.List;

public interface SessionTrail<S extends Snapshot> {

	List<S> getSnapshots();

	void appendSnapshot(S snapshot);
}
