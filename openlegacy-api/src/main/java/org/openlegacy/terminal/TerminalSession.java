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
import org.openlegacy.terminal.services.TerminalSendAction;

/**
 * The main entry point for the terminal session. In addition to it's parent classes methods for retrieving the current screen and
 * Legacy vendors needs to implement this class
 */
public interface TerminalSession extends StatefullSession<TerminalSnapshot> {

	<R extends ScreenEntity> R doAction(TerminalAction action);

	/**
	 * 
	 * @param action
	 * @param screenEntity
	 * @return The current screen entity
	 */
	<S extends ScreenEntity, R extends ScreenEntity> R doAction(TerminalAction action, S screenEntity);

	<S extends ScreenEntity, R extends ScreenEntity> R doAction(TerminalAction action, S screenEntity,
			Class<R> expectedScreenEntity);

	void doAction(TerminalSendAction terminalSendAction);

	<S extends ScreenEntity> S getEntity();

	String getSessionId();

}
