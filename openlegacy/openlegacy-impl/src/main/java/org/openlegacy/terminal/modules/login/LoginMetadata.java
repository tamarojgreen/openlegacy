package org.openlegacy.terminal.modules.login;

import org.openlegacy.exceptions.RegistryException;
import org.openlegacy.modules.login.Login;
import org.openlegacy.terminal.definitions.FieldMappingDefinition;
import org.openlegacy.terminal.definitions.ScreenEntityDefinition;
import org.openlegacy.terminal.spi.ScreenEntitiesRegistry;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

@Component
public class LoginMetadata {

	@Inject
	private ScreenEntitiesRegistry screenEntitiesRegistry;

	private FieldMappingDefinition userField;
	private FieldMappingDefinition passwordField;
	private FieldMappingDefinition errorField;
	private ScreenEntityDefinition loginScreenDefinition;;

	public void initCache() {

		if (loginScreenDefinition != null) {
			return;
		}

		loginScreenDefinition = screenEntitiesRegistry.getFirstEntityDefinition(Login.LoginScreen.class);

		userField = loginScreenDefinition.getFirstFieldDefinition(Login.UserField.class);
		passwordField = loginScreenDefinition.getFirstFieldDefinition(Login.PasswordField.class);
		errorField = loginScreenDefinition.getFirstFieldDefinition(Login.ErrorField.class);

		Class<?> hostEntityClass = loginScreenDefinition.getEntityClass();
		if (userField == null) {
			throw (new RegistryException("User field not defined in " + hostEntityClass));
		}
		if (passwordField == null) {
			throw (new RegistryException("Password field not defined in " + hostEntityClass));
		}
		if (errorField == null) {
			throw (new RegistryException("Error field not defined in " + hostEntityClass));
		}
	}

	public FieldMappingDefinition getUserField() {
		return userField;
	}

	public FieldMappingDefinition getPasswordField() {
		return passwordField;
	}

	public FieldMappingDefinition getErrorField() {
		return errorField;
	}

	public ScreenEntityDefinition getLoginScreenDefinition() {
		return loginScreenDefinition;
	}
}
