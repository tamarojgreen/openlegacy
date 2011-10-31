package org.openlegacy.terminal.modules.login;

import org.openlegacy.HostAction;
import org.openlegacy.exceptions.RegistryException;
import org.openlegacy.modules.login.LoginException;
import org.openlegacy.modules.login.LoginModule;
import org.openlegacy.terminal.definitions.FieldMappingDefinition;
import org.openlegacy.terminal.definitions.ScreenEntityDefinition;
import org.openlegacy.terminal.spi.ScreenEntitiesRegistry;
import org.openlegacy.terminal.support.TerminalSessionModuleAdapter;
import org.openlegacy.terminal.support.actions.SendKeyActions;
import org.openlegacy.terminal.utils.ScreenEntityDirectFieldAccessor;
import org.openlegacy.utils.ProxyUtil;
import org.springframework.beans.factory.annotation.Autowired;

public class LoginModuleImpl extends TerminalSessionModuleAdapter implements LoginModule {

	@Autowired
	private ScreenEntitiesRegistry screenEntitiesRegistry;
	private HostAction hostAction = SendKeyActions.ENTER;
	private boolean loggedIn = false;

	private FieldMappingDefinition userField;
	private FieldMappingDefinition passwordField;
	private FieldMappingDefinition errorField;
	private ScreenEntityDefinition loginScreenDefinition;;

	public void login(String user, String password) throws LoginException {
		lazyMetadataInit();

		try {
			Object loginEntity = loginScreenDefinition.getEntityClass().newInstance();
			ScreenEntityDirectFieldAccessor fieldAccessor = new ScreenEntityDirectFieldAccessor(loginEntity);
			fieldAccessor.setFieldValue(userField.getName(), user);
			fieldAccessor.setFieldValue(passwordField.getName(), password);
			login(loginEntity);
		} catch (Exception e) {
			throw (new IllegalStateException(e));
		}

	}

	public void login(Object loginEntity) throws LoginException, RegistryException {
		lazyMetadataInit();

		Class<?> registryLoginClass = loginScreenDefinition.getEntityClass();
		if (!ProxyUtil.isClassesMatch(loginEntity.getClass(), registryLoginClass)) {
			throw (new RegistryException("LoginModule entity " + loginEntity.getClass() + " doesn't match registry login screen"
					+ registryLoginClass));
		}
		getTerminalSession().doAction(hostAction, loginEntity);

		ScreenEntityDirectFieldAccessor fieldAccessor = new ScreenEntityDirectFieldAccessor(loginEntity);
		if (getTerminalSession().getEntity() instanceof LoginScreen) {
			Object value = fieldAccessor.getFieldValue(errorField.getName());
			throw (new LoginException(value.toString()));
		}
		loggedIn = true;
	}

	private void lazyMetadataInit() {

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

}
