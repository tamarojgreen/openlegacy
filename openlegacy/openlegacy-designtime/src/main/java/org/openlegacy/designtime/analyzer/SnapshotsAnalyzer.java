package org.openlegacy.designtime.analyzer;

import org.openlegacy.EntityDefinition;
import org.openlegacy.Snapshot;
import org.openlegacy.terminal.definitions.ScreenEntityDefinition;
import org.openlegacy.terminal.module.TerminalSessionTrail;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

public interface SnapshotsAnalyzer<S extends Snapshot, D extends EntityDefinition<?>> {

	Map<String, D> analyzeSnapshots(List<S> snapshots);

	Map<String, ScreenEntityDefinition> analyzeTrail(InputStream trailFile);

	Map<String, ScreenEntityDefinition> analyzeTrail(TerminalSessionTrail trail);

}
