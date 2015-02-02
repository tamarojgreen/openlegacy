package org.openlegacy.db.modules.login;

import org.apache.commons.dbcp.BasicDataSource;
import org.openlegacy.exceptions.RegistryException;
import org.openlegacy.modules.login.Login;
import org.openlegacy.modules.login.LoginException;
import org.openlegacy.modules.login.User;
import org.openlegacy.terminal.modules.login.PersistedUser;

import java.sql.Connection;
import java.sql.SQLException;

import javax.inject.Inject;

public class LoginModule implements Login {

	@Inject
	private BasicDataSource dataSource;

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
		try {
			Connection asd = dataSource.getConnection(user, password);
			asd.commit();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
