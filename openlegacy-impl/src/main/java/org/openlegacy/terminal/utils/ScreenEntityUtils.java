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

import org.openlegacy.definitions.ActionDefinition;
import org.openlegacy.definitions.FieldDefinition;
import org.openlegacy.terminal.ScreenEntity;
import org.openlegacy.terminal.ScreenPojoFieldAccessor;
import org.openlegacy.terminal.TerminalSession;
import org.openlegacy.terminal.actions.TerminalAction;
import org.openlegacy.terminal.actions.TerminalActions;
import org.openlegacy.terminal.definitions.ScreenEntityDefinition;
import org.openlegacy.terminal.services.ScreenEntitiesRegistry;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

@Component
public class ScreenEntityUtils {

	@Inject
	private ScreenEntitiesRegistry screenEntitiesRegistry;

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
		if (actionAlias == null) {
			sessionAction = TerminalActions.ENTER();
		} else {
			List<ActionDefinition> actions = entityDefinitions.getActions();
			for (ActionDefinition actionDefinition : actions) {
				if (actionDefinition.getAlias().equals(actionAlias)) {
					sessionAction = (TerminalAction)actionDefinition.getAction();
				}
			}
		}
		Assert.notNull(sessionAction, MessageFormat.format("Alias for session action {0} not found", actionAlias));
		terminalSession.doAction(sessionAction, screenEntity);
	}

}
