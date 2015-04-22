/*******************************************************************************
 * Copyright (c) 2015 OpenLegacy Inc.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     OpenLegacy Inc. - initial API and implementation
 *******************************************************************************/

package org.openlegacy.remoting.web.backend;

import org.openlegacy.modules.login.LoginException;

import java.io.IOException;
import java.util.List;

/**
 * @author Ivan Bort
 */
public interface RemotingBackendScreensRestController {

	public void login(String user, String password) throws LoginException;

	public void loginPostJson(String json) throws Exception;

	public List<String> messages() throws IOException;

	public void logoff();
}
