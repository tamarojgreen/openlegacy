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
package org.openlegacy.terminal.support.binders;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openlegacy.definitions.ActionDefinition;
import org.openlegacy.terminal.ScreenEntityBinder;
import org.openlegacy.terminal.TerminalField;
import org.openlegacy.terminal.TerminalSendAction;
import org.openlegacy.terminal.TerminalSnapshot;
import org.openlegacy.terminal.definitions.ScreenEntityDefinition;
import org.openlegacy.terminal.definitions.ScreenTableDefinition;
import org.openlegacy.terminal.definitions.TerminalActionDefinition;
import org.openlegacy.terminal.services.ScreenEntitiesRegistry;
import org.openlegacy.utils.ReflectionUtil;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

public class ScreenEntityTableActionsBinder implements ScreenEntityBinder {

	private static final long serialVersionUID = 1L;

	private final static Log logger = LogFactory.getLog(ScreenEntityTableActionsBinder.class);

	@Inject
	private ScreenEntitiesRegistry entitiesRegistry;

	@SuppressWarnings("unchecked")
	@Override
	public void populateEntity(Object screenEntity, TerminalSnapshot terminalSnapshot) {

		ScreenEntityDefinition entityDefinition = entitiesRegistry.get(screenEntity.getClass());

		Collection<ScreenTableDefinition> tableDefinitions = entityDefinition.getTableDefinitions().values();
		for (ScreenTableDefinition screenTableDefinition : tableDefinitions) {
			List<ActionDefinition> actions = screenTableDefinition.getActions();

			List<TerminalActionDefinition> entityActions = new ArrayList<TerminalActionDefinition>();
			for (ActionDefinition actionDefinition : actions) {
				TerminalActionDefinition terminalActionDefinition = (TerminalActionDefinition)actionDefinition;
				if (terminalActionDefinition.getPosition() != null && terminalActionDefinition.getLength() > 0
						&& !terminalActionDefinition.getWhen().equals(".*")) {
					TerminalField terminalField = terminalSnapshot.getField(terminalActionDefinition.getPosition());
					if (terminalField != null && terminalField.isHidden()) {
						continue;
					}
					String text = terminalSnapshot.getText(terminalActionDefinition.getPosition(),
							terminalActionDefinition.getLength());
					if (text.matches(terminalActionDefinition.getWhen())) {
						entityActions.add(terminalActionDefinition);
					}
				} else {
					entityActions.add(terminalActionDefinition);
				}
			}

			String tableName = StringUtils.capitalize(screenTableDefinition.getTableEntityName());
			String methodName = MessageFormat.format("get{0}sActions", tableName);
			try {
				List<ActionDefinition> tableActions = (List<ActionDefinition>)ReflectionUtil.invoke(screenEntity, methodName);
				if (tableActions != null) {
					tableActions.addAll(entityActions);
				}
			} catch (RuntimeException e) {
				logger.warn(MessageFormat.format("No {0} method found in entity:{1}", methodName,
						entityDefinition.getEntityName()));
			}

		}
	}

	@Override
	public void populateAction(TerminalSendAction remoteAction, TerminalSnapshot snapshot, Object entity) {
		// do nothing

	}

}
