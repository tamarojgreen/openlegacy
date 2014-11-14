package org.openlegacy.demo.db.modules;

import org.openlegacy.exceptions.RegistryException;
import org.openlegacy.modules.login.Login;
import org.openlegacy.modules.login.LoginException;
import org.openlegacy.modules.login.User;
import org.openlegacy.terminal.modules.login.PersistedUser;

public class LoginModule implements Login {

	private User loggedInUser = null;

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

	@Override
	public void login(String user, String password) throws LoginException, RegistryException {
		if (loggedInUser != null) {
			return;
		}

		loggedInUser = new PersistedUser(user);
	}

	@Override
	public void login(Object loginEntity) throws LoginException, RegistryException {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isLoggedIn() {
		return loggedInUser != null;
	}

	@Override
	public User getLoggedInUser() {
		return loggedInUser;
	}

	@Override
	public void logoff() {
		loggedInUser = null;
	}

}
