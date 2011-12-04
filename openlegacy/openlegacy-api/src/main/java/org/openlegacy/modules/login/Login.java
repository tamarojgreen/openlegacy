package org.openlegacy.modules.login;

import org.openlegacy.EntityType;
import org.openlegacy.FieldType;
import org.openlegacy.exceptions.RegistryException;
import org.openlegacy.modules.SessionModule;

/**
 * A Login module is able to perform a login/log-off based on field and entity declaration
 * 
 */
public interface Login extends SessionModule {

	void login(String user, String password) throws LoginException, RegistryException;

	/**
	 * 
	 * @param loginEntity
	 *            A login entity object
	 * @throws LoginException
	 * @throws RegistryException
	 */
	void login(Object loginEntity) throws LoginException, RegistryException;

	boolean isLoggedIn();

	String getLoggedInUser();

	void logoff();

	public static class LoginEntity implements EntityType {
	}

	public static class UserField implements FieldType {
	}

	public static class PasswordField implements FieldType {
	}

	public static class ErrorField implements FieldType {
	}
}
