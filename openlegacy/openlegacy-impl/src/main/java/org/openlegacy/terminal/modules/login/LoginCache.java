package org.openlegacy.terminal.modules.login;

import org.openlegacy.exceptions.RegistryException;
import org.openlegacy.modules.login.LoginModule;
import org.openlegacy.terminal.definitions.FieldMappingDefinition;
import org.openlegacy.terminal.definitions.ScreenEntityDefinition;
import org.openlegacy.terminal.spi.ScreenEntitiesRegistry;
import org.springframework.beans.factory.annotation.Autowired;

public class LoginCache {

	@Autowired
	private ScreenEntitiesRegistry screenEntitiesRegistry;

	private FieldMappingDefinition userField;
	private FieldMappingDefinition passwordField;
	private FieldMappingDefinition errorField;
	private ScreenEntityDefinition loginScreenDefinition;;

	public void initCache() {

		loginScreenDefinition = screenEntitiesRegistry.getFirstEntityDefinition(LoginModule.LoginScreen.class);

		userField = loginScreenDefinition.getFirstFieldDefinition(LoginModule.UserField.class);
		passwordField = loginScreenDefinition.getFirstFieldDefinition(LoginModule.PasswordField.class);
		errorField = loginScreenDefinition.getFirstFieldDefinition(LoginModule.ErrorField.class);

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
