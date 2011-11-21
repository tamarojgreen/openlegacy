package org.openlegacy;

import org.openlegacy.exceptions.EntityNotFoundException;
import org.openlegacy.modules.SessionModule;

/**
 * A common interface for a session
 * 
 */
public interface Session {

	Object getDelegate();

	<T> T getEntity(Class<T> entityClass) throws EntityNotFoundException;

	<M extends SessionModule> M getModule(Class<M> module);

	void disconnect();

	boolean isConnected();
}
