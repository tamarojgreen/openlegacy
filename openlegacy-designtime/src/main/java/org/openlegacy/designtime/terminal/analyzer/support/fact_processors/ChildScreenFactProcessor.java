package org.openlegacy.designtime.terminal.analyzer.support.fact_processors;

import org.openlegacy.designtime.terminal.analyzer.ScreenFact;
import org.openlegacy.designtime.terminal.analyzer.ScreenFactProcessor;
import org.openlegacy.designtime.terminal.model.ScreenEntityDesigntimeDefinition;

public class ChildScreenFactProcessor implements ScreenFactProcessor {

	public boolean accept(ScreenFact screenFact) {
		return screenFact instanceof ChildScreenFact;
	}

	public void process(ScreenEntityDesigntimeDefinition screenEntityDefinition, ScreenFact screenFact) {
		ChildScreenFact childScreenFact = (ChildScreenFact)screenFact;

		screenEntityDefinition.getChildScreensDefinitions().add(childScreenFact.getChildScreenEntityDefinition());
	}

}
