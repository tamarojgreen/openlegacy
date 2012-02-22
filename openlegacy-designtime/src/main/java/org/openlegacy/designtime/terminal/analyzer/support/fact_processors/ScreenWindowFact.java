package org.openlegacy.designtime.terminal.analyzer.support.fact_processors;

import org.openlegacy.designtime.terminal.analyzer.ScreenFact;
import org.openlegacy.terminal.TerminalField;

public class ScreenWindowFact implements ScreenFact {

	private TerminalField topBorderField;
	private TerminalField buttomBorderField;

	public ScreenWindowFact(TerminalField topBorderField, TerminalField buttomBorderField) {
		this.topBorderField = topBorderField;
		this.buttomBorderField = buttomBorderField;
	}

	public TerminalField getTopBorderField() {
		return topBorderField;
	}

	public TerminalField getButtomBorderField() {
		return buttomBorderField;
	}
}
