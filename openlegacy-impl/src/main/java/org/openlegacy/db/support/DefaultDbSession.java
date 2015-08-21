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
import org.openlegacy.SessionPropertiesProvider;
import org.openlegacy.authorization.AuthorizationService;
import org.openlegacy.db.DbSession;
import org.openlegacy.db.actions.DbAction;
import org.openlegacy.db.actions.DbActions;
import org.openlegacy.db.definitions.DbEntityDefinition;
import org.openlegacy.db.exceptions.DbActionException;
import org.openlegacy.db.exceptions.DbActionNotMappedException;
import org.openlegacy.db.services.DbEntitiesRegistry;
import org.openlegacy.definitions.ActionDefinition;
import org.openlegacy.exceptions.EntityNotAccessibleException;
import org.openlegacy.exceptions.EntityNotFoundException;
import org.openlegacy.exceptions.OpenLegacyException;
import org.openlegacy.exceptions.OpenLegacyRuntimeException;
import org.openlegacy.modules.login.Login;
import org.openlegacy.modules.login.Login.LoginEntity;
import org.openlegacy.modules.login.User;
import org.openlegacy.modules.roles.Roles;
import org.openlegacy.support.AbstractSession;
import org.openlegacy.utils.ReflectionUtil;
import org.openlegacy.utils.StringUtil;

import java.text.MessageFormat;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class DefaultDbSession extends AbstractSession implements DbSession {

	private static final long serialVersionUID = 1L;

	@Inject
	private DbEntitiesRegistry dbEntitiesRegistry;

	@PersistenceContext
	transient EntityManager entityManager;

	private boolean forceAuthorization = true;

	@Inject
	private AuthorizationService authorizationService;

	@Inject
	private SessionPropertiesProvider sessionPropertiesProvider;

	@Override
	public Object getDelegate() {
		return null;
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> T getEntity(Class<T> entityClass, Object... keys) throws EntityNotFoundException {
		if (!StringUtil.isEmpty(dbEntitiesRegistry.getErrorMessage())) {
			throw new OpenLegacyRuntimeException(dbEntitiesRegistry.getErrorMessage());
		}

		authorize(entityClass);

		T entity = ReflectionUtil.newInstance(entityClass);

		DbEntityDefinition dbEntityDefinition = dbEntitiesRegistry.get(entityClass);
		ActionDefinition action = dbEntityDefinition.getAction(DbActions.READ.class);

		if (action == null) {
			throw new DbActionNotMappedException(
					"No READ action is defined. Define @DbActions(actions = { @Action(action = READ.class, ...) })");
		}
		return (T) doAction(DbActions.READ(), entity, keys);
	}

	@Override
	public Object getEntity(String entityName, Object... keys) throws EntityNotFoundException {
		DbEntityDefinition dbEntityDefinition = dbEntitiesRegistry.get(entityName);
		return getEntity(dbEntityDefinition.getEntityClass(), keys);
	}

	@Override
	public void disconnect() {}

	@Override
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

	@Override
	public Object doAction(DbAction action, Object dbEntity, Object... keys) {
		Roles rolesModule = getModule(Roles.class);
		if (rolesModule != null) {
			Login loginModule = getModule(Login.class);
			User loggedInUser = loginModule.getLoggedInUser();
			if (!rolesModule.isActionPermitted(action, dbEntity, loggedInUser)) {
				throw new DbActionException(MessageFormat.format("Logged in user {0} has no permission for action {1}",
						loggedInUser.getUserName(), action.getClass().getSimpleName()));
			}
		}
		return action.perform(getEntityManager(), dbEntity, keys);
	}

	@Override
	public void login(String user, String password) {
		//required to fill properties (such as roles etc.)
		try {
			User loggedInUser = getModule(Login.class).getLoggedInUser();
			loggedInUser.getProperties().putAll(sessionPropertiesProvider.getSessionProperties().getProperties());
		} catch (OpenLegacyException e) {
			e.printStackTrace();
		}
	}

	public void setForceAuthorization(boolean forceAuthorization) {
		this.forceAuthorization = forceAuthorization;
	}

	private <S> void authorize(Class<S> entityClass) {
		if (!forceAuthorization) {
			return;
		}
		DbEntityDefinition definitions = dbEntitiesRegistry.get(entityClass);
		User loggedInUser = getModule(Login.class).getLoggedInUser();
		if (definitions.getType() != LoginEntity.class && !authorizationService.isAuthorized(loggedInUser, entityClass)) {
			throw (new EntityNotAccessibleException(MessageFormat.format("Logged in user {0} has no permission for entity {1}",
					loggedInUser.getUserName(), entityClass.getName())));
		}
	}

}
