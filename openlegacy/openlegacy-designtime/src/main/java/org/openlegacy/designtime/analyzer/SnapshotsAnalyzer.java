package org.openlegacy.designtime.analyzer;

import org.openlegacy.EntityDefinition;
import org.openlegacy.Snapshot;

import java.util.List;

public interface SnapshotsAnalyzer<S extends Snapshot, D extends EntityDefinition<?>> {

	List<D> analyzeSnapshots(List<S> snapshots);
}
