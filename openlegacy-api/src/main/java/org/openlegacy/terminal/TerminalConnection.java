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
package org.openlegacy.terminal;

import org.openlegacy.ApplicationConnection;

import java.rmi.RemoteException;

/**
 * Emulation providers needs to implement this class
 */
public interface TerminalConnection extends ApplicationConnection<TerminalSnapshot, TerminalSendAction> {

	@Override
	TerminalSnapshot getSnapshot() throws RemoteException;

	@Override
	TerminalSnapshot fetchSnapshot() throws RemoteException;

	@Override
	void doAction(TerminalSendAction terminalSendAction) throws RemoteException;

	void flip() throws RemoteException;

	boolean isRightToLeftState() throws RemoteException;
}
