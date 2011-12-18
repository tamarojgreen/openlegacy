package org.openlegacy.designtime.analyzer;

import org.openlegacy.EntityDefinition;
import org.openlegacy.Snapshot;
import org.openlegacy.designtime.terminal.model.ScreenEntityDesigntimeDefinition;
import org.openlegacy.terminal.TerminalSnapshot;

import java.util.Collection;
import java.util.Map;

public interface SnapshotsAnalyzerContext<S extends Snapshot, D extends EntityDefinition<?>> {

	Collection<S> getActiveSnapshots();

	void setActiveSnapshots(Collection<S> snapshots);

	void addEntityDefinition(String desiredEntityName, ScreenEntityDesigntimeDefinition screenEntityDefinition);

	void finalizeEntitiesDefinitions();

	Map<String, D> getEntitiesDefinitions();

	Collection<TerminalSnapshot> getAccessedFromSnapshots(Collection<TerminalSnapshot> incomingSnapshots);
}
