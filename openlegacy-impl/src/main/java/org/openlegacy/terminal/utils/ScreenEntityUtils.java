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
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openlegacy.definitions.ActionDefinition;
import org.openlegacy.definitions.FieldDefinition;
import org.openlegacy.terminal.ScreenEntity;
import org.openlegacy.terminal.ScreenPojoFieldAccessor;
import org.openlegacy.terminal.TerminalSession;
import org.openlegacy.terminal.actions.TerminalAction;
import org.openlegacy.terminal.actions.TerminalActions;
import org.openlegacy.terminal.definitions.ScreenEntityDefinition;
import org.openlegacy.terminal.services.ScreenEntitiesRegistry;
import org.openlegacy.test.utils.AssertUtils;
import org.openlegacy.utils.ProxyUtil;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

public class ScreenEntityUtils implements InitializingBean {

	private static final String NEXT = "next";
	private static final String PREVIOUS = "previous";

	private final static Log logger = LogFactory.getLog(AssertUtils.class);

	/**
	 * For demo's purposes
	 */
	private boolean returnTrueOnDifferentKeys = false;

	@Inject
	private ScreenEntitiesRegistry screenEntitiesRegistry;

	/**
	 * A map of action alias to actual terminal action
	 */
	private Map<String, TerminalAction> defaultActionAliasToAction;

	/**
	 * Find the all screen fields marked as key=true
	 * 
	 * @param entity
	 * @return
	 */
	public List<Object> getKeysValues(ScreenEntity entity) {
		ScreenEntityDefinition definitions = screenEntitiesRegistry.get(entity.getClass());
		List<? extends FieldDefinition> keyFields = definitions.getKeys();
		List<Object> keysValue = new ArrayList<Object>();
		ScreenPojoFieldAccessor fieldAccessor = new SimpleScreenPojoFieldAccessor(entity);
		for (FieldDefinition fieldDefinition : keyFields) {
			keysValue.add(fieldAccessor.getFieldValue(fieldDefinition.getName()));
		}
		return keysValue;
	}

	public boolean isEntitiesEquals(ScreenEntity entity, Class<?> screenEntityClass, Object... requestedEntityKeys) {
		if (entity == null) {
			return false;
		}
		if (!ProxyUtil.isClassesMatch(entity.getClass(), screenEntityClass)) {
			return false;
		}

		List<Object> actualEntityKeysValues = getKeysValues(entity);
		if (requestedEntityKeys == null && actualEntityKeysValues.size() == 0) {
			return true;
		}

		// it's OK that entity is requested with no keys (sub screen for example within main screen context). False only if
		// request and no/less keys defined on screen
		if (requestedEntityKeys.length > actualEntityKeysValues.size()) {
			return false;
		}

		for (int i = 0; i < requestedEntityKeys.length; i++) {
			Object actualEntityKeyValue = actualEntityKeysValues.get(i);
			if (!actualEntityKeyValue.toString().equals(requestedEntityKeys[i].toString())) {
				if (returnTrueOnDifferentKeys) {
					logger.warn(MessageFormat.format(
							"Request entity key:{0} doesn''t match current entity key:{1}. Skipping as screenEntityUtils.returnTrueOnDifferentKeys was set to true",
							requestedEntityKeys[i], actualEntityKeyValue));
					return true;
				} else {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * Sends the provided screen entity and action alias over the given terminal session
	 * 
	 * @param terminalSession
	 *            A terminal session to perform over the send action
	 * @param screenEntity
	 *            The screen entity to send
	 * @param actionAlias
	 *            An action alias which belongs to the screen entity
	 */
	public void sendScreenEntity(TerminalSession terminalSession, ScreenEntity screenEntity, String actionAlias) {
		ScreenEntityDefinition entityDefinitions = screenEntitiesRegistry.get(screenEntity.getClass());
		TerminalAction sessionAction = null;
		if (StringUtils.isEmpty(actionAlias)) {
			sessionAction = TerminalActions.ENTER();
		} else {
			List<ActionDefinition> actions = entityDefinitions.getActions();
			for (ActionDefinition actionDefinition : actions) {
				if (actionDefinition.getAlias().equals(actionAlias)) {
					sessionAction = (TerminalAction)actionDefinition.getAction();
				}
			}
			if (sessionAction == null) {
				sessionAction = defaultActionAliasToAction.get(actionAlias);
			}
		}
		Assert.notNull(sessionAction, MessageFormat.format("Alias for session action {0} not found", actionAlias));
		terminalSession.doAction(sessionAction, screenEntity);
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

	public String getEntityName(ScreenEntity screenEntity) {
		String screenEntityName = ProxyUtil.getOriginalClass(screenEntity.getClass()).getSimpleName();
		return screenEntityName;
	}

	public void setReturnTrueOnDifferentKeys(boolean returnTrueOnDifferentKeys) {
		this.returnTrueOnDifferentKeys = returnTrueOnDifferentKeys;
	}
}
