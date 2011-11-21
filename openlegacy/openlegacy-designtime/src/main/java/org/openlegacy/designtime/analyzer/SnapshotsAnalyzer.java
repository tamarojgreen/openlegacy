package org.openlegacy.designtime.analyzer;

import org.openlegacy.HostEntityDefinition;
import org.openlegacy.Snapshot;

import java.util.List;

public interface SnapshotsAnalyzer<S extends Snapshot, D extends HostEntityDefinition<?>> {

	List<D> analyzeSnapshots(List<S> snapshots);
}
