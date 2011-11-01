package org.openlegacy;

import java.util.Map;

public interface EntityDefinition {

	String getEntityName();

	Class<?> getEntityClass();

	Class<? extends EntityType> getType();

	/**
	 * Map of field name -> field mapping definition
	 * 
	 * @return
	 */
	Map<String, ? extends FieldDefinition> getFieldsDefinitions();

	FieldDefinition getFirstFieldDefinition(Class<? extends FieldType> fieldType);

}
