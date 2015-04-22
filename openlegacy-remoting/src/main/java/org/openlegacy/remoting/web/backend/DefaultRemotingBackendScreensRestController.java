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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openlegacy.json.EntitySerializationUtils;
import org.openlegacy.modules.login.Login;
import org.openlegacy.modules.login.LoginException;
import org.openlegacy.modules.messages.Messages;
import org.openlegacy.mvc.AbstractRestController.LoginObject;
import org.openlegacy.terminal.TerminalSession;
import org.openlegacy.utils.UrlUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Ivan Bort
 */
public class DefaultRemotingBackendScreensRestController implements RemotingBackendScreensRestController {

	private static final Log logger = LogFactory.getLog(DefaultRemotingBackendScreensRestController.class);

	private TerminalSession terminalSession;

	/**
	 * Whether to enable /login URL calls. Can be overridden from /application.properties
	 */
	private boolean enableLogin = true;

	/**
	 * Whether to enable /login via URL GET calls. Can be overridden from /application.properties
	 */
	private boolean enableGetLogin = true;

	@Override
	public void login(String user, String password) throws LoginException {
		if (!enableLogin || !enableGetLogin) {
			throw (new UnsupportedOperationException("/login is not support"));
		}
		Login loginModule = terminalSession.getModule(Login.class);
		if (loginModule != null) {
			loginModule.login(user, password);
		} else {
			logger.warn("No login module defined. Skipping login");
		}
	}

	@Override
	public void loginPostJson(String json) throws Exception {
		json = UrlUtil.decode(json, "{");
		if (!enableLogin) {
			throw (new UnsupportedOperationException("/login is not support"));
		}

		try {
			LoginObject login = EntitySerializationUtils.deserialize(json, LoginObject.class);
			terminalSession.getModule(org.openlegacy.modules.login.Login.class).login(login.getUser(), login.getPassword());
		} catch (LoginException e) {
			terminalSession.disconnect();
			throw e;
		} catch (Exception e) {
			logger.fatal("Invalid login", e);
			throw e;
		}

	}

	@Override
	public List<String> messages() throws IOException {
		Messages messagesModule = terminalSession.getModule(Messages.class);
		List<String> messages = messagesModule.getMessages();
		if (messages.size() > 0) {
			List<String> list = new ArrayList<String>(messages);
			messagesModule.resetMessages();
			return list;
		}
		return null;
	}

	@Override
	public void logoff() {
		Login loginModule = terminalSession.getModule(Login.class);
		if (loginModule != null) {
			loginModule.logoff();
		} else {
			terminalSession.disconnect();
		}
	}

	public void setEnableLogin(boolean enableLogin) {
		this.enableLogin = enableLogin;
	}

	public void setEnableGetLogin(boolean enableGetLogin) {
		this.enableGetLogin = enableGetLogin;
	}

	public void setTerminalSession(TerminalSession terminalSession) {
		this.terminalSession = terminalSession;
	}

}
