package org.openlegacy;

/**
 * A common interface for a host session factory
 * 
 * @param <T>
 *            - the target type of HostSession
 */
public interface HostSessionFactory<T extends HostSession> {

	T getSession();
}
