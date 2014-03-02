package org.openlegacy.designtime.mains;

import org.openlegacy.EntityDefinition;

public class ServiceEntityParameter implements ServiceParameter {

	private EntityDefinition<?> entityDefinition;

	public ServiceEntityParameter(EntityDefinition<?> entityDefinition) {
		this.entityDefinition = entityDefinition;
	}

	public EntityDefinition<?> getEntityDefinition() {
		return entityDefinition;
	}
}
