package org.openlegacy.db.modules.login;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openlegacy.exceptions.RegistryException;
import org.openlegacy.modules.login.Login;
import org.openlegacy.modules.login.LoginException;
import org.openlegacy.modules.login.User;

import java.io.Serializable;
import java.text.MessageFormat;

public class EmptyDbLoginModule implements Login, Serializable {

	private static final long serialVersionUID = 1L;

	private final static Log logger = LogFactory.getLog(EmptyDbLoginModule.class);

	private User loggedInUser = null;

	public void login(String user, String password) throws LoginException, RegistryException {
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
		logger.info(MessageFormat.format("User {0} successfully logged off", loggedInUser.getUserName()));
		loggedInUser = null;

	}

	public void destroy() {
		// TODO Auto-generated method stub

	}

}
