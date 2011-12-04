package org.openlegacy.support;

import org.openlegacy.Session;
import org.openlegacy.modules.SessionModule;

/**
 * Place holder for session module future override-able methods
 * 
 */
public class SessionModuleAdapter<S extends Session> implements SessionModule {

	private S session;

	public S getSession() {
		return session;
	}

	public void setSession(S session) {
		this.session = session;
	}
}
