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
package org.openlegacy.terminal.modules.trail;

import org.openlegacy.ApplicationConnection;
import org.openlegacy.RemoteAction;
import org.openlegacy.Snapshot;
import org.openlegacy.exceptions.OpenlegacyRemoteRuntimeException;
import org.openlegacy.modules.trail.SessionTrail;
import org.openlegacy.modules.trail.Trail;
import org.openlegacy.terminal.TerminalSendAction;
import org.openlegacy.terminal.TerminalSnapshot;
import org.openlegacy.terminal.support.SimpleTerminalOutgoingSnapshot;
import org.openlegacy.terminal.support.TerminalSessionModuleAdapter;

import java.rmi.RemoteException;

public class DefaultTerminalTrailModule extends TerminalSessionModuleAdapter implements Trail {

	private static final long serialVersionUID = 1L;

	private SessionTrail<TerminalSnapshot> sessionTrail;

	@Override
	public SessionTrail<TerminalSnapshot> getSessionTrail() {
		return sessionTrail;
	}

	@Override
	public void beforeConnect(ApplicationConnection<?, ?> terminalConnection) {
		sessionTrail.clear();
	}

	@Override
	public void afterConnect(ApplicationConnection<?, ?> terminalConnection) {
		try {
			sessionTrail.appendSnapshot((TerminalSnapshot)terminalConnection.getSnapshot());
		} catch (RemoteException e) {
			throw (new OpenlegacyRemoteRuntimeException(e));
		}
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void beforeAction(ApplicationConnection<?, ?> terminalConnection, RemoteAction terminalSendAction) {
		try {
			sessionTrail.appendSnapshot(new SimpleTerminalOutgoingSnapshot((TerminalSnapshot)terminalConnection.getSnapshot(),
					(TerminalSendAction)terminalSendAction));
		} catch (RemoteException e) {
			throw (new OpenlegacyRemoteRuntimeException(e));
		}
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void afterAction(ApplicationConnection<?, ?> connection, RemoteAction action, Snapshot result) {
		try {
			sessionTrail.appendSnapshot((TerminalSnapshot)connection.getSnapshot());
		} catch (RemoteException e) {
			throw (new OpenlegacyRemoteRuntimeException(e));
		}
	}

	public void setSessionTrail(SessionTrail<TerminalSnapshot> sessionTrail) {
		this.sessionTrail = sessionTrail;
	}

}
