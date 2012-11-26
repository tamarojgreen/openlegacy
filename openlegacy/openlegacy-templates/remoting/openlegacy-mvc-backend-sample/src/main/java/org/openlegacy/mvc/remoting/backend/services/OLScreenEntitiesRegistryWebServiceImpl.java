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

package org.openlegacy.mvc.remoting.backend.services;

import org.openlegacy.EntityType;
import org.openlegacy.exceptions.RegistryException;
import org.openlegacy.mvc.remoting.services.OLScreenEntitiesRegistry;
import org.openlegacy.terminal.definitions.ScreenEntityDefinition;
import org.openlegacy.terminal.definitions.ScreenPartEntityDefinition;
import org.openlegacy.terminal.definitions.ScreenTableDefinition;
import org.openlegacy.terminal.services.ScreenEntitiesRegistry;

import java.util.Collection;
import java.util.List;

/**
 * @author Imivan
 * 
 */
public class OLScreenEntitiesRegistryWebServiceImpl implements OLScreenEntitiesRegistry {

	private ScreenEntitiesRegistry registry;

	public OLScreenEntitiesRegistryWebServiceImpl(ScreenEntitiesRegistry screensRegistry) {
		this.registry = screensRegistry;
	}

	/* (non-Javadoc)
	 * @see org.openlegacy.terminal.services.ScreenEntitiesRegistry#addPart(org.openlegacy.terminal.definitions.ScreenPartEntityDefinition)
	 */
	@Override
	public void addPart(ScreenPartEntityDefinition screenPartEntityDefinition) {
		registry.addPart(screenPartEntityDefinition);
	}

	/* (non-Javadoc)
	 * @see org.openlegacy.terminal.services.ScreenEntitiesRegistry#getPart(java.lang.Class)
	 */
	@Override
	public ScreenPartEntityDefinition getPart(Class<?> containingClass) {
		return registry.getPart(containingClass);
	}

	/* (non-Javadoc)
	 * @see org.openlegacy.terminal.services.ScreenEntitiesRegistry#addTable(org.openlegacy.terminal.definitions.ScreenTableDefinition)
	 */
	@Override
	public void addTable(ScreenTableDefinition tableDefinition) {
		registry.addTable(tableDefinition);
	}

	/* (non-Javadoc)
	 * @see org.openlegacy.terminal.services.ScreenEntitiesRegistry#getTable(java.lang.Class)
	 */
	@Override
	public ScreenTableDefinition getTable(Class<?> containingClass) {
		return registry.getTable(containingClass);
	}

	/* (non-Javadoc)
	 * @see org.openlegacy.EntitiesRegistry#getEntitiesDefinitions()
	 */
	@Override
	public Collection<ScreenEntityDefinition> getEntitiesDefinitions() {
		return registry.getEntitiesDefinitions();
	}

	/* (non-Javadoc)
	 * @see org.openlegacy.EntitiesRegistry#getEntityClass(java.lang.String)
	 */
	@Override
	public Class<?> getEntityClass(String entityName) {
		return registry.getEntityClass(entityName);
	}

	/* (non-Javadoc)
	 * @see org.openlegacy.EntitiesRegistry#getEntityName(java.lang.Class)
	 */
	@Override
	public String getEntityName(Class<?> entity) {
		return registry.getEntityName(entity);
	}

	/* (non-Javadoc)
	 * @see org.openlegacy.EntitiesRegistry#getByType(java.lang.Class)
	 */
	@Override
	public List<Class<?>> getByType(Class<? extends EntityType> entityType) {
		return registry.getByType(entityType);
	}

	/* (non-Javadoc)
	 * @see org.openlegacy.EntitiesRegistry#getSingleEntityDefinition(java.lang.Class)
	 */
	@Override
	public ScreenEntityDefinition getSingleEntityDefinition(Class<? extends EntityType> entityType) throws RegistryException {
		return registry.getSingleEntityDefinition(entityType);
	}

	/* (non-Javadoc)
	 * @see org.openlegacy.EntitiesRegistry#add(org.openlegacy.EntityDefinition)
	 */
	@Override
	public void add(ScreenEntityDefinition entityDefinition) {
		registry.add(entityDefinition);
	}

	/* (non-Javadoc)
	 * @see org.openlegacy.EntitiesRegistry#get(java.lang.Class)
	 */
	@Override
	public ScreenEntityDefinition get(Class<?> entityClass) {
		return registry.get(entityClass);
	}

	/* (non-Javadoc)
	 * @see org.openlegacy.EntitiesRegistry#get(java.lang.String)
	 */
	@Override
	public ScreenEntityDefinition get(String entityName) {
		return registry.get(entityName);
	}

	/* (non-Javadoc)
	 * @see org.openlegacy.EntitiesRegistry#clear()
	 */
	@Override
	public void clear() {
		registry.clear();
	}

	/* (non-Javadoc)
	 * @see org.openlegacy.EntitiesRegistry#getPackages()
	 */
	@Override
	public List<String> getPackages() {
		return registry.getPackages();
	}

	/* (non-Javadoc)
	 * @see org.openlegacy.EntitiesRegistry#isDirty()
	 */
	@Override
	public boolean isDirty() {
		return registry.isDirty();
	}

}
