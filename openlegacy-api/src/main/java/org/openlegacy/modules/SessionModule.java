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
package org.openlegacy.modules;

/**
 * Define a plug-able module into a session. Purpose is to allow extending the session functionality with functionality such as:
 * {@link org.openlegacy.modules.trail.Trail}, {@link org.openlegacy.modules.login.Login},
 * {@link org.openlegacy.modules.menu.Menu}, etc <br/>
 * Example: <code>terminalSession.getModule(Login.class).login("user","password")</code>
 * 
 * @author Roi Mor
 * 
 */
public interface SessionModule {

	void destroy();
}
