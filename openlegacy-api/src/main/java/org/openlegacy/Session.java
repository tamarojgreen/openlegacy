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
 */
public interface Session {

	Object getDelegate();

	<T> T getEntity(Class<T> entityClass) throws EntityNotFoundException;

	Object getEntity(String entityName) throws EntityNotFoundException;
	
	<M extends SessionModule> M getModule(Class<M> module);

	void disconnect();

	boolean isConnected();
}
