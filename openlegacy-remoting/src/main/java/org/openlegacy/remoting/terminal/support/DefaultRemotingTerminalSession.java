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

package org.openlegacy.remoting.terminal.support;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openlegacy.EntityWrapper;
import org.openlegacy.exceptions.EntityNotFoundException;
import org.openlegacy.modules.login.Login;
import org.openlegacy.modules.login.LoginException;
import org.openlegacy.modules.menu.Menu;
import org.openlegacy.modules.menu.MenuItem;
import org.openlegacy.modules.messages.Messages;
import org.openlegacy.modules.navigation.Navigation;
import org.openlegacy.remoting.json.EntitySerializationUtils;
import org.openlegacy.remoting.terminal.IRemotingTerminalSession;
import org.openlegacy.support.SimpleEntityWrapper;
import org.openlegacy.terminal.services.ScreenEntitiesRegistry;
import org.openlegacy.terminal.support.DefaultTerminalSession;
import org.openlegacy.utils.ProxyUtil;
import org.openlegacy.utils.UrlUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * @author Ivan Bort
 */
public class DefaultRemotingTerminalSession extends DefaultTerminalSession implements IRemotingTerminalSession {

	private static final long serialVersionUID = 1L;

	private static final Log logger = LogFactory.getLog(DefaultRemotingTerminalSession.class);

	@Inject
	private ScreenEntitiesRegistry screenEntitiesRegistry;

	/**
	 * Whether to perform login on session start. Can be overridden from /application.properties
	 * defaultRestController.requiresLogin=true
	 */
	private boolean requiresLogin = false;

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
		Login loginModule = getModule(Login.class);
		if (loginModule != null) {
			loginModule.login(user, password);
		} else {
			logger.warn("No login module defined. Skipping login");
		}
	}

	@Override
	public Object getMenu() throws RuntimeException {
		Menu menuModule = getModule(Menu.class);
		if (menuModule == null) {
			return null;
		}
		MenuItem menus = menuModule.getMenuTree();
		return menus;
	}

	@Override
	public Object loginPostJson(String json) throws Exception {
		json = UrlUtil.decode(json, "{");
		if (!enableLogin) {
			throw (new UnsupportedOperationException("/login is not support"));
		}

		try {
			LoginObject login = EntitySerializationUtils.deserialize(json, LoginObject.class);
			getModule(org.openlegacy.modules.login.Login.class).login(login.getUser(), login.getPassword());
		} catch (LoginException e) {
			disconnect();
			throw e;
		} catch (Exception e) {
			logger.fatal("Invalid login", e);
			throw e;
		}

		return getMenu();
	}

	@Override
	public boolean authenticate() throws LoginException {
		if (!requiresLogin) {
			return true;
		}
		org.openlegacy.modules.login.Login loginModule = getModule(org.openlegacy.modules.login.Login.class);
		if (!loginModule.isLoggedIn()) {
			throw new LoginException("User unauthorized!");
		}
		return true;

	}

	protected Object getApiEntity(String entityName, String key) {
		Object entity;
		Object[] keys = new Object[0];
		if (key != null) {
			keys = key.split("\\+");
		}

		if (key == null) {
			entity = getEntity(entityName);
		} else {
			entity = getEntity(entityName, keys);
		}
		return entity;
	}

	protected EntityWrapper getEntityInner(Object entity, boolean children) {
		if (entity == null) {
			throw (new EntityNotFoundException("No entity found"));
		}
		entity = ProxyUtil.getTargetObject(entity, children);
		Navigation navigationModule = getModule(Navigation.class);
		boolean isWindow = screenEntitiesRegistry.get(entity.getClass()).isWindow();
		SimpleEntityWrapper wrapper = new SimpleEntityWrapper(entity, navigationModule != null ? navigationModule.getPaths()
				: null, null, isWindow);
		return wrapper;
	}

	public EntityWrapper getEntityRequest(String entityName, String key, boolean children) throws EntityNotFoundException,
			LoginException {
		if (!authenticate()) {
			return null;
		}
		try {
			Object entity = getApiEntity(entityName, key);
			return getEntityInner(entity, children);
		} catch (EntityNotFoundException e) {
			logger.fatal(e, e);
			throw e;
		}
	}

	@Override
	public List<String> messages() throws IOException {
		Messages messagesModule = getModule(Messages.class);
		List<String> messages = messagesModule.getMessages();
		if (messages.size() > 0) {
			List<String> list = new ArrayList<String>(messages);
			messagesModule.resetMessages();
			return list;
		}
		return null;
	}

	public void setRequiresLogin(boolean requiresLogin) {
		this.requiresLogin = requiresLogin;
	}

	public void setEnableLogin(boolean enableLogin) {
		this.enableLogin = enableLogin;
	}

	public void setEnableGetLogin(boolean enableGetLogin) {
		this.enableGetLogin = enableGetLogin;
	}

	public static class LoginObject {

		private String user;
		private String password;

		public String getUser() {
			return user;
		}

		public void setUser(String user) {
			this.user = user;
		}

		public String getPassword() {
			return password;
		}

		public void setPassword(String password) {
			this.password = password;
		}

	}

}
