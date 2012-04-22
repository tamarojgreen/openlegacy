package org.openlegacy.designtime.terminal.analyzer.support.fact_processors;

import org.openlegacy.designtime.terminal.analyzer.ScreenFact;
import org.openlegacy.designtime.terminal.analyzer.ScreenFactProcessor;
import org.openlegacy.designtime.terminal.analyzer.TerminalActionAnalyzer;
import org.openlegacy.designtime.terminal.model.ScreenEntityDesigntimeDefinition;
import org.openlegacy.terminal.TerminalPosition;
import org.openlegacy.terminal.definitions.TerminalActionDefinition;

import javax.inject.Inject;

public abstract class AbstractActionFactProcessor implements ScreenFactProcessor {

	@Inject
	private TerminalActionAnalyzer terminalActionAnalyzer;

	public boolean accept(ScreenFact screenFact) {
		return screenFact.getClass() == ScreenActionFact.class;
	}

	public void process(ScreenEntityDesigntimeDefinition screenEntityDefinition, ScreenFact screenFact) {
		ScreenActionFact screenActionFact = (ScreenActionFact)screenFact;

		TerminalActionDefinition actionDefinition = buildActionDefinition(screenActionFact.getAction(),
				screenActionFact.getCaption(), screenActionFact.getTerminalPosition());

		if (actionDefinition != null) {
			addAction(screenEntityDefinition, screenActionFact, actionDefinition);
		}

	}

	protected abstract TerminalActionDefinition buildActionDefinition(String action, String caption,
			TerminalPosition terminalPosition);

	protected abstract void addAction(ScreenEntityDesigntimeDefinition screenEntityDefinition, ScreenActionFact screenActionFact,
			TerminalActionDefinition actionDefinition);

	protected TerminalActionAnalyzer getTerminalActionAnalyzer() {
		return terminalActionAnalyzer;
	}
}
