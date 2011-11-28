package org.openlegacy.designtime.analyzer;

import org.openlegacy.EntityDefinition;
import org.openlegacy.Snapshot;
import org.openlegacy.terminal.definitions.ScreenEntityDefinition;

import java.util.Collection;
import java.util.Map;

public interface SnapshotsAnalyzerContext<S extends Snapshot, D extends EntityDefinition<?>> {

	Collection<S> getActiveSnapshots();

	void setActiveSnapshots(Collection<S> snapshots);

	void addEntityDefinition(ScreenEntityDefinition screenEntityDefinition);

	Map<String, D> getEntitiesDefinitions();
}
