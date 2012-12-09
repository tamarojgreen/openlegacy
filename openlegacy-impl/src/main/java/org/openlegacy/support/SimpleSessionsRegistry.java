package org.openlegacy.support;

import org.openlegacy.Session;
import org.openlegacy.SessionProperties;
import org.openlegacy.SessionsRegistry;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class SimpleSessionsRegistry<S extends Session> implements SessionsRegistry<S> {

	private Map<SessionProperties, S> sessions = new HashMap<SessionProperties, S>();
	private Map<String, S> sessionsById = new HashMap<String, S>();

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

	public void unregister(S session) {
		sessions.remove(session.getProperties());
		sessionsById.remove(session.getProperties().getId());

	}

	public Set<SessionProperties> getSessionsProperties() {
		return sessions.keySet();
	}

	public S getSession(String sessionId) {
		return sessionsById.get(sessionId);
	}

}
