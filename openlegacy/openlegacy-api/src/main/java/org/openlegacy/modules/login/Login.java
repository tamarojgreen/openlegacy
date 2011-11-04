package org.openlegacy.modules.login;

import org.openlegacy.FieldType;
import org.openlegacy.exceptions.RegistryException;
import org.openlegacy.modules.HostSessionModule;
import org.openlegacy.terminal.ScreenEntityType;

public interface Login extends HostSessionModule {

	void login(String user, String password) throws LoginException, RegistryException;

	void login(Object loginScreen) throws LoginException, RegistryException;

	boolean isLoggedIn();

	void logoff();

	public static class LoginScreen implements ScreenEntityType {
	}

	public static class UserField implements FieldType {
	}

	public static class PasswordField implements FieldType {
	}

	public static class ErrorField implements FieldType {
	}
}
