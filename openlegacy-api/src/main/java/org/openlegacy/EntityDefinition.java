package org.openlegacy;

import org.openlegacy.definitions.FieldDefinition;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface EntityDefinition<D extends FieldDefinition> {

	String getEntityName();

	String getPackageName();

	String getDisplayName();

	Class<?> getEntityClass();

	String getEntityClassName();

	Class<? extends EntityType> getType();

	String getTypeName();

	/**
	 * Map of field name -> field mapping definition
	 * 
	 * @return
	 */
	Map<String, D> getFieldsDefinitions();

	D getFirstFieldDefinition(Class<? extends FieldType> fieldType);

	List<? extends FieldDefinition> getKeys();

	List<EntityDefinition<?>> getChildEntitiesDefinitions();

	/**
	 * Fetch all children, grand children and so. Used for generating composite page also with child with indirect connection
	 * 
	 * @return
	 */
	Set<EntityDefinition<?>> getAllChildScreensDefinitions();
}
