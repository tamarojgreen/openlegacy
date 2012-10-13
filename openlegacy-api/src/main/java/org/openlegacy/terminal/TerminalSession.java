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
package org.openlegacy.terminal;

import org.openlegacy.StatefullSession;
import org.openlegacy.terminal.actions.TerminalAction;
import org.openlegacy.terminal.actions.TerminalActions;
import org.openlegacy.terminal.exceptions.ScreenEntityNotAccessibleException;

/**
 * The main entry point for the terminal session. In addition to it's parent classes methods for retrieving the current screen and
 * Legacy vendors needs to implement this class
 */
public interface TerminalSession extends StatefullSession<TerminalSnapshot> {

	/**
	 * Performs the given action on the session. An action is typically defined in {@link TerminalActions} which has an underlying
	 * mapping to command, but can be a custom action implementation
	 * 
	 * @param action
	 * @return
	 * 
	 * @see TerminalActions
	 * 
	 */
	<R extends ScreenEntity> R doAction(TerminalAction action);

	/**
	 * Performs the given action, along the screen entity fields.
	 * 
	 * @param action
	 *            the action to perform
	 * @param screenEntity
	 *            the screen entity to send
	 * @return The current screen entity
	 */
	<S extends ScreenEntity, R extends ScreenEntity> R doAction(TerminalAction action, S screenEntity);

	/**
	 * Performs the given action, along the screen entity fields, and defines the expected resulting screen
	 * 
	 * @param action
	 * @param screenEntity
	 * @param expectedScreenEntity
	 * @return the current screen entity.
	 * @exception ScreenEntityNotAccessibleException
	 *                in case the expected entity is not reached
	 */
	<S extends ScreenEntity, R extends ScreenEntity> R doAction(TerminalAction action, S screenEntity,
			Class<R> expectedScreenEntity) throws ScreenEntityNotAccessibleException;

	/**
	 * Performs a terminal level send action
	 * 
	 * @param terminalSendAction
	 *            the terminal send action to perform
	 * @see TerminalSendAction
	 */
	void doAction(TerminalSendAction terminalSendAction);

	<S extends ScreenEntity> S getEntity();

	String getSessionId();

	Integer getSequence();

}
