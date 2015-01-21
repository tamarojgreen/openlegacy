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
package org.openlegacy.terminal.utils;

import org.apache.commons.lang.StringUtils;
import org.openlegacy.definitions.ActionDefinition;
import org.openlegacy.terminal.ScreenEntity;
import org.openlegacy.terminal.actions.TerminalAction;
import org.openlegacy.terminal.actions.TerminalAction.AdditionalKey;
import org.openlegacy.terminal.actions.TerminalActions;
import org.openlegacy.terminal.definitions.ScreenEntityDefinition;
import org.openlegacy.terminal.definitions.ScreenTableDefinition;
import org.openlegacy.terminal.definitions.SimpleTerminalActionDefinition;
import org.openlegacy.terminal.definitions.TerminalActionDefinition;
import org.openlegacy.terminal.services.ScreenEntitiesRegistry;
import org.openlegacy.utils.SpringUtil;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

public class ScreenEntityUtils implements InitializingBean, Serializable {

	private static final long serialVersionUID = 1L;

	private static final String NEXT = "next";
	private static final String PREVIOUS = "previous";

	private static final String LOOKUP = "lookup";

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
	public TerminalActionDefinition findAction(ScreenEntity screenEntity, TerminalAction action) {
		ScreenEntitiesRegistry screenEntitiesRegistry = SpringUtil.getBean(applicationContext, ScreenEntitiesRegistry.class);
		ScreenEntityDefinition entityDefinitions = screenEntitiesRegistry.get(screenEntity.getClass());
		List<ActionDefinition> actions = entityDefinitions.getActions();
		for (ActionDefinition actionDefinition : actions) {
			TerminalAction action2 = (TerminalAction)actionDefinition.getAction();
			if (action2.equals(action)) {
				return (TerminalActionDefinition)actionDefinition;
			}
		}

		return null;
	}

	public TerminalActionDefinition findAction(ScreenEntity screenEntity, String actionAlias) {
		ScreenEntitiesRegistry screenEntitiesRegistry = SpringUtil.getBean(applicationContext, ScreenEntitiesRegistry.class);
		ScreenEntityDefinition entityDefinitions = screenEntitiesRegistry.get(screenEntity.getClass());
		TerminalAction sessionAction = null;
		TerminalActionDefinition matchedActionDefinition = null;
		String focusField = null;

		// action may be combined of <action>-<focus field>
		if (actionAlias != null && actionAlias.contains("-")) {
			String[] actionParts = actionAlias.split("-");
			actionAlias = actionParts[0];
			focusField = actionParts[1];
		}
		if (actionAlias != null && actionAlias.equals(TerminalAction.NONE)) {
			sessionAction = TerminalActions.NONE();
			matchedActionDefinition = new SimpleTerminalActionDefinition(sessionAction, AdditionalKey.NONE, "", null);
		} else if (StringUtils.isEmpty(actionAlias)) {
			sessionAction = TerminalActions.ENTER();
			matchedActionDefinition = new SimpleTerminalActionDefinition(sessionAction, AdditionalKey.NONE, "", null);
		} else {
			List<ActionDefinition> actions = entityDefinitions.getActions();
			for (ActionDefinition actionDefinition : actions) {
				if (actionDefinition.getAlias().equals(actionAlias)) {
					matchedActionDefinition = (TerminalActionDefinition)actionDefinition;
					sessionAction = (TerminalAction)actionDefinition.getAction();
				}
			}
			Collection<ScreenTableDefinition> tables = entityDefinitions.getTableDefinitions().values();
			for (ScreenTableDefinition screenTableDefinition : tables) {
				List<ActionDefinition> tableActions = screenTableDefinition.getActions();
				for (ActionDefinition actionDefinition : tableActions) {
					if (actionDefinition.getAlias().equals(actionAlias)) {
						matchedActionDefinition = (TerminalActionDefinition)actionDefinition;
						sessionAction = (TerminalAction)actionDefinition.getAction();

					}
				}
			}
			if (sessionAction == null) {
				sessionAction = defaultActionAliasToAction.get(actionAlias);
				matchedActionDefinition = new SimpleTerminalActionDefinition(sessionAction, AdditionalKey.NONE, "", null);
			}
		}

		if (matchedActionDefinition.getFocusField() != null && focusField == null) {
			focusField = matchedActionDefinition.getFocusField();
		}
		if (matchedActionDefinition != null && focusField != null) {
			screenEntity.setFocusField(focusField);
		}
		Assert.notNull(sessionAction, MessageFormat.format("Alias for session action {0} not found", actionAlias));
		return matchedActionDefinition;
	}

	public void setDefaultActionAliasToAction(Map<String, TerminalAction> actionAliasToAction) {
		this.defaultActionAliasToAction = actionAliasToAction;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		if (defaultActionAliasToAction == null) {
			// initialize with default mapping if not set from bean
			defaultActionAliasToAction = new HashMap<String, TerminalAction>();
			defaultActionAliasToAction.put(NEXT, TerminalActions.PAGE_DOWN());
			defaultActionAliasToAction.put(PREVIOUS, TerminalActions.PAGE_UP());
			defaultActionAliasToAction.put(LOOKUP, TerminalActions.F4());
		}
	}
}
