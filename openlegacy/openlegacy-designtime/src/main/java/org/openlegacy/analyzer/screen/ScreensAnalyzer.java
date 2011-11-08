package org.openlegacy.analyzer.screen;

import org.openlegacy.terminal.TerminalSnapshot;
import org.openlegacy.terminal.definitions.ScreenEntityDefinition;

import java.util.List;

public interface ScreensAnalyzer {

	List<ScreenEntityDefinition> analyzeScreens(List<TerminalSnapshot> snapshots);
}
