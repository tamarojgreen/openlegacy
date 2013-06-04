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

/**
 * Define a session override-able methods which happens before & after a connection action
 * 
 */
public interface ApplicationConnectionListener {

	void beforeConnect(ApplicationConnection<?, ?> connection);

	void afterConnect(ApplicationConnection<?, ?> connection);

	void beforeAction(ApplicationConnection<?, ?> connection, RemoteAction action);

	void afterAction(ApplicationConnection<?, ?> connection, RemoteAction action, Snapshot result);

}
