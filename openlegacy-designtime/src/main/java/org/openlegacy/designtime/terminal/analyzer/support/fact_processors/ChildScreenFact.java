package org.openlegacy.designtime.terminal.analyzer.support.fact_processors;

import org.openlegacy.designtime.terminal.analyzer.ScreenFact;
import org.openlegacy.designtime.terminal.model.ScreenEntityDesigntimeDefinition;

public class ChildScreenFact implements ScreenFact {

	private ScreenEntityDesigntimeDefinition childScreenEntityDefinition;

	public ChildScreenFact(ScreenEntityDesigntimeDefinition childScreenEntityDefinition) {
		this.childScreenEntityDefinition = childScreenEntityDefinition;
	}

	public ScreenEntityDesigntimeDefinition getChildScreenEntityDefinition() {
		return childScreenEntityDefinition;
	}
}
