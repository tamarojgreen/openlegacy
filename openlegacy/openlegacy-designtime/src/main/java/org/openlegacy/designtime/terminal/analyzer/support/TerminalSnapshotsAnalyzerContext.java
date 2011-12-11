package org.openlegacy.designtime.terminal.analyzer.support;

import org.openlegacy.designtime.analyzer.SnapshotsAnalyzerContext;
import org.openlegacy.designtime.terminal.model.ScreenEntityDesigntimeDefinition;
import org.openlegacy.terminal.TerminalSnapshot;
import org.openlegacy.terminal.definitions.ScreenEntityDefinition;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class TerminalSnapshotsAnalyzerContext implements SnapshotsAnalyzerContext<TerminalSnapshot, ScreenEntityDefinition> {

	private static final String SCREEN = "Screen";
	private Collection<TerminalSnapshot> activeSnapshots;
	private Map<String, ScreenEntityDefinition> entitiesDefinitions = new HashMap<String, ScreenEntityDefinition>();

	public Collection<TerminalSnapshot> getActiveSnapshots() {
		return activeSnapshots;
	}

	public void setActiveSnapshots(Collection<TerminalSnapshot> snapshots) {
		activeSnapshots = snapshots;

	}

	public void addEntityDefinition(ScreenEntityDesigntimeDefinition screenEntityDefinition) {
		if (entitiesDefinitions.containsValue(screenEntityDefinition)) {
			entitiesDefinitions.remove(screenEntityDefinition);
		}
		String entityName = screenEntityDefinition.getEntityName();
		entityName = suggestEntityName(screenEntityDefinition);
		screenEntityDefinition.setEntityName(entityName);
		entitiesDefinitions.put(entityName, screenEntityDefinition);
	}

	private String suggestEntityName(ScreenEntityDesigntimeDefinition screenEntityDefinition) {
		String entityName = screenEntityDefinition.getEntityName();

		if (entityName == null) {
			Integer sequence = screenEntityDefinition.getSnapshot().getSequence();
			if (sequence != null) {
				entityName = SCREEN + sequence;
			} else {
				entityName = SCREEN + entitiesDefinitions.size();
			}
		}

		entityName = findFreeEntityName(entityName);
		return entityName;
	}

	private String findFreeEntityName(String entityName) {
		int nameCount = 1;
		while (entitiesDefinitions.get(entityName) != null) {
			entityName = entityName + nameCount++;
		}
		return entityName;
	}

	public Map<String, ScreenEntityDefinition> getEntitiesDefinitions() {
		return entitiesDefinitions;
	}

}
