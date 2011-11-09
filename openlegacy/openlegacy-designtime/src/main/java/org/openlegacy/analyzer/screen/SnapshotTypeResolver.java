package org.openlegacy.analyzer.screen;

import org.openlegacy.Snapshot;

public interface SnapshotTypeResolver<S extends Snapshot> {

	SnapshotType resolve(S snapshot);
}
