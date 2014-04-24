package org.openlegacy.designtime.mains;

import org.openlegacy.EntityDefinition;
import org.openlegacy.definitions.PartEntityDefinition;

/**
 * @author Ivan Bort
 * 
 */
public class ServicePartParameter implements ServiceParameter {

	private EntityDefinition<?> entityDefinition;
	private PartEntityDefinition<?> partEntityDefinition;

	public ServicePartParameter(EntityDefinition<?> entityDefinition, PartEntityDefinition<?> partEntityDefinition) {
		this.entityDefinition = entityDefinition;
		this.partEntityDefinition = partEntityDefinition;
	}

	public EntityDefinition<?> getEntityDefinition() {
		return entityDefinition;
	}

	public PartEntityDefinition<?> getPartEntityDefinition() {
		return partEntityDefinition;
	}

}
