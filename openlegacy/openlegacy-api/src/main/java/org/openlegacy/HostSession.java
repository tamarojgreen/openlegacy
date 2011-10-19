package org.openlegacy;

import org.openlegacy.exceptions.HostEntityNotFoundException;

/**
 * A common interface for a host session
 *
 */
public interface HostSession {

	<T> T getEntity(Class<T> hostEntity) throws HostEntityNotFoundException;
}
