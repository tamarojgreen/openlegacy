package org.openlegacy.rpc.modules.login;

import org.openlegacy.exceptions.RegistryException;
import org.openlegacy.modules.login.Login;
import org.openlegacy.modules.login.LoginException;
import org.openlegacy.modules.login.User;
import org.openlegacy.rpc.support.RpcSessionModuleAdapter;
import org.openlegacy.terminal.modules.login.PersistedUser;

import java.io.Serializable;

public class DefaultRpcLoginModule extends RpcSessionModuleAdapter implements Login, Serializable {

	private static final long serialVersionUID = 1L;

	private User loggedInUser = null;

	public void login(String user, String password) throws LoginException, RegistryException {
		if (loggedInUser != null) {
			return;
		}
		getSession().login(user, password);

		loggedInUser = new PersistedUser(user);

	}

	public void login(Object loginEntity) throws LoginException, RegistryException {
		// TODO Auto-generated method stub

	}

	public boolean isLoggedIn() {

		return loggedInUser != null;
	}

	public User getLoggedInUser() {
		return loggedInUser;
	}

	public void logoff() {
		getSession().disconnect();
		loggedInUser = null;

	}

}
