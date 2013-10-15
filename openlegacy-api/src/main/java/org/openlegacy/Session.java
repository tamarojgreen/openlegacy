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
package org.openlegacy;

import org.openlegacy.exceptions.EntityNotFoundException;
import org.openlegacy.modules.SessionModule;

/**
 * An abstract interface for a session. A session is logical, and provides additional logic over it's connection:
 * {@link ApplicationConnection}
 * 
 * @author Roi Mor
 * 
 * @see ApplicationConnection
 */
public interface Session {

	/**
	 * Returns the underlying connection provider implementation. Should be cast to the actual implementation. Useful if you wish
	 * to use specific provider implementation.
	 * 
	 * @return provider implementation session
	 */
	Object getDelegate();

	/**
	 * Returns an entity of the given class and given keys. Keys can be empty, if no keys defined for the given entity.
	 * 
	 * @param entityClass
	 *            the request entity class
	 * @param keys
	 *            keys of the request entity
	 * @return an entity instance from the session which matches the request entity class and keys
	 * @throws EntityNotFoundException
	 *             if the entity is not found
	 */
	<T> T getEntity(Class<T> entityClass, Object... keys) throws EntityNotFoundException;

	/**
	 * Returns an entity of the given name and given keys. Entity default names are the class simple name, unless specified. Keys
	 * can be empty, if no keys defined for the given entity.
	 * 
	 * @param entityName
	 *            the request entity name keys of the request entity
	 * @return an entity instance from the session which matches the request entity class and keys
	 * @throws EntityNotFoundException
	 *             if the entity is not found
	 */
	Object getEntity(String entityName, Object... keys) throws EntityNotFoundException;

	/**
	 * Returns a session module by the module class name.
	 * 
	 * @param module
	 *            a module class
	 * @return a session module
	 */
	<M extends SessionModule> M getModule(Class<M> module);

	/**
	 * Disconnect the session
	 */
	void disconnect();

	/**
	 * Is the session connected
	 * 
	 * @return
	 */
	boolean isConnected();

	SessionProperties getProperties();

}
