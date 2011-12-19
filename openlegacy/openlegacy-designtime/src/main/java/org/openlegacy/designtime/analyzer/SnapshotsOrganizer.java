package org.openlegacy.designtime.analyzer;

import org.openlegacy.Snapshot;
import org.openlegacy.designtime.terminal.analyzer.SnapshotPickerStrategy;

import java.util.Collection;
import java.util.Set;

public interface SnapshotsOrganizer<S extends Snapshot> {

	void add(Collection<S> snapshots);

	Collection<Set<S>> getGroups();

	Collection<S> getGroupsRepresenters(SnapshotPickerStrategy<S> snapshotPickerStrategy);

}
