package org.openlegacy.designtime.terminal.analyzer.support;

import org.openlegacy.designtime.analyzer.SnapshotsAnalyzerContext;
import org.openlegacy.designtime.terminal.model.ScreenEntityDesigntimeDefinition;
import org.openlegacy.terminal.TerminalSnapshot;
import org.openlegacy.terminal.definitions.ScreenEntityDefinition;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class TerminalSnapshotsAnalyzerContext implements SnapshotsAnalyzerContext<TerminalSnapshot, ScreenEntityDefinition> {

	private static final String SCREEN = "Screen";
	private Collection<TerminalSnapshot> activeSnapshots;
	private Map<String, List<ScreenEntityDesigntimeDefinition>> entitiesDefinitions = new HashMap<String, List<ScreenEntityDesigntimeDefinition>>();

	public Collection<TerminalSnapshot> getActiveSnapshots() {
		return activeSnapshots;
	}

	public void setActiveSnapshots(Collection<TerminalSnapshot> snapshots) {
		activeSnapshots = snapshots;

	}

	public void addEntityDefinition(String desiredEntityName, ScreenEntityDesigntimeDefinition screenEntityDefinition) {

		if (desiredEntityName == null) {
			desiredEntityName = suggestEntityName(screenEntityDefinition);
		}

		List<ScreenEntityDesigntimeDefinition> matchingDefinitions = entitiesDefinitions.get(desiredEntityName);
		if (matchingDefinitions == null) {
			matchingDefinitions = new ArrayList<ScreenEntityDesigntimeDefinition>();
			entitiesDefinitions.put(desiredEntityName, matchingDefinitions);
		}
		matchingDefinitions.add(screenEntityDefinition);

	}

	private String suggestEntityName(ScreenEntityDefinition screenEntityDefinition) {
		String desiredEntityName;
		Integer sequence = screenEntityDefinition.getSnapshot().getSequence();
		if (sequence != null) {
			desiredEntityName = SCREEN + sequence;
		} else {
			desiredEntityName = SCREEN + entitiesDefinitions.size();
		}
		return desiredEntityName;
	}

	private static String findFreeEntityName(String entityName, Map<String, ScreenEntityDefinition> entitiesDefinitions) {
		int nameCount = 1;
		String tempEntityName = entityName;
		String baseEntityName = entityName;
		while (entitiesDefinitions.get(tempEntityName) != null) {
			tempEntityName = baseEntityName + nameCount++;
		}
		return tempEntityName;
	}

	/**
	 * Once all screen entities definitions are requested, flatten all the entities, and provide each entity definition a unique
	 * name.
	 * 
	 */
	public Map<String, ScreenEntityDefinition> getEntitiesDefinitions() {
		Set<Entry<String, List<ScreenEntityDesigntimeDefinition>>> entitiesByNameDefiniton = entitiesDefinitions.entrySet();
		Map<String, ScreenEntityDefinition> entitiesDefinitionsResult = new HashMap<String, ScreenEntityDefinition>();

		for (Entry<String, List<ScreenEntityDesigntimeDefinition>> entry : entitiesByNameDefiniton) {
			List<ScreenEntityDesigntimeDefinition> definitions = entry.getValue();
			// when combining few screen definition with the same desired entity name, give priority to entity with a lower
			// snapshot sequence
			Collections.sort(definitions, TerminalSnapshotSequenceComparator.instance());

			for (ScreenEntityDesigntimeDefinition screenEntityDefinition : definitions) {
				String actualEntityName = findFreeEntityName(entry.getKey(), entitiesDefinitionsResult);
				screenEntityDefinition.setEntityName(actualEntityName);
				entitiesDefinitionsResult.put(actualEntityName, screenEntityDefinition);
			}
		}
		return entitiesDefinitionsResult;
	}

	private static class TerminalSnapshotSequenceComparator implements Comparator<ScreenEntityDefinition> {

		private static TerminalSnapshotSequenceComparator instance = new TerminalSnapshotSequenceComparator();

		public static TerminalSnapshotSequenceComparator instance() {
			return instance;
		}

		public int compare(ScreenEntityDefinition o1, ScreenEntityDefinition o2) {
			Integer sequence1 = o1.getSnapshot().getSequence();
			Integer sequence2 = o2.getSnapshot().getSequence();
			if (sequence1 == null || sequence2 == null) {
				return 0;
			}
			return sequence1 - sequence2;
		}

	}
}
