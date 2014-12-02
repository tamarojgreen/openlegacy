package org.openlegacy.terminal.validations;

import org.openlegacy.terminal.definitions.ScreenEntityDefinition;
import org.openlegacy.terminal.definitions.ScreenFieldDefinition;
import org.openlegacy.validations.EntityValidator;
import org.openlegacy.validations.Validation;

import java.util.List;

public class ScreenEntityValidator implements EntityValidator<ScreenEntityDefinition, ScreenFieldDefinition> {

	@Override
	public List<Validation> getValidations(ScreenEntityDefinition entityDefinition) {
		return null;
	}

}
