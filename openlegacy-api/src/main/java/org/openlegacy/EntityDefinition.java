package org.openlegacy;

import org.openlegacy.definitions.FieldDefinition;
import org.openlegacy.loaders.ClassAnnotationsLoader;
import org.openlegacy.loaders.FieldAnnotationsLoader;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * An entity definition is the meta data on an entity class, typically gathered from annotations on the entity class, Loaded
 * usually using {@link ClassAnnotationsLoader} or {@link FieldAnnotationsLoader} Example: store screen identifications, screen
 * actions, field positions
 * 
 * @author Roi Mor
 * 
 * @param <D>
 *            The field java type in use
 */
public interface EntityDefinition<D extends FieldDefinition> {

	/**
	 * The entity name
	 * 
	 * @return entity name
	 */
	String getEntityName();

	/**
	 * The java package name of the entity
	 * 
	 * @return java package name of the entity
	 */
	String getPackageName();

	/**
	 * The entity display name. If not specified default formatted display value should be supplied
	 * 
	 * @return
	 */
	String getDisplayName();

	/**
	 * The entity class. Not relevant for design time
	 * 
	 * @return entity class
	 */
	Class<?> getEntityClass();

	/**
	 * The entity class name.
	 * 
	 * @return
	 */
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
	Set<EntityDefinition<?>> getAllChildEntitiesDefinitions();
}
