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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openlegacy.Session;
import org.openlegacy.SessionProperties;
import org.openlegacy.SessionsRegistry;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class SimpleSessionsRegistry<S extends Session> implements SessionsRegistry<S> {

	private final static Log logger = LogFactory.getLog(SimpleSessionsRegistry.class);

	private Map<SessionProperties, S> sessions = new HashMap<SessionProperties, S>();
	private Map<String, S> sessionsById = new HashMap<String, S>();

	@Override
	public void register(S session) {
		SimpleSessionProperties sessionProperties = (SimpleSessionProperties)session.getProperties();
		if (sessionProperties.getId() == null) {
			String sessionId = UUID.randomUUID().toString();
			sessionProperties.setId(sessionId);
		}
		sessionProperties.setStartedOn(new Date());
		sessions.put(sessionProperties, session);
		sessionsById.put(sessionProperties.getId(), session);
	}

	@Override
	public void unregister(S session) {
		try {
			sessions.remove(session.getProperties());
			sessionsById.remove(session.getProperties().getId());
		} catch (Exception e) {
			logger.warn(e.getMessage());
		}

	}

	@Override
	public Set<SessionProperties> getSessionsProperties() {
		return sessions.keySet();
	}

	@Override
	public S getSession(String sessionId) {
		return sessionsById.get(sessionId);
	}

}
