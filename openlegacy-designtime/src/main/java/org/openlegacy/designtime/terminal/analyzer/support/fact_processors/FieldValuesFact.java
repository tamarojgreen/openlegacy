package org.openlegacy.designtime.terminal.analyzer.support.fact_processors;

import org.openlegacy.designtime.terminal.analyzer.ScreenFact;
import org.openlegacy.terminal.definitions.FieldAssignDefinition;
import org.openlegacy.terminal.definitions.ScreenEntityDefinition;

public class FieldValuesFact implements ScreenFact {

	private FieldAssignDefinition fieldAssignDefinition;
	private ScreenEntityDefinition lookupWindowScreenDefinition;

	public FieldValuesFact(FieldAssignDefinition fieldAssignDefinition, ScreenEntityDefinition lookupWindowScreenDefinition) {
		this.fieldAssignDefinition = fieldAssignDefinition;
		this.lookupWindowScreenDefinition = lookupWindowScreenDefinition;
	}

	public FieldAssignDefinition getFieldAssignDefinition() {
		return fieldAssignDefinition;
	}

	public ScreenEntityDefinition getLookupWindowScreenDefinition() {
		return lookupWindowScreenDefinition;
	}
}
