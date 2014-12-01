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

import org.openlegacy.definitions.ActionDefinition;
import org.openlegacy.terminal.ScreenEntity;
import org.openlegacy.terminal.ScreenEntityBinder;
import org.openlegacy.terminal.TerminalField;
import org.openlegacy.terminal.TerminalSendAction;
import org.openlegacy.terminal.TerminalSnapshot;
import org.openlegacy.terminal.definitions.ScreenEntityDefinition;
import org.openlegacy.terminal.definitions.TerminalActionDefinition;
import org.openlegacy.terminal.services.ScreenEntitiesRegistry;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class ScreenEntityActionsBinder implements ScreenEntityBinder {

	@Inject
	private ScreenEntitiesRegistry entitiesRegistry;

	@Override
	public void populateEntity(Object screenEntity, TerminalSnapshot terminalSnapshot) {

		ScreenEntityDefinition entityDefinition = entitiesRegistry.get(screenEntity.getClass());
		List<ActionDefinition> actions = entityDefinition.getActions();

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

		((ScreenEntity)screenEntity).getActions().addAll(entityActions);
	}

	@Override
	public void populateAction(TerminalSendAction remoteAction, TerminalSnapshot snapshot, Object entity) {
		// do nothing

	}

}
