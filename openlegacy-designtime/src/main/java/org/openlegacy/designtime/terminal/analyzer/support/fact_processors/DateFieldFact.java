package org.openlegacy.designtime.terminal.analyzer.support.fact_processors;

import org.openlegacy.designtime.terminal.analyzer.ScreenFact;
import org.openlegacy.terminal.TerminalField;
import org.openlegacy.terminal.definitions.ScreenFieldDefinition;

public class DateFieldFact implements ScreenFact {

	private TerminalField labelField;
	private TerminalField leftTerminalField;
	private ScreenFieldDefinition leftField;
	private ScreenFieldDefinition middleField;
	private ScreenFieldDefinition rightField;

	public DateFieldFact(TerminalField labelField, TerminalField leftTerminalField, ScreenFieldDefinition leftField,
			ScreenFieldDefinition middleField, ScreenFieldDefinition rightField) {
		this.labelField = labelField;
		this.leftTerminalField = leftTerminalField;
		this.leftField = leftField;
		this.middleField = middleField;
		this.rightField = rightField;
	}

	public TerminalField getLabelField() {
		return labelField;
	}

	public TerminalField getLeftTerminalField() {
		return leftTerminalField;
	}

	public ScreenFieldDefinition getLeftField() {
		return leftField;
	}

	public ScreenFieldDefinition getMiddleField() {
		return middleField;
	}

	public ScreenFieldDefinition getRightField() {
		return rightField;
	}

}
