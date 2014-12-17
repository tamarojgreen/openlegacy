package org.openlegacy.terminal.validations.support;

import org.openlegacy.terminal.definitions.ScreenEntityDefinition;
import org.openlegacy.terminal.definitions.ScreenFieldDefinition;
import org.openlegacy.terminal.validations.ScreenValidateRule;
import org.openlegacy.validations.SimpleValidation;
import org.openlegacy.validations.Validation;
import org.openlegacy.validations.Validation.ValidationType;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class FieldOutsideRangeRule implements ScreenValidateRule {

	@Override
	public List<Validation> validate(ScreenEntityDefinition entityDefinition) {
		Collection<ScreenFieldDefinition> fields = entityDefinition.getFieldsDefinitions().values();
		List<Validation> validations = new ArrayList<Validation>();
		for (ScreenFieldDefinition screenFieldDefinition : fields) {
			if (!entityDefinition.getScreenSize().contains(screenFieldDefinition.getPosition())) {
				String name = screenFieldDefinition.getName();
				validations.add(new SimpleValidation(MessageFormat.format("Field {0} is outside screen bounds", name), name,
						ScreenEntityValidator.FIELDS, ValidationType.ERROR));
			}
			if (screenFieldDefinition.getEndPosition() != null
					&& !entityDefinition.getScreenSize().contains(screenFieldDefinition.getEndPosition())) {
				String name = screenFieldDefinition.getName();
				validations.add(new SimpleValidation(MessageFormat.format("Field {0} is outside screen bounds", name), name,
						ScreenEntityValidator.FIELDS, ValidationType.ERROR));
			}
		}
		return validations;
	}
}
