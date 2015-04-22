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
package org.openlegacy.support;

import org.openlegacy.ApplicationConnection;
import org.openlegacy.ApplicationConnectionListener;
import org.openlegacy.RemoteAction;
import org.openlegacy.Session;
import org.openlegacy.Snapshot;
import org.openlegacy.modules.SessionModule;

import java.io.Serializable;

/**
 * Place holder for session module future override-able methods
 * 
 */
public abstract class SessionModuleAdapter<S extends Session> implements SessionModule, ApplicationConnectionListener, Serializable {

	private static final long serialVersionUID = 1L;

	private transient S session;

	/**
	 * for serialization purpose only
	 */
	public SessionModuleAdapter() {}

	@Override
	public void beforeConnect(ApplicationConnection<?, ?> connection) {
		// allow override
	}

	@Override
	public void afterConnect(ApplicationConnection<?, ?> connection) {
		// allow override
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void beforeAction(ApplicationConnection<?, ?> cconnection, RemoteAction action) {
		// allow override
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void afterAction(ApplicationConnection<?, ?> connection, RemoteAction action, Snapshot result) {
		// allow override
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void afterAction(ApplicationConnection<?, ?> connection, RemoteAction action, Snapshot result, String entityName) {
		// allow override
	}

	public S getSession() {
		return session;
	}

	public void setSession(S session) {
		this.session = session;
	}
}
