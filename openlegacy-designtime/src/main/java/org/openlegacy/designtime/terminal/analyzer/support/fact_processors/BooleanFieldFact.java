package org.openlegacy.designtime.terminal.analyzer.support.fact_processors;

import org.openlegacy.designtime.terminal.analyzer.ScreenFact;
import org.openlegacy.terminal.TerminalField;
import org.openlegacy.terminal.definitions.ScreenFieldDefinition;

public class BooleanFieldFact implements ScreenFact {

	private ScreenFieldDefinition screenFieldDefinition;
	private TerminalField trueFalseTextField;
	private String regex;

	public BooleanFieldFact(ScreenFieldDefinition screenFieldDefinition,TerminalField trueFalseTextField, String regex) {
		this.screenFieldDefinition = screenFieldDefinition;
		this.trueFalseTextField = trueFalseTextField;
		this.regex = regex;
	}

	public ScreenFieldDefinition getScreenFieldDefinition() {
		return screenFieldDefinition;
	}
	public TerminalField getTrueFalseTextField() {
		return trueFalseTextField;
	}
	
	public String getRegex() {
		return regex;
	}
}
