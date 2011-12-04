package org.openlegacy.terminal.modules.login;

import org.openlegacy.SessionModuleMetadata;
import org.openlegacy.exceptions.RegistryException;
import org.openlegacy.modules.login.Login;
import org.openlegacy.terminal.definitions.ScreenEntityDefinition;
import org.openlegacy.terminal.definitions.ScreenFieldDefinition;
import org.openlegacy.terminal.spi.ScreenEntitiesRegistry;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

@Component
public class LoginMetadata implements SessionModuleMetadata {

	@Inject
	private ScreenEntitiesRegistry screenEntitiesRegistry;

	private ScreenFieldDefinition userField;
	private ScreenFieldDefinition passwordField;
	private ScreenFieldDefinition errorField;
	private ScreenEntityDefinition loginScreenDefinition;;

	public void initCache() {

		if (loginScreenDefinition != null) {
			return;
		}

		loginScreenDefinition = screenEntitiesRegistry.getSingleEntityDefinition(Login.LoginEntity.class);

		userField = loginScreenDefinition.getFirstFieldDefinition(Login.UserField.class);
		passwordField = loginScreenDefinition.getFirstFieldDefinition(Login.PasswordField.class);
		errorField = loginScreenDefinition.getFirstFieldDefinition(Login.ErrorField.class);

		Class<?> entityClass = loginScreenDefinition.getEntityClass();
		if (userField == null) {
			throw (new RegistryException("User field not defined in " + entityClass));
		}
		if (passwordField == null) {
			throw (new RegistryException("Password field not defined in " + entityClass));
		}
		if (errorField == null) {
			throw (new RegistryException("Error field not defined in " + entityClass));
		}
	}

	public ScreenFieldDefinition getUserField() {
		return userField;
	}

	public ScreenFieldDefinition getPasswordField() {
		return passwordField;
	}

	public ScreenFieldDefinition getErrorField() {
		return errorField;
	}

	public ScreenEntityDefinition getLoginScreenDefinition() {
		return loginScreenDefinition;
	}
}
