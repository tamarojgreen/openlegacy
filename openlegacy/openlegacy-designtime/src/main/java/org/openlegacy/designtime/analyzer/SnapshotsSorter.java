package org.openlegacy.designtime.analyzer;

import org.openlegacy.Snapshot;

import java.util.Collection;
import java.util.Set;

public interface SnapshotsSorter<S extends Snapshot> {

	void add(Collection<S> snapshots);

	Collection<Set<S>> getGroups();

	Collection<S> getFirstOfEachGroup();

}
