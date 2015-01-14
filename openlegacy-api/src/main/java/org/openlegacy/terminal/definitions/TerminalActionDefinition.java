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
package org.openlegacy.terminal.definitions;

import org.openlegacy.annotations.screen.Action;
import org.openlegacy.definitions.ActionDefinition;
import org.openlegacy.terminal.TerminalPosition;
import org.openlegacy.terminal.TerminalPositionContainer;
import org.openlegacy.terminal.actions.TerminalAction.AdditionalKey;
import org.openlegacy.terminal.actions.TerminalActions.SimpleTerminalMappedAction;
import org.openlegacy.terminal.services.ScreenEntitiesRegistry;

/**
 * A session action definition. Translated from {@link Action} and store within a {@link ScreenEntityDefinition} into
 * {@link ScreenEntitiesRegistry}
 * 
 * @author Roi Mor
 * 
 */
public interface TerminalActionDefinition extends ActionDefinition, TerminalPositionContainer {

	@Override
	TerminalPosition getPosition();

	AdditionalKey getAdditionalKey();

	String getFocusField();

	/**
	 * Together with row, column define the actual position of the action on the screen
	 * 
	 * @return
	 */
	@Override
	int getLength();

	/**
	 * Together with row, column, length defines if to show the action
	 * 
	 * @return
	 */
	@Override
	String getWhen();

	int getSleep();

	Class<? extends SimpleTerminalMappedAction> getKeyboardKey();
}
