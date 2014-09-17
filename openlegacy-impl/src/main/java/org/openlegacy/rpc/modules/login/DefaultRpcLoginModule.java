package org.openlegacy.rpc.modules.login;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openlegacy.exceptions.RegistryException;
import org.openlegacy.modules.login.Login;
import org.openlegacy.modules.login.LoginException;
import org.openlegacy.modules.login.User;
import org.openlegacy.rpc.support.RpcSessionModuleAdapter;
import org.openlegacy.terminal.modules.login.PersistedUser;

import java.io.Serializable;
import java.text.MessageFormat;

public class DefaultRpcLoginModule extends RpcSessionModuleAdapter implements Login, Serializable {

	private static final long serialVersionUID = 1L;

	private final static Log logger = LogFactory.getLog(DefaultRpcLoginModule.class);

	private User loggedInUser = null;

	public void login(String user, String password) throws LoginException, RegistryException {
		if (loggedInUser != null) {
			return;
		}
		getSession().login(user, password);

		loggedInUser = new PersistedUser(user);
		logger.info(MessageFormat.format("User {0} successfully logged in", loggedInUser.getUserName()));

	}

	public void login(Object loginEntity) throws LoginException, RegistryException {
		// not implemented

	}

	public boolean isLoggedIn() {

		return loggedInUser != null;
	}

	public User getLoggedInUser() {
		return loggedInUser;
	}

	public void logoff() {
		getSession().disconnect();
		logger.info(MessageFormat.format("User {0} successfully logged off", loggedInUser.getUserName()));
		loggedInUser = null;

	}

}
