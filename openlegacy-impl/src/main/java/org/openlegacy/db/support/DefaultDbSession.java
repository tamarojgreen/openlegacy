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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openlegacy.ApplicationConnection;
import org.openlegacy.SessionPropertiesProvider;
import org.openlegacy.authorization.AuthorizationService;
import org.openlegacy.cache.modules.CacheModule;
import org.openlegacy.cache.modules.CacheModule.ObtainEntityCallback;
import org.openlegacy.db.DbEntityBinder;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class DefaultDbSession extends AbstractSession implements DbSession {

	private static final long serialVersionUID = 1L;
	private final static Log logger = LogFactory.getLog(DefaultDbSession.class);

	@Inject
	private DbEntitiesRegistry dbEntitiesRegistry;

	@PersistenceContext
	transient EntityManager entityManager;

	private boolean forceAuthorization = true;

	@Inject
	private AuthorizationService authorizationService;

	@Inject
	private SessionPropertiesProvider sessionPropertiesProvider;

	@Inject
	private List<DbEntityBinder> dbEntityBinders;

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

	public Object doDbAction(DbAction action, Object dbEntity, Object... keys) {
		Roles rolesModule = getModule(Roles.class);
		if (rolesModule != null) {
			Login loginModule = getModule(Login.class);
			User loggedInUser = loginModule.getLoggedInUser();
			if (!rolesModule.isActionPermitted(action, dbEntity, loggedInUser)) {
				throw new DbActionException(MessageFormat.format("Logged in user {0} has no permission for action {1}",
						loggedInUser.getUserName(), action.getClass().getSimpleName()));
			}
		}
		Object object = action.perform(getEntityManager(), dbEntity, keys);

		applyBinders(object);

		if (rolesModule != null) {
			rolesModule.populateEntity(object, getModule(Login.class));
		}

		return object;
	}

	@Override
	public void login(String user, String password) {
		//required to fill properties (such as roles etc.)
		try {
			User loggedInUser = getModule(Login.class).getLoggedInUser();
			loggedInUser.getProperties().putAll(sessionPropertiesProvider.getSessionProperties().getProperties());
		} catch (OpenLegacyException e) {
			logger.debug(e.getMessage(), e);
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

	@Override
	public Object doAction(final DbAction action, final Object dbEntity, final Object... keys) {
		CacheModule cacheModule = getModule(CacheModule.class);

		if (cacheModule != null) {
			return cacheModule.doStuff(action.getClass(), dbEntity.getClass(), new ObtainEntityCallback() {

				@Override
				public Object obtainEntity() {
					return doDbAction(action, dbEntity, keys);
				}

				@Override
				public List<Object> getEntityKeys() {
					return new ArrayList<Object>(Arrays.asList(keys));
				}
			});
		} else {
			return doDbAction(action, dbEntity, keys);
		}
	}

	@Override
	public void applyBinders(Object dbEntity) {
		for (DbEntityBinder dbEntityBinder : dbEntityBinders) {
			dbEntityBinder.populateEntity(dbEntity);
		}
	}

}
