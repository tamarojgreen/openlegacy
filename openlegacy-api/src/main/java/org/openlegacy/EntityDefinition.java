/*******************************************************************************
 * Copyright (c) 2012 OpenLegacy Inc.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     OpenLegacy Inc. - initial API and implementation
 *******************************************************************************/
package org.openlegacy;

import org.openlegacy.definitions.ActionDefinition;
import org.openlegacy.definitions.FieldDefinition;
import org.openlegacy.definitions.PartEntityDefinition;
import org.openlegacy.loaders.ClassAnnotationsLoader;
import org.openlegacy.loaders.FieldAnnotationsLoader;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * An entity definition is the meta-data on an entity class, typically gathered from annotations on the entity class. Loaded
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
	 * @return display name
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
	 * @return entity class name
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

	/**
	 * Return all fields of the given type
	 * 
	 * @param fieldType
	 * @return
	 */
	List<? extends FieldDefinition> getFieldDefinitions(Class<? extends FieldType> fieldType);

	/**
	 * list of field definitions which are marked as keys of the entity
	 * 
	 * @return list of key field definitions
	 */
	List<? extends FieldDefinition> getKeys();

	/**
	 * List of child entities definitions if the entity has child entities
	 * 
	 * @return list of child entities definitions
	 */
	List<EntityDefinition<?>> getChildEntitiesDefinitions();

	/**
	 * Fetch all children, grand children and so. Used for generating composite page also with child with indirect connection
	 * 
	 * @return list of recursive child entities definitions
	 */
	Set<EntityDefinition<?>> getAllChildEntitiesDefinitions();

	List<ActionDefinition> getActions();

	ActionDefinition getAction(Class<?> actionClass);

	/**
	 * field name -> part definition
	 * 
	 * @return map of screen parts definitions of the screen entity
	 */
	Map<String, PartEntityDefinition<D>> getPartsDefinitions();
}
