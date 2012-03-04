package org.openlegacy.designtime.terminal.analyzer.support.fact_processors;

import org.openlegacy.designtime.terminal.analyzer.ScreenFact;
import org.openlegacy.terminal.TerminalPosition;

public class ScreenActionFact implements ScreenFact {

	private String captionAction;
	private TerminalPosition terminalPosition;
	private String regex;

	public ScreenActionFact(String captionAction, TerminalPosition terminalPosition, String trueFalseRegex) {
		this.captionAction = captionAction;
		this.terminalPosition = terminalPosition;
		this.regex = trueFalseRegex;
	}

	public String getCaptionAction() {
		return captionAction;
	}

	public TerminalPosition getTerminalPosition() {
		return terminalPosition;
	}

	public String getRegex() {
		return regex;
	}
}
