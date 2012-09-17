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
package org.openlegacy.terminal.support;

import org.openlegacy.support.SessionModuleAdapter;
import org.openlegacy.terminal.TerminalConnection;
import org.openlegacy.terminal.TerminalConnectionListener;
import org.openlegacy.terminal.TerminalSendAction;
import org.openlegacy.terminal.TerminalSession;
import org.openlegacy.terminal.TerminalSessionModule;

import java.io.Serializable;

/**
 * Define a terminal session override-able methods which happens before & after a terminal session action
 * 
 */
public abstract class TerminalSessionModuleAdapter extends SessionModuleAdapter<TerminalSession> implements TerminalSessionModule, TerminalConnectionListener, Serializable {

	private static final long serialVersionUID = 1L;

	public void beforeConnect(TerminalConnection terminalConnection) {
		// allow override
	}

	public void afterConnect(TerminalConnection terminalConnection) {
		// allow override
	}

	public void beforeSendAction(TerminalConnection terminalConnection, TerminalSendAction terminalSendAction) {
		// allow override
	}

	public void afterSendAction(TerminalConnection terminalConnection) {
		// allow override
	}

	public void destroy() {
		// allow override
	}
}
