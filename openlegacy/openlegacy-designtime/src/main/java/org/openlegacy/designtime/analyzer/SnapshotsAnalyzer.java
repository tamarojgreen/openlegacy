package org.openlegacy.designtime.analyzer;

import org.openlegacy.EntityDefinition;
import org.openlegacy.Snapshot;
import org.openlegacy.terminal.definitions.ScreenEntityDefinition;

import java.util.List;
import java.util.Map;

public interface SnapshotsAnalyzer<S extends Snapshot, D extends EntityDefinition<?>> {

	Map<String, ScreenEntityDefinition> analyzeSnapshots(List<S> snapshots);
}
