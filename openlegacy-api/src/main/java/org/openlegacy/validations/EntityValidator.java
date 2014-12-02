package org.openlegacy.validations;

import org.openlegacy.EntityDefinition;
import org.openlegacy.definitions.FieldDefinition;

import java.util.List;

public interface EntityValidator<E extends EntityDefinition<F>, F extends FieldDefinition> {

	List<Validation> getValidations(E entityDefinition);
}
