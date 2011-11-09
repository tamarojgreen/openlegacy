package org.openlegacy.analyzer.screen;

import org.openlegacy.Snapshot;

import java.util.Collection;
import java.util.Set;

public interface SnapshotsSorter {

	void add(SnapshotType snapshotType, Snapshot snapshot);
	Collection<Set<Snapshot>> getMatches();

}
