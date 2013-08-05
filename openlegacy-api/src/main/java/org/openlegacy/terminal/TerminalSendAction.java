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

import org.openlegacy.RemoteAction;

/**
 * Defines a low level terminal send action on a {@link TerminalConnection}. Contains command, cursor position and modified
 * fields.
 * 
 * @author Roi Mor
 * 
 */
public interface TerminalSendAction extends RemoteAction<TerminalField> {

	Object getCommand();

	TerminalPosition getCursorPosition();

	void setCursorPosition(TerminalPosition fieldPosition);
}