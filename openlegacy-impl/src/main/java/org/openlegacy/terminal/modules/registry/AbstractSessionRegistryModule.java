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
package org.openlegacy.terminal.modules.registry;

import org.openlegacy.ApplicationConnection;
import org.openlegacy.RemoteAction;
import org.openlegacy.SessionsRegistry;
import org.openlegacy.Snapshot;
import org.openlegacy.modules.registry.Registry;
import org.openlegacy.support.SessionModuleAdapter;
import org.openlegacy.support.SimpleSessionProperties;

import java.util.Date;

import javax.inject.Inject;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class AbstractSessionRegistryModule extends SessionModuleAdapter implements Registry {

	private static final long serialVersionUID = 1L;

	@Inject
	private SessionsRegistry sessionsRegistry;

	@Override
	public void beforeConnect(ApplicationConnection connection) {
		sessionsRegistry.register(getSession());
	}

	@Override
	public void afterConnect(ApplicationConnection terminalConnection) {
		setLastActivity();
	}

	@Override
	public void afterAction(ApplicationConnection connection, RemoteAction action, Snapshot result) {
		setLastActivity();
	}

	public void destroy() {
		sessionsRegistry.unregister(getSession());
	}

	private void setLastActivity() {
		((SimpleSessionProperties)getSession().getProperties()).setLastActivity(new Date());
	}

}
