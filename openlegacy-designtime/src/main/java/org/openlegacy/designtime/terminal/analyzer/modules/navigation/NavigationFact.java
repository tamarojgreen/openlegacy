package org.openlegacy.designtime.terminal.analyzer.modules.navigation;

import org.openlegacy.designtime.terminal.analyzer.ScreenFact;
import org.openlegacy.designtime.terminal.model.ScreenEntityDesigntimeDefinition;
import org.openlegacy.terminal.TerminalSnapshot;

public class NavigationFact implements ScreenFact {

	private ScreenEntityDesigntimeDefinition accessedFromScreenEntityDefinition;
	private TerminalSnapshot accessedFromSnapshot;

	public NavigationFact(ScreenEntityDesigntimeDefinition screenEntityDefinition, TerminalSnapshot accessedFromSnapshot) {
		this.accessedFromScreenEntityDefinition = screenEntityDefinition;
		this.accessedFromSnapshot = accessedFromSnapshot;
	}

	public ScreenEntityDesigntimeDefinition getAccessedFromScreenEntityDefinition() {
		return accessedFromScreenEntityDefinition;
	}

	public TerminalSnapshot getAccessedFromSnapshot() {
		return accessedFromSnapshot;
	}

}
