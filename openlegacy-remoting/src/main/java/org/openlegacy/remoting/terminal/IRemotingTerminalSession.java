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

package org.openlegacy.remoting.terminal;

import org.openlegacy.EntityWrapper;
import org.openlegacy.exceptions.EntityNotFoundException;
import org.openlegacy.modules.login.LoginException;
import org.openlegacy.terminal.TerminalSession;

import java.io.IOException;
import java.util.List;

/**
 * @author Ivan Bort
 */
public interface IRemotingTerminalSession extends TerminalSession {

	public void login(String user, String password) throws LoginException;

	public Object getMenu() throws IOException;

	public Object loginPostJson(String json) throws Exception;

	public boolean authenticate() throws LoginException;

	public EntityWrapper getEntityRequest(String entityName, String key, boolean children) throws EntityNotFoundException,
			LoginException;

	public List<String> messages() throws IOException;
}
