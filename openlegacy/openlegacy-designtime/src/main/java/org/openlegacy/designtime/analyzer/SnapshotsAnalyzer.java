package org.openlegacy.designtime.analyzer;

import org.openlegacy.EntityDefinition;
import org.openlegacy.Snapshot;

import java.util.List;
import java.util.Map;

public interface SnapshotsAnalyzer<S extends Snapshot, D extends EntityDefinition<?>> {

	Map<String, D> analyzeSnapshots(List<S> snapshots);
}
