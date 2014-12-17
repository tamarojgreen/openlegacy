package org.openlegacy.terminal.validations.support;

import org.openlegacy.terminal.definitions.ScreenEntityDefinition;
import org.openlegacy.terminal.definitions.ScreenFieldDefinition;
import org.openlegacy.terminal.validations.ScreenValidateRule;
import org.openlegacy.validations.EntityValidator;
import org.openlegacy.validations.support.AbstractEntityValidator;

import java.util.List;

public class ScreenEntityValidator extends AbstractEntityValidator<ScreenEntityDefinition, ScreenFieldDefinition> implements EntityValidator<ScreenEntityDefinition, ScreenFieldDefinition> {

	public static final String GENERAL = "general";
	public static final String FIELDS = "fields";
	public static final String TABLE = "table";
	public static final String IDENTIFIERS = "identifiers";
	public static final String ACTIONS = "actions";

	private List<? extends ScreenValidateRule> validationRules;

	@Override
	public List<? extends ScreenValidateRule> getValidationRules() {
		return validationRules;
	}

	public void setValidationRules(List<? extends ScreenValidateRule> validationRules) {
		this.validationRules = validationRules;
	}
}
