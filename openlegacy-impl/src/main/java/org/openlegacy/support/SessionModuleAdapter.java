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

/**
 * Place holder for session module future override-able methods
 * 
 */
public abstract class SessionModuleAdapter<S extends Session> implements SessionModule {

	private S session;

	public S getSession() {
		return session;
	}

	public void setSession(S session) {
		this.session = session;
	}
}
