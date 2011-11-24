package org.openlegacy.designtime.terminal.analyzer.support;

import org.openlegacy.designtime.analyzer.SnapshotsAnalyzerContext;
import org.openlegacy.terminal.TerminalSnapshot;
import org.openlegacy.terminal.definitions.ScreenEntityDefinition;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class TerminalSnapshotsAnalyzerContext implements SnapshotsAnalyzerContext<TerminalSnapshot, ScreenEntityDefinition> {

	private Collection<TerminalSnapshot> activeSnapshots;
	private Map<String, ScreenEntityDefinition> entitiesDefinitions = new HashMap<String, ScreenEntityDefinition>();

	public Collection<TerminalSnapshot> getActiveSnapshots() {
		return activeSnapshots;
	}

	public void setActiveSnapshots(Collection<TerminalSnapshot> snapshots) {
		activeSnapshots = snapshots;

	}

	public Map<String, ScreenEntityDefinition> getEntitiesDefinitions() {
		return entitiesDefinitions;
	}

}
