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
package org.openlegacy.providers.applinx;

import com.sabratec.applinx.baseobject.GXClientBaseObjectFactory;
import com.sabratec.applinx.baseobject.GXGeneralException;
import com.sabratec.applinx.baseobject.GXIClientBaseObject;

import org.openlegacy.exceptions.OpenLegacyProviderException;
import org.openlegacy.terminal.ConnectionProperties;
import org.openlegacy.terminal.TerminalConnection;
import org.openlegacy.terminal.TerminalConnectionFactory;

import javax.inject.Inject;

public class ApxTerminalConnctionFactory implements TerminalConnectionFactory {

	@Inject
	private ApxServerLoader apxServerLoader;

	public TerminalConnection getConnection(ConnectionProperties connectionProperties) {
		try {
			return new ApxTerminalConnection(apxServerLoader.getSession(connectionProperties));
		} catch (GXGeneralException e) {
			throw (new OpenLegacyProviderException(e));
		}
	}

	public void disconnect(TerminalConnection terminalConnection) {
		try {
			GXClientBaseObjectFactory.endSession((GXIClientBaseObject)terminalConnection.getDelegate());
		} catch (GXGeneralException e) {
			throw (new OpenLegacyProviderException(e));
		}
	}
}
