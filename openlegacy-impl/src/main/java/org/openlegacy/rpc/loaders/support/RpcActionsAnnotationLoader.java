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
package org.openlegacy.rpc.loaders.support;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openlegacy.EntitiesRegistry;
import org.openlegacy.annotations.rpc.RpcActions;
import org.openlegacy.definitions.ActionDefinition;
import org.openlegacy.loaders.support.AbstractClassAnnotationLoader;
import org.openlegacy.rpc.RpcEntity;
import org.openlegacy.rpc.actions.RpcAction;
import org.openlegacy.rpc.definitions.RpcEntityDefinition;
import org.openlegacy.rpc.definitions.RpcPartEntityDefinition;
import org.openlegacy.rpc.definitions.SimpleRpcActionDefinition;
import org.openlegacy.utils.ReflectionUtil;
import org.openlegacy.utils.StringUtil;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.lang.annotation.Annotation;
import java.text.MessageFormat;
import java.util.List;

@Component
public class RpcActionsAnnotationLoader extends AbstractClassAnnotationLoader {

	private final static Log logger = LogFactory.getLog(RpcActionsAnnotationLoader.class);

	@Override
	public boolean match(Annotation annotation) {
		return annotation.annotationType() == RpcActions.class;
	}

	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void load(EntitiesRegistry entitiesRegistry, Annotation annotation, Class<?> containingClass) {

		RpcEntityDefinition rpcEntityDefinition = (RpcEntityDefinition)entitiesRegistry.get(containingClass);
		List<ActionDefinition> actionsRef;
		RpcPartEntityDefinition partEntityDefinition;
		Class<?> holdingClass;
		if (rpcEntityDefinition == null) {
			partEntityDefinition = (RpcPartEntityDefinition)entitiesRegistry.getPart(containingClass);
			Assert.notNull(
					partEntityDefinition,
					MessageFormat.format(
							"RPC entity definition for class {0} not found. Verify @RpcActions is defined along @RpcEntity or @RpcPart annotation",
							containingClass.getName()));
			actionsRef = partEntityDefinition.getActions();
			holdingClass = partEntityDefinition.getPartClass();
		} else {
			actionsRef = rpcEntityDefinition.getActions();
			holdingClass = rpcEntityDefinition.getEntityClass();
		}

		RpcActions rpcActions = (RpcActions)annotation;

		org.openlegacy.annotations.rpc.Action[] actions = rpcActions.actions();
		if (actions.length > 0) {
			for (org.openlegacy.annotations.rpc.Action action : actions) {
				Class<? extends RpcAction> theAction = action.action();

				SimpleRpcActionDefinition actionDefinition = null;
				String displayName = action.displayName().length() > 0 ? action.displayName()
						: StringUtil.toDisplayName(action.action().getSimpleName());
				actionDefinition = new SimpleRpcActionDefinition(ReflectionUtil.newInstance(theAction), displayName);

				if (StringUtils.isEmpty(action.alias())) {
					actionDefinition.setAlias(StringUtils.uncapitalize(displayName));
				} else {
					actionDefinition.setAlias(action.alias());
				}
				actionDefinition.setProgramPath(action.path());
				actionDefinition.setGlobal(action.global());

				if (action.targetEntity() != RpcEntity.NONE.class) {
					actionDefinition.setTargetEntity(action.targetEntity());
				}
				actionsRef.add(actionDefinition);
				if (logger.isDebugEnabled()) {
					logger.debug(MessageFormat.format("Action {0} - \"{1}\" was added to the registry for rpc entity {2}",
							theAction.getSimpleName(), displayName, containingClass));
				}
			}
			logger.info(MessageFormat.format("RPC actions for \"{0}\" was added to the rpc registry", holdingClass));
		}
	}
}
