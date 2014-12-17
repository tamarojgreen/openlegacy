package org.openlegacy.validations.support;

import org.openlegacy.EntityDefinition;
import org.openlegacy.definitions.FieldDefinition;
import org.openlegacy.validations.EntityValidator;
import org.openlegacy.validations.ValidateRule;
import org.openlegacy.validations.Validation;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractEntityValidator<E extends EntityDefinition<F>, F extends FieldDefinition> implements EntityValidator<E, F> {

	@SuppressWarnings("unchecked")
	@Override
	public List<Validation> validate(EntityDefinition<?> entityDefinition) {

		Assert.notNull(getValidationRules(), "No validation rules configured");

		List<Validation> validations = new ArrayList<Validation>();

		for (ValidateRule<E, F> rule : getValidationRules()) {
			validations.addAll(rule.validate((E)entityDefinition));
		}
		return validations;
	}

	public abstract List<? extends ValidateRule<E, F>> getValidationRules();

}
