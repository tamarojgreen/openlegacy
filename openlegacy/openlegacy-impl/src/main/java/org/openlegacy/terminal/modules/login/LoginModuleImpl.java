package org.openlegacy.terminal.modules.login;

import org.openlegacy.HostAction;
import org.openlegacy.exceptions.RegistryException;
import org.openlegacy.modules.login.Login;
import org.openlegacy.modules.login.LoginException;
import org.openlegacy.terminal.definitions.FieldMappingDefinition;
import org.openlegacy.terminal.definitions.ScreenEntityDefinition;
import org.openlegacy.terminal.spi.ScreenEntitiesRegistry;
import org.openlegacy.terminal.support.TerminalSessionModuleAdapter;
import org.openlegacy.terminal.support.actions.SendKeyActions;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

public class LoginModuleImpl extends TerminalSessionModuleAdapter implements Login, InitializingBean {

	@Autowired
	private ScreenEntitiesRegistry screenEntitiesRegistry;
	private HostAction hostAction = SendKeyActions.ENTER;
	private boolean loggedIn = false;

	private FieldMappingDefinition userField;
	private FieldMappingDefinition passwordField;
	private FieldMappingDefinition errorField;
	private ScreenEntityDefinition loginScreenDefinition;;

	public void login(String user, String password) throws LoginException {
		// TODO
	}

	public void login(Object loginEntityInstance) throws LoginException {

		Class<?> registryLoginClass = loginScreenDefinition.getHostEntityClass();
		if (registryLoginClass != loginEntityInstance.getClass()) {
			throw (new RegistryException("Login entity instance " + loginEntityInstance.getClass()
					+ " doesn't match registry login screen" + registryLoginClass));
		}
		getTerminalSession().doAction(hostAction, loginEntityInstance);

		if (getTerminalSession().getEntity() instanceof LoginScreen) {
			Object value = loginScreenDefinition.getFieldValue(loginEntityInstance, errorField);
			throw (new LoginException(value.toString()));
		}
		loggedIn = true;
	}

	private void initMetadata() {

		loginScreenDefinition = screenEntitiesRegistry.findFirstEntityDefinitionByType(Login.LoginScreen.class);

		userField = loginScreenDefinition.findFirstFieldDefinitionByType(Login.UserField.class);
		passwordField = loginScreenDefinition.findFirstFieldDefinitionByType(Login.PasswordField.class);
		errorField = loginScreenDefinition.findFirstFieldDefinitionByType(Login.ErrorField.class);

		Class<?> hostEntityClass = loginScreenDefinition.getHostEntityClass();
		if (userField == null) {
			throw (new LoginException("User field not defined in " + hostEntityClass));
		}
		if (passwordField == null) {
			throw (new LoginException("Password field not defined in " + hostEntityClass));
		}
		if (errorField == null) {
			throw (new LoginException("Error field not defined in " + hostEntityClass));
		}
	}

	public boolean isLoggedIn() {
		return loggedIn;
	}

	public void logoff() {
		loggedIn = false;
		// do nothing
	}

	public void setHostAction(HostAction hostAction) {
		this.hostAction = hostAction;
	}

	public void afterPropertiesSet() throws Exception {
		initMetadata();
	}

}
