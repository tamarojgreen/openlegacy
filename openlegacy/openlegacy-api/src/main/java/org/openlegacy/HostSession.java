package org.openlegacy;

import org.openlegacy.exceptions.HostEntityNotFoundException;
import org.openlegacy.modules.HostSessionModule;

/**
 * A common interface for a host session
 * 
 */
public interface HostSession {

	<T> T getEntity(Class<T> hostEntity) throws HostEntityNotFoundException;

	<M extends HostSessionModule> M getModule(Class<M> module);

	void disconnect();

	boolean isConnected();
}
