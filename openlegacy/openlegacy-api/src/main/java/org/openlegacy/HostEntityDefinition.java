package org.openlegacy;

import java.util.Map;

public interface HostEntityDefinition<D extends FieldDefinition> {

	String getHostEntityName();

	Class<?> getHostEntityClass();

	Class<? extends HostEntityType> getType();

	/**
	 * Map of field name -> field mapping definition
	 * 
	 * @return
	 */
	Map<String, D> getFieldDefinitions();

	D findFirstFieldDefinitionByType(Class<? extends FieldType> fieldType);

	Object getFieldValue(Object entityInstance, FieldDefinition fieldDefinition);
}
