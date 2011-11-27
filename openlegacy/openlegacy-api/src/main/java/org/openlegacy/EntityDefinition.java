package org.openlegacy;

import java.util.Map;

public interface EntityDefinition<D extends FieldDefinition> {

	String getEntityName();

	String getPackageName();

	String getDisplayName();

	Class<?> getEntityClass();

	Class<? extends EntityType> getType();

	/**
	 * Map of field name -> field mapping definition
	 * 
	 * @return
	 */
	Map<String, D> getFieldsDefinitions();

	D getFirstFieldDefinition(Class<? extends FieldType> fieldType);

}
