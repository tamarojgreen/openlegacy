package org.openlegacy.designtime.terminal.analyzer.support.fact_processors;

import org.openlegacy.designtime.terminal.analyzer.ScreenFact;
import org.openlegacy.terminal.TerminalField;

import java.util.List;

public class ScreenIdentifiersFact implements ScreenFact {

	private List<TerminalField> identifiers;

	public ScreenIdentifiersFact(List<TerminalField> identifiers) {
		this.identifiers = identifiers;
	}

	public List<TerminalField> getIdentifiers() {
		return identifiers;
	}
}
