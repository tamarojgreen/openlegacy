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
package org.openlegacy.rpc.utils;

import org.apache.commons.lang.StringUtils;
import org.openlegacy.definitions.ActionDefinition;
import org.openlegacy.definitions.RpcActionDefinition;
import org.openlegacy.rpc.RpcActions;
import org.openlegacy.rpc.RpcEntity;
import org.openlegacy.rpc.RpcSession;
import org.openlegacy.rpc.actions.RpcAction;
import org.openlegacy.rpc.definitions.RpcEntityDefinition;
import org.openlegacy.rpc.services.RpcEntitiesRegistry;
import org.openlegacy.utils.SpringUtil;
import org.springframework.context.ApplicationContext;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.List;

import javax.inject.Inject;

public class RpcEntityUtils implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private transient ApplicationContext applicationContext;

	/**
	 * Sends the provided screen entity and action alias over the given terminal session
	 * 
	 * @param rpcSession
	 *            A terminal session to perform over the send action
	 * @param rpcEntity
	 *            The screen entity to send
	 * @param actionAlias
	 *            An action alias which belongs to the screen entity
	 * 
	 * @return The invoked action definition
	 */
	public RpcEntity sendEntity(RpcSession rpcSession, RpcEntity rpcEntity, String actionAlias) {
		RpcEntitiesRegistry rpcEntitiesRegistry = SpringUtil.getBean(applicationContext, RpcEntitiesRegistry.class);
		RpcEntityDefinition entityDefinitions = rpcEntitiesRegistry.get(rpcEntity.getClass());
		RpcAction sessionAction = null;
		if (StringUtils.isEmpty(actionAlias)) {
			sessionAction = RpcActions.READ();
		} else {
			List<ActionDefinition> actions = entityDefinitions.getActions();
			for (ActionDefinition actionDefinition : actions) {
				if (actionDefinition.getAlias().equals(actionAlias)) {
					sessionAction = (RpcAction)actionDefinition.getAction();
				}
			}
		}

		Assert.notNull(sessionAction, MessageFormat.format("Alias for rpc action {0} not found", actionAlias));
		return rpcSession.doAction(sessionAction, rpcEntity);
	}

	public RpcActionDefinition findAction(RpcEntity rpcEntity, String actionAlias) {
		RpcEntitiesRegistry screenEntitiesRegistry = SpringUtil.getBean(applicationContext, RpcEntitiesRegistry.class);
		RpcEntityDefinition entityDefinitions = screenEntitiesRegistry.get(rpcEntity.getClass());
		RpcAction sessionAction = null;
		RpcActionDefinition matchedActionDefinition = null;
		if (StringUtils.isEmpty(actionAlias)) {
			sessionAction = RpcActions.READ();
		} else {
			List<ActionDefinition> actions = entityDefinitions.getActions();
			for (ActionDefinition actionDefinition : actions) {
				if (actionDefinition.getAlias().equals(actionAlias)) {
					matchedActionDefinition = (RpcActionDefinition)actionDefinition;
					sessionAction = (RpcAction)actionDefinition.getAction();
				}
			}
		}

		Assert.notNull(sessionAction, MessageFormat.format("Alias for session action {0} not found", actionAlias));
		return matchedActionDefinition;
	}
}
