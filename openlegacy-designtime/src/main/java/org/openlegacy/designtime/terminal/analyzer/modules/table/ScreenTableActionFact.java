package org.openlegacy.designtime.terminal.analyzer.modules.table;

import org.openlegacy.designtime.terminal.analyzer.support.fact_processors.ScreenActionFact;
import org.openlegacy.terminal.TerminalPosition;
import org.openlegacy.terminal.definitions.ScreenTableDefinition;

public class ScreenTableActionFact extends ScreenActionFact {

	private ScreenTableDefinition screenTableDefinition;

	public ScreenTableActionFact(ScreenTableDefinition screenTableDefinition, String captionAction,
			TerminalPosition terminalPosition, String trueFalseRegex) {
		super(captionAction, terminalPosition, trueFalseRegex);
		this.screenTableDefinition = screenTableDefinition;
	}

	public ScreenTableDefinition getScreenTableDefinition() {
		return screenTableDefinition;
	}

}
