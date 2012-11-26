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
package org.openlegacy.support;

import org.openlegacy.Session;
import org.openlegacy.modules.SessionModule;

import java.io.Serializable;

/**
 * Place holder for session module future override-able methods
 * 
 */
public abstract class SessionModuleAdapter<S extends Session> implements SessionModule, Serializable {

	private static final long serialVersionUID = 1L;

	private S session;

	/**
	 * for serialization purpose only
	 */
	public SessionModuleAdapter() {}

	public S getSession() {
		return session;
	}

	public void setSession(S session) {
		this.session = session;
	}
}
