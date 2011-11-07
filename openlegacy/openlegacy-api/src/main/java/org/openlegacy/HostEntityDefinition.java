package org.openlegacy;

import java.util.Map;

public interface HostEntityDefinition<D extends FieldDefinition> {

	String getEntityName();

	String getDisplayName();

	Class<?> getEntityClass();

	Class<? extends HostEntityType> getType();

	/**
	 * Map of field name -> field mapping definition
	 * 
	 * @return
	 */
	Map<String, D> getFieldsDefinitions();

	D getFirstFieldDefinition(Class<? extends FieldType> fieldType);

}
