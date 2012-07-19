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
 * A common interface for a session
 * 
 */
public interface Session {

	Object getDelegate();

	<T> T getEntity(Class<T> entityClass) throws EntityNotFoundException;

	Object getEntity(String entityName) throws EntityNotFoundException;
	
	<M extends SessionModule> M getModule(Class<M> module);

	void disconnect();

	boolean isConnected();
}
