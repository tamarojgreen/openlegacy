package org.openlegacy;

import org.openlegacy.exceptions.HostEntityNotFoundException;
import org.openlegacy.trail.SessionTrail;

/**
 * A common interface for a host session
 * 
 */
public interface HostSession {

	<T> T getEntity(Class<T> hostEntity) throws HostEntityNotFoundException;

	SessionTrail getSessionTrail();

	void setSessionTrail(SessionTrail sessionTrail);
}
