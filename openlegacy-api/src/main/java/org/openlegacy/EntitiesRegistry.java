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

import org.openlegacy.definitions.FieldDefinition;
import org.openlegacy.exceptions.RegistryException;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * A common interface for defining a registry for entities, and retrieving an entity class by name. <br/>
 * An entity is class which can describe a legacy screen, a Legacy program, or any other legacy asset
 * 
 * @param <H>
 *            The managed entities definitions which this registry stores
 * @param <D>
 *            The field definitions used by the managed entities definitions <H>
 * 
 * @author Roi Mor
 * 
 * @see EntityDefinition
 */
public interface EntitiesRegistry<H extends EntityDefinition<D>, D extends FieldDefinition> {

	/**
	 * Gets all entities within the registry
	 * 
	 * @return all entities within the registry
	 */
	public Collection<H> getEntitiesDefinitions();

	/**
	 * Return an entity class by entity name
	 * 
	 * @param entityName
	 *            the requested entity name
	 * @return entity class
	 */
	Class<?> getEntityClass(String entityName);

	/**
	 * Return the entity name for a given entity class
	 * 
	 * @param entity
	 *            an entity class
	 * @return entity name
	 */
	String getEntityName(Class<?> entity);

	/**
	 * Returns a set of entities classes which matches the given {@link EntityType}. Useful for retrieving all entities of same
	 * type
	 * 
	 * @param entityType
	 *            the requested entities type
	 * @return a list of entities in the specified type
	 */
	Set<Class<?>> getByType(Class<? extends EntityType> entityType);

	/**
	 * Returns a single entity with the specified {@link EntityType}.
	 * 
	 * @param entityType
	 *            the requested entity type
	 * @return a matching entity class to the given {@link EntityType}
	 * @throws RegistryException
	 *             thrown when more then entities of the specified type is found, a
	 */
	H getSingleEntityDefinition(Class<? extends EntityType> entityType) throws RegistryException;

	/**
	 * Adds an {@link EntityDefinition} to the registry
	 * 
	 * @param entityDefinition
	 *            the entity definition to add
	 */
	void add(H entityDefinition);

	/**
	 * Returns an entity definition for the given class
	 * 
	 * @param entityClass
	 * @return an entity definitions for the given entity class
	 */
	H get(Class<?> entityClass);

	/**
	 * Returns an entity definition for the given entity name
	 * 
	 * @param entityName
	 * @return an entity definitions for the given entity class
	 */
	H get(String entityName);

	/**
	 * Clean the registry from it's content. Called when registry is loaded/reloaded
	 */
	void clear();

	/**
	 * Returns a list of java packages names the registry is combined of
	 * 
	 * @return a list of java packages names
	 */
	List<String> getPackages();

	boolean isDirty();

	public boolean contains(Class<?> beanClass);

}
