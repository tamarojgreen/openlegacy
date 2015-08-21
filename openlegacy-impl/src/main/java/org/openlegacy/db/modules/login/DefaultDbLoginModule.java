package org.openlegacy.db.modules.login;

import org.apache.commons.dbcp.BasicDataSource;
import org.openlegacy.db.support.DbSessionModuleAdapter;
import org.openlegacy.exceptions.RegistryException;
import org.openlegacy.modules.login.Login;
import org.openlegacy.modules.login.LoginException;
import org.openlegacy.modules.login.User;
import org.openlegacy.terminal.modules.login.PersistedUser;

import java.sql.DriverManager;
import java.sql.SQLException;

import javax.inject.Inject;

public class DefaultDbLoginModule extends DbSessionModuleAdapter implements Login {

	private static final long serialVersionUID = 1L;

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
			DriverManager.getConnection(dataSource.getUrl(), user, password);
		} catch (SQLException e) {
			throw (new LoginException(e.getMessage()));
		}
		loggedInUser = new PersistedUser(user);
		getSession().login(user, password);
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
