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
package org.openlegacy.rpc.support;

import org.apache.commons.lang.ClassUtils;
import org.apache.commons.lang.SerializationUtils;
import org.apache.commons.lang.StringUtils;
import org.openlegacy.ApplicationConnection;
import org.openlegacy.ApplicationConnectionListener;
import org.openlegacy.annotations.rpc.Direction;
import org.openlegacy.authorization.AuthorizationService;
import org.openlegacy.definitions.FieldDefinition;
import org.openlegacy.definitions.RpcActionDefinition;
import org.openlegacy.exceptions.EntityNotAccessibleException;
import org.openlegacy.exceptions.EntityNotFoundException;
import org.openlegacy.exceptions.OpenLegacyRuntimeException;
import org.openlegacy.modules.SessionModule;
import org.openlegacy.modules.login.Login;
import org.openlegacy.modules.login.Login.LoginEntity;
import org.openlegacy.modules.login.User;
import org.openlegacy.modules.roles.Roles;
import org.openlegacy.rpc.RpcActionNotMappedException;
import org.openlegacy.rpc.RpcConnection;
import org.openlegacy.rpc.RpcEntity;
import org.openlegacy.rpc.RpcEntityBinder;
import org.openlegacy.rpc.RpcField;
import org.openlegacy.rpc.RpcFieldConverter;
import org.openlegacy.rpc.RpcInvokeAction;
import org.openlegacy.rpc.RpcPojoFieldAccessor;
import org.openlegacy.rpc.RpcResult;
import org.openlegacy.rpc.RpcSession;
import org.openlegacy.rpc.actions.RpcAction;
import org.openlegacy.rpc.actions.RpcActions;
import org.openlegacy.rpc.actions.RpcActions.RpcActionAdapter;
import org.openlegacy.rpc.definitions.RpcEntityDefinition;
import org.openlegacy.rpc.exceptions.RpcActionException;
import org.openlegacy.rpc.services.RpcEntitiesRegistry;
import org.openlegacy.rpc.support.binders.RpcFieldsExpressionBinder;
import org.openlegacy.rpc.utils.HierarchyRpcPojoFieldAccessor;
import org.openlegacy.rpc.utils.SimpleHierarchyRpcPojoFieldAccessor;
import org.openlegacy.support.AbstractSession;
import org.openlegacy.types.BinaryArray;
import org.openlegacy.utils.ReflectionUtil;
import org.openlegacy.utils.StringUtil;
import org.springframework.util.Assert;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

public class DefaultRpcSession extends AbstractSession implements RpcSession {

	private static final long serialVersionUID = 1L;

	private RpcConnection rpcConnection;

	@Inject
	protected RpcEntitiesRegistry rpcEntitiesRegistry;

	@Inject
	private List<RpcEntityBinder> rpcEntityBinders;

	@Inject
	private List<RpcFieldConverter> rpcFieldConverters;

	@Inject
	private RpcFieldsExpressionBinder rpcExpressionBinder;

	private boolean forceAuthorization = true;

	@Inject
	private AuthorizationService authorizationService;

	@Override
	public Object getDelegate() {
		return rpcConnection;
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> T getEntity(Class<T> entityClass, Object... keys) throws EntityNotFoundException {
		if (!StringUtil.isEmpty(rpcEntitiesRegistry.getErrorMessage())) {
			throw new OpenLegacyRuntimeException(rpcEntitiesRegistry.getErrorMessage());
		}

		authorize(entityClass);

		T entity = ReflectionUtil.newInstance(entityClass);

		RpcEntityDefinition rpcDefinition = rpcEntitiesRegistry.get(entityClass);

		RpcActionAdapter actionAdapter = null;

		if (rpcDefinition.getAction(RpcActions.READ.class) != null) {
			actionAdapter = RpcActions.READ();
		} else if (rpcDefinition.getAction(RpcActions.EXECUTE.class) != null) {
			actionAdapter = RpcActions.EXECUTE();
		} else {
			throw (new RpcActionNotMappedException(
					"No READ or EXEUCTE action is defined. Define @RpcActions(actions = { @Action(action = <action>.class, path = ...) })"));
		}

		List<? extends FieldDefinition> keysDefinitions = rpcDefinition.getKeys();
		Assert.isTrue(keysDefinitions.size() == keys.length, MessageFormat.format(
				"Provided keys {0} doesnt match entity {1} keys", StringUtils.join(keys, "-"), rpcDefinition.getEntityName()));
		HierarchyRpcPojoFieldAccessor fieldAccesor = new SimpleHierarchyRpcPojoFieldAccessor(entity);
		int index = 0;
		for (FieldDefinition fieldDefinition : keysDefinitions) {

			RpcPojoFieldAccessor directFieldAccessor = fieldAccesor.getPartAccessor(fieldDefinition.getName());
			if (ClassUtils.getAllSuperclasses(fieldDefinition.getJavaType()).contains(BinaryArray.class)) {
				BinaryArray array = null;
				try {
					array = (BinaryArray) fieldDefinition.getJavaType().newInstance();
				} catch (Exception e) {
					throw (new OpenLegacyRuntimeException(e));
				}
				array.setValue((String) keys[index]);
				directFieldAccessor.setFieldValue(StringUtil.removeNamespace(fieldDefinition.getName()), array);
			} else {
				directFieldAccessor.setFieldValue(StringUtil.removeNamespace(fieldDefinition.getName()), keys[index]);
			}
			index++;
		}

		return (T) doAction(actionAdapter, (RpcEntity) entity);
	}

	@Override
	public Object getEntity(String entityName, Object... keys) throws EntityNotFoundException {
		RpcEntityDefinition rpcDefinition = rpcEntitiesRegistry.get(entityName);
		return getEntity(rpcDefinition.getEntityClass(), keys);
	}

	@Override
	public void disconnect() {
		rpcConnection.disconnect();
	}

	@Override
	public boolean isConnected() {
		return rpcConnection.isConnected();
	}

	@Override
	protected ApplicationConnection<?, ?> getConnection() {
		return rpcConnection;
	}

	public void setConnection(RpcConnection rpcConnection) {
		this.rpcConnection = rpcConnection;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		super.afterPropertiesSet();
		Assert.notNull(rpcConnection, "RPC connection bean has not been found");
	}

	@Override
	@SuppressWarnings("unchecked")
	public RpcEntity doAction(RpcAction action, RpcEntity rpcEntity) {

		Roles rolesModule = getModule(Roles.class);
		if (rolesModule != null) {
			Login loginModule = getModule(Login.class);
			User loggedInUser = loginModule.getLoggedInUser();
			if (!rolesModule.isActionPermitted(action, rpcEntity, loggedInUser)) {
				throw new RpcActionException(MessageFormat.format("Logged in user {0} has no permission for action {1}",
						loggedInUser.getUserName(), action.getClass().getSimpleName()));
			}
		}

		RpcEntityDefinition rpcDefinition = rpcEntitiesRegistry.get(rpcEntity.getClass());
		RpcActionDefinition actionDefinition = (RpcActionDefinition) rpcDefinition.getAction(action.getClass());

		SimpleRpcInvokeAction rpcAction = new SimpleRpcInvokeAction();
		rpcAction.setRpcPath(actionDefinition.getProgramPath());
		rpcAction.setProperties(actionDefinition.getProperties());
		populateRpcFields(rpcEntity, rpcDefinition, rpcAction);
		converToLegacyFields(rpcAction);

		if (actionDefinition.getTargetEntity() != null) {
			return (RpcEntity) getEntity(actionDefinition.getTargetEntity());
		} else {
			RpcResult rpcResult = invoke(rpcAction, rpcEntity.getClass().getSimpleName());
			RpcResult rpcResultCopy = rpcResult.clone();
			converToApiFields(rpcResultCopy.getRpcFields());
			populateEntity(rpcEntity, rpcDefinition, rpcResultCopy);
		}
		return rpcEntity;

	}

	final protected RpcResult invoke(SimpleRpcInvokeAction rpcAction, String entityName) {
		// clone to avoid modifications by connection of fields collection
		SimpleRpcInvokeAction clonedRpcAction = (SimpleRpcInvokeAction) SerializationUtils.clone(rpcAction);

		// Determine if any fields need to be removed.
		// Some calculated fields do not have a corresponding field on the actual RPC Call
		final List<RpcField> fieldsToRemove = new ArrayList<RpcField>();
		for (RpcField rpcField : rpcAction.getFields()) {
			if (rpcField.getDirection() == Direction.NONE) {
				fieldsToRemove.add(rpcField);
			}
		}
		rpcAction.getFields().removeAll(fieldsToRemove);

		RpcResult rpcResult = rpcConnection.invoke(rpcAction);
		notifyModulesAfterSend(clonedRpcAction, rpcResult, entityName);
		return rpcResult;
	}

	private void notifyModulesAfterSend(SimpleRpcInvokeAction rpcAction, RpcResult rpcResult, String entityName) {
		Collection<? extends SessionModule> modulesList = getSessionModules().getModules();
		for (SessionModule sessionModule : modulesList) {
			if (sessionModule instanceof ApplicationConnectionListener) {
				((ApplicationConnectionListener) sessionModule).afterAction(getConnection(), rpcAction, rpcResult, entityName);
			}
		}
	}

	final protected void converToLegacyFields(RpcInvokeAction rpcAction) {

		for (RpcFieldConverter rpcFieldConverter : rpcFieldConverters) {
			rpcFieldConverter.toLegacy(rpcAction.getFields());
		}

		Collections.sort(rpcAction.getFields(), new RpcOrderFieldComparator());

	}

	final protected void converToApiFields(List<RpcField> fields) {

		for (RpcFieldConverter rpcFieldConverter : rpcFieldConverters) {
			rpcFieldConverter.toApi(fields);
		}

		Collections.sort(fields, new RpcOrderFieldComparator());

	}

	final protected void populateRpcFields(RpcEntity rpcEntity, RpcEntityDefinition rpcEntityDefinition, RpcInvokeAction rpcAction) {
		for (RpcEntityBinder rpcEntityBinder : rpcEntityBinders) {
			rpcEntityBinder.populateAction(rpcAction, rpcEntity);
		}
	}

	final protected void populateEntity(RpcEntity rpcEntity, RpcEntityDefinition rpcDefinition, RpcResult rpcResult) {

		for (RpcEntityBinder rpcEntityBinder : rpcEntityBinders) {
			rpcEntityBinder.populateEntity(rpcEntity, rpcResult);
		}
		rpcExpressionBinder.populateEntity(rpcEntity, rpcResult);

		Roles rolesModule = getModule(Roles.class);
		if (rolesModule != null) {
			rolesModule.populateEntity(rpcEntity, getModule(Login.class));
		}

	}

	@Override
	public void login(String user, String password) {
		notifyModulesBeforeConnect();
		rpcConnection.login(user, password);
		notifyModulesAfterConnect();
	}

	public void setForceAuthorization(boolean forceAuthorization) {
		this.forceAuthorization = forceAuthorization;
	}

	private <S> void authorize(Class<S> entityClass) {
		if (!forceAuthorization) {
			return;
		}
		RpcEntityDefinition definitions = rpcEntitiesRegistry.get(entityClass);
		User loggedInUser = getModule(Login.class).getLoggedInUser();
		if (definitions.getType() != LoginEntity.class && !authorizationService.isAuthorized(loggedInUser, entityClass)) {
			throw (new EntityNotAccessibleException(MessageFormat.format("Logged in user {0} has no permission for entity {1}",
					loggedInUser.getUserName(), entityClass.getName())));
		}
	}

}
