package org.openlegacy.designtime.terminal.analyzer;

import org.openlegacy.designtime.terminal.model.ScreenEntityDesigntimeDefinition;

public interface ScreenFactAnalyzer {

	void analyze(ScreenEntityDesigntimeDefinition screenEntityDefinition, ScreenFact screenFact);
}
