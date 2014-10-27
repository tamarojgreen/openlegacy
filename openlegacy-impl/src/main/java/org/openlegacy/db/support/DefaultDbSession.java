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

package org.openlegacy.db.support;

import org.openlegacy.ApplicationConnection;
import org.openlegacy.db.DbEntity;
import org.openlegacy.db.DbSession;
import org.openlegacy.db.actions.DbAction;
import org.openlegacy.db.actions.DbActions;
import org.openlegacy.db.definitions.DbEntityDefinition;
import org.openlegacy.db.exceptions.DbActionNotMappedException;
import org.openlegacy.db.services.DbEntitiesRegistry;
import org.openlegacy.definitions.ActionDefinition;
import org.openlegacy.exceptions.EntityNotFoundException;
import org.openlegacy.support.AbstractSession;
import org.openlegacy.utils.ReflectionUtil;

import javax.inject.Inject;
import javax.persistence.EntityManager;

public class DefaultDbSession extends AbstractSession implements DbSession {

	private static final long serialVersionUID = 1L;

	@Inject
	private DbEntitiesRegistry dbEntitiesRegistry;

	@Inject
	private EntityManager entityManager;

	public Object getDelegate() {
		return null;
	}

	@SuppressWarnings("unchecked")
	public <T> T getEntity(Class<T> entityClass, Object... keys) throws EntityNotFoundException {
		T entity = ReflectionUtil.newInstance(entityClass);

		DbEntityDefinition dbEntityDefinition = dbEntitiesRegistry.get(entityClass);
		ActionDefinition action = dbEntityDefinition.getAction(DbActions.READ.class);

		if (action == null) {
			throw new DbActionNotMappedException(
					"No READ action is defined. Define @DbActions(actions = { @Action(action = READ.class, ...) })");
		}
		return (T)doAction(DbActions.READ(), (DbEntity)entity);
	}

	public Object getEntity(String entityName, Object... keys) throws EntityNotFoundException {
		DbEntityDefinition dbEntityDefinition = dbEntitiesRegistry.get(entityName);
		return getEntity(dbEntityDefinition.getEntityClass(), keys);
	}

	public void disconnect() {}

	public boolean isConnected() {
		return false;
	}

	@Override
	protected ApplicationConnection<?, ?> getConnection() {
		return null;
	}

	public EntityManager getEntityManager() {
		return entityManager;
	}

	public DbEntity doAction(DbAction action, DbEntity dbEntity) {
		action.perform(this, dbEntity);
		return dbEntity;
	}

}
