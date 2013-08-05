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
package org.openlegacy.rpc.support;

import org.apache.commons.lang.SerializationUtils;
import org.apache.commons.lang.StringUtils;
import org.openlegacy.ApplicationConnection;
import org.openlegacy.ApplicationConnectionListener;
import org.openlegacy.definitions.ActionDefinition;
import org.openlegacy.definitions.FieldDefinition;
import org.openlegacy.definitions.RpcActionDefinition;
import org.openlegacy.exceptions.EntityNotFoundException;
import org.openlegacy.modules.SessionModule;
import org.openlegacy.rpc.RpcActionNotMappedException;
import org.openlegacy.rpc.RpcActions;
import org.openlegacy.rpc.RpcConnection;
import org.openlegacy.rpc.RpcEntity;
import org.openlegacy.rpc.RpcEntityBinder;
import org.openlegacy.rpc.RpcField;
import org.openlegacy.rpc.RpcInvokeAction;
import org.openlegacy.rpc.RpcResult;
import org.openlegacy.rpc.RpcSession;
import org.openlegacy.rpc.actions.RpcAction;
import org.openlegacy.rpc.definitions.RpcEntityDefinition;
import org.openlegacy.rpc.services.RpcEntitiesRegistry;
import org.openlegacy.rpc.utils.SimpleRpcPojoFieldAccessor;
import org.openlegacy.support.AbstractSession;
import org.openlegacy.utils.ReflectionUtil;
import org.springframework.util.Assert;

import java.text.MessageFormat;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.inject.Inject;

public class DefaultRpcSession extends AbstractSession implements RpcSession {

	private static final long serialVersionUID = 1L;

	private RpcConnection rpcConnection;

	@Inject
	private RpcEntitiesRegistry rpcEntitiesRegistry;

	@Inject
	private List<RpcEntityBinder> rpcEntityBinders;

	public Object getDelegate() {
		return rpcConnection;
	}

	@SuppressWarnings("unchecked")
	public <T> T getEntity(Class<T> entityClass, Object... keys) throws EntityNotFoundException {
		T entity = ReflectionUtil.newInstance(entityClass);
		SimpleRpcPojoFieldAccessor fieldAccesor = new SimpleRpcPojoFieldAccessor(entity);

		RpcEntityDefinition rpcDefinition = rpcEntitiesRegistry.get(entityClass);
		ActionDefinition action = rpcDefinition.getAction(RpcActions.READ.class);

		if (action == null) {
			throw (new RpcActionNotMappedException(
					"No READ action is defined. Define @RpcActions(actions = { @Action(action = READ.class, path = ...) })"));
		}

		List<? extends FieldDefinition> keysDefinitions = rpcDefinition.getKeys();
		Assert.isTrue(
				keysDefinitions.size() == keys.length,
				MessageFormat.format("Provided keys {0} doesnt match entity {1} keys", StringUtils.join(keys, "-"),
						rpcDefinition.getEntityName()));
		int index = 0;
		for (FieldDefinition fieldDefinition : keysDefinitions) {
			fieldAccesor.setFieldValue(fieldDefinition.getName(), keys[index]);
			index++;
		}

		return (T)doAction(RpcActions.READ(), (RpcEntity)entity);
	}

	public Object getEntity(String entityName, Object... keys) throws EntityNotFoundException {
		RpcEntityDefinition rpcDefinition = rpcEntitiesRegistry.get(entityName);
		return getEntity(rpcDefinition.getEntityClass(), keys);
	}

	public void disconnect() {
		rpcConnection.disconnect();
	}

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

	@SuppressWarnings("unchecked")
	public RpcEntity doAction(RpcAction action, RpcEntity rpcEntity) {
		RpcEntityDefinition rpcDefinition = rpcEntitiesRegistry.get(rpcEntity.getClass());
		RpcActionDefinition actionDefinition = (RpcActionDefinition)rpcDefinition.getAction(action.getClass());

		SimpleRpcInvokeAction rpcAction = new SimpleRpcInvokeAction();
		rpcAction.setRpcPath(actionDefinition.getProgramPath());
		populateRpcFields(rpcEntity, rpcDefinition, rpcAction);
		RpcResult rpcResult = invoke(rpcAction);
		if (actionDefinition.getTargetEntity() != null) {
			return (RpcEntity)getEntity(actionDefinition.getTargetEntity());
		} else {
			populateEntity(rpcEntity, rpcDefinition, rpcResult);
		}
		return rpcEntity;

	}

	private RpcResult invoke(SimpleRpcInvokeAction rpcAction) {
		// clone to avoid modifications by connection of fields collection
		SimpleRpcInvokeAction clonedRpcAction = (SimpleRpcInvokeAction)SerializationUtils.clone(rpcAction);
		RpcResult rpcResult = rpcConnection.invoke(rpcAction);
		notifyModulesAfterSend(clonedRpcAction, rpcResult);
		return rpcResult;
	}

	protected void notifyModulesAfterSend(SimpleRpcInvokeAction rpcAction, RpcResult rpcResult) {
		Collection<? extends SessionModule> modulesList = getSessionModules().getModules();
		for (SessionModule sessionModule : modulesList) {
			if (sessionModule instanceof ApplicationConnectionListener) {
				((ApplicationConnectionListener)sessionModule).afterAction(getConnection(), rpcAction, rpcResult);
			}
		}
	}

	private void populateRpcFields(RpcEntity rpcEntity, RpcEntityDefinition rpcEntityDefinition, RpcInvokeAction rpcAction) {
		for (RpcEntityBinder rpcEntityBinder : rpcEntityBinders) {
			rpcEntityBinder.populateAction(rpcAction, rpcEntity);
		}
		Collections.sort(rpcAction.getFields(), new Comparator<RpcField>() {

			public int compare(RpcField field1, RpcField field2) {
				return field1.getOrder() - field2.getOrder();
			}
		});
	}

	private void populateEntity(RpcEntity rpcEntity, RpcEntityDefinition rpcDefinition, RpcResult rpcResult) {
		for (RpcEntityBinder rpcEntityBinder : rpcEntityBinders) {
			rpcEntityBinder.populateEntity(rpcEntity, rpcResult);
		}
	}
}
