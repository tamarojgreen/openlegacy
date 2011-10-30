package org.openlegacy.modules.login;

import org.openlegacy.FieldType;
import org.openlegacy.HostEntityType;
import org.openlegacy.modules.HostSessionModule;

public interface Login extends HostSessionModule {

	void login(String user, String password) throws LoginException;

	void login(Object loginScreen) throws LoginException;

	boolean isLoggedIn();

	void logoff();

	public static class LoginScreen implements HostEntityType {
	}

	public static class UserField implements FieldType {
	}

	public static class PasswordField implements FieldType {
	}

	public static class ErrorField implements FieldType {
	}
}
