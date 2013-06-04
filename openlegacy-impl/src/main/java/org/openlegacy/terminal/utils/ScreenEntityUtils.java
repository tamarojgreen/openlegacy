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
package org.openlegacy.terminal.utils;

import org.apache.commons.lang.StringUtils;
import org.openlegacy.definitions.ActionDefinition;
import org.openlegacy.terminal.ScreenEntity;
import org.openlegacy.terminal.TerminalSession;
import org.openlegacy.terminal.actions.TerminalAction;
import org.openlegacy.terminal.actions.TerminalActions;
import org.openlegacy.terminal.definitions.ScreenEntityDefinition;
import org.openlegacy.terminal.definitions.TerminalActionDefinition;
import org.openlegacy.terminal.services.ScreenEntitiesRegistry;
import org.openlegacy.utils.SpringUtil;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

public class ScreenEntityUtils implements InitializingBean, Serializable {

	private static final long serialVersionUID = 1L;

	private static final String NEXT = "next";
	private static final String PREVIOUS = "previous";

	@Inject
	private transient ApplicationContext applicationContext;

	/**
	 * A map of action alias to actual terminal action
	 */
	private Map<String, TerminalAction> defaultActionAliasToAction;

	/**
	 * Sends the provided screen entity and action alias over the given terminal session
	 * 
	 * @param terminalSession
	 *            A terminal session to perform over the send action
	 * @param screenEntity
	 *            The screen entity to send
	 * @param actionAlias
	 *            An action alias which belongs to the screen entity
	 * 
	 * @return The invoked action definition
	 */
	public TerminalActionDefinition sendScreenEntity(TerminalSession terminalSession, ScreenEntity screenEntity,
			String actionAlias) {
		ScreenEntitiesRegistry screenEntitiesRegistry = SpringUtil.getBean(applicationContext, ScreenEntitiesRegistry.class);
		ScreenEntityDefinition entityDefinitions = screenEntitiesRegistry.get(screenEntity.getClass());
		TerminalAction sessionAction = null;
		TerminalActionDefinition invokedActionDefinition = null;
		if (StringUtils.isEmpty(actionAlias)) {
			sessionAction = TerminalActions.ENTER();
		} else {
			List<ActionDefinition> actions = entityDefinitions.getActions();
			for (ActionDefinition actionDefinition : actions) {
				if (actionDefinition.getAlias().equals(actionAlias)) {
					invokedActionDefinition = (TerminalActionDefinition)actionDefinition;
					sessionAction = (TerminalAction)actionDefinition.getAction();
				}
			}
			if (sessionAction == null) {
				sessionAction = defaultActionAliasToAction.get(actionAlias);
			}
		}

		if (invokedActionDefinition != null && invokedActionDefinition.getFocusField() != null) {
			screenEntity.setFocusField(invokedActionDefinition.getFocusField());
		}
		Assert.notNull(sessionAction, MessageFormat.format("Alias for session action {0} not found", actionAlias));
		terminalSession.doAction(sessionAction, screenEntity);
		return invokedActionDefinition;
	}

	public void setDefaultActionAliasToAction(Map<String, TerminalAction> actionAliasToAction) {
		this.defaultActionAliasToAction = actionAliasToAction;
	}

	public void afterPropertiesSet() throws Exception {
		if (defaultActionAliasToAction == null) {
			// initialize with default mapping if not set from bean
			defaultActionAliasToAction = new HashMap<String, TerminalAction>();
			defaultActionAliasToAction.put(NEXT, TerminalActions.PAGEDOWN());
			defaultActionAliasToAction.put(PREVIOUS, TerminalActions.PAGEUP());
		}
	}
}
