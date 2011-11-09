package org.openlegacy.analyzer.screen;

import org.openlegacy.Snapshot;
import org.openlegacy.terminal.definitions.ScreenEntityDefinition;

import java.util.List;

public interface SnapshotsAnalyzer {

	List<ScreenEntityDefinition> analyzeSnapshots(List<Snapshot> snapshots);
}
