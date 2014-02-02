/*******************************************************************************
 * Copyright (c) 2014 OpenLegacy Inc.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     OpenLegacy Inc. - initial API and implementation
 *******************************************************************************/
package org.openlegacy.support;

import org.apache.commons.collections.map.CaseInsensitiveMap;
import org.openlegacy.EntitiesRegistry;
import org.openlegacy.EntityDefinition;
import org.openlegacy.EntityType;
import org.openlegacy.definitions.FieldDefinition;
import org.openlegacy.definitions.PartEntityDefinition;
import org.openlegacy.exceptions.RegistryException;
import org.openlegacy.utils.ProxyUtil;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * An abstract implementation of entities registry
 * 
 */
public abstract class AbstractEntitiesRegistry<E extends EntityDefinition<D>, D extends FieldDefinition, P extends PartEntityDefinition<D>> implements EntitiesRegistry<E, D, P>, Serializable {

	private static final long serialVersionUID = 1L;

	private final Map<Class<?>, E> entitiesDefinitions = new HashMap<Class<?>, E>();

	private List<String> packages;

	@SuppressWarnings("unchecked")
	private Map<String, Class<?>> entities = new CaseInsensitiveMap();
	private Map<Class<?>, String> reversedEntities = new HashMap<Class<?>, String>();

	private Map<Class<?>, Collection<FieldDefinition>> allFieldsOfType = new HashMap<Class<?>, Collection<FieldDefinition>>();

	private final Map<Class<?>, P> partDefinitions = new HashMap<Class<?>, P>();

	// map of entities by type
	private Map<Class<? extends EntityType>, Set<Class<?>>> entitiesByTypes = new HashMap<Class<? extends EntityType>, Set<Class<?>>>();

	private boolean dirty = false;

	private void add(String entityName, Class<?> entity) {
		entities.put(entityName, entity);
		reversedEntities.put(entity, entityName);
	}

	protected void addToTypes(Class<? extends EntityType> entityType, Class<?> entity) {
		Set<Class<?>> relevantEntities = entitiesByTypes.get(entityType);
		if (relevantEntities == null) {
			relevantEntities = new HashSet<Class<?>>();
			entitiesByTypes.put(entityType, relevantEntities);
		}
		relevantEntities.add(entity);

	}

	public Class<?> getEntityClass(String entityName) {
		if (entityName.contains(".")) {
			entityName = entityName.substring(entityName.lastIndexOf(".") + 1);
		}
		return entities.get(entityName);
	}

	public String getEntityName(Class<?> entity) {
		return reversedEntities.get(ProxyUtil.getOriginalClass(entity));
	}

	public void add(E entityDefinition) {
		add(entityDefinition.getEntityName(), entityDefinition.getEntityClass());
		addToTypes(entityDefinition.getType(), entityDefinition.getEntityClass());
		entitiesDefinitions.put(entityDefinition.getEntityClass(), entityDefinition);
	}

	public E get(Class<?> entityClass) {
		entityClass = ProxyUtil.getOriginalClass(entityClass);
		return entitiesDefinitions.get(entityClass);
	}

	public E get(String entityName) {
		Class<?> entityClass = getEntityClass(entityName);
		return entitiesDefinitions.get(entityClass);
	}

	public Collection<E> getEntitiesDefinitions() {
		return entitiesDefinitions.values();
	}

	public Set<Class<?>> getByType(Class<? extends EntityType> entityType) {
		return entitiesByTypes.get(entityType);
	}

	public E getSingleEntityDefinition(Class<? extends EntityType> entityType) throws RegistryException {
		Set<Class<?>> matchingTypes = getByType(entityType);

		if (matchingTypes == null || matchingTypes.size() == 0) {
			return null;
		}

		if (matchingTypes.size() > 1) {
			throw (new RegistryException(
					MessageFormat.format("Found {0} matching entities in the registry", matchingTypes.size())));
		}

		return get(matchingTypes.iterator().next());
	}

	public void setPackages(List<String> packages) {
		this.packages = packages;
	}

	public List<String> getPackages() {
		return packages;
	}

	public void clear() {
		entities.clear();
		reversedEntities.clear();
		entitiesByTypes.clear();
		partDefinitions.clear();
		allFieldsOfType.clear();
	}

	public boolean isDirty() {
		return dirty;
	}

	public void setDirty(boolean dirty) {
		this.dirty = dirty;
	}

	public boolean contains(Class<?> beanClass) {
		return get(beanClass) != null;
	}

	public void addPart(P partEntityDefinition) {
		partDefinitions.put(partEntityDefinition.getPartClass(), partEntityDefinition);
	}

	public P getPart(Class<?> containingClass) {
		return partDefinitions.get(containingClass);
	}

	public Collection<? extends FieldDefinition> getAllFieldsOfType(Class<?> javaType) {

		Collection<FieldDefinition> allFields = allFieldsOfType.get(javaType);
		if (allFields == null) {
			Collection<?> entities = getEntitiesDefinitions();
			allFields = new ArrayList<FieldDefinition>();
			for (Object object : entities) {
				@SuppressWarnings("unchecked")
				EntityDefinition<FieldDefinition> entityDefinition = (EntityDefinition<FieldDefinition>)object;
				Collection<FieldDefinition> fields = entityDefinition.getAllFieldsDefinitions().values();
				for (FieldDefinition fieldDefinition : fields) {
					Class<?> fieldJavaType = fieldDefinition.getJavaType();
					if (javaType.isAssignableFrom(fieldJavaType)) {
						allFields.add(fieldDefinition);
					}
				}
			}
			allFieldsOfType.put(javaType, allFields);
		}
		return allFields;
	}
}
