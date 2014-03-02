package org.openlegacy.designtime.mains;

import org.openlegacy.EntityDefinition;
import org.openlegacy.definitions.FieldDefinition;

public class ServiceEntityFieldParameter implements ServiceParameter {

	private EntityDefinition<?> entityDefinition;
	private FieldDefinition fieldDefinition;

	public ServiceEntityFieldParameter(EntityDefinition<?> entityDefinition, FieldDefinition fieldDefinition) {
		this.entityDefinition = entityDefinition;
		this.fieldDefinition = fieldDefinition;
	}

	EntityDefinition<?> getEntityDefinition() {
		return entityDefinition;
	}

	public FieldDefinition getFieldDefinition() {
		return fieldDefinition;
	}
}
