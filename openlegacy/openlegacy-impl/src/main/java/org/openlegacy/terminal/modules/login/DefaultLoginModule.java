package org.openlegacy.terminal.modules.login;

import org.openlegacy.HostAction;
import org.openlegacy.exceptions.RegistryException;
import org.openlegacy.modules.login.LoginException;
import org.openlegacy.modules.login.Login;
import org.openlegacy.terminal.actions.SendKeyActions;
import org.openlegacy.terminal.support.TerminalSessionModuleAdapter;
import org.openlegacy.terminal.utils.ScreenEntityDirectFieldAccessor;
import org.openlegacy.utils.ProxyUtil;
import org.springframework.beans.factory.annotation.Autowired;

public class DefaultLoginModule extends TerminalSessionModuleAdapter implements Login {

	@Autowired
	private LoginCache loginCache;

	private HostAction hostAction = SendKeyActions.ENTER;
	private boolean loggedIn = false;

	public void login(String user, String password) throws LoginException {
		lazyMetadataInit();

		try {
			Object loginEntity = loginCache.getLoginScreenDefinition().getEntityClass().newInstance();
			ScreenEntityDirectFieldAccessor fieldAccessor = new ScreenEntityDirectFieldAccessor(loginEntity);
			fieldAccessor.setFieldValue(loginCache.getUserField().getName(), user);
			fieldAccessor.setFieldValue(loginCache.getPasswordField().getName(), password);
			login(loginEntity);
		} catch (Exception e) {
			throw (new IllegalStateException(e));
		}

	}

	public void login(Object loginEntity) throws LoginException, RegistryException {
		lazyMetadataInit();

		Class<?> registryLoginClass = loginCache.getLoginScreenDefinition().getEntityClass();
		if (!ProxyUtil.isClassesMatch(loginEntity.getClass(), registryLoginClass)) {
			throw (new RegistryException("LoginModule entity " + loginEntity.getClass() + " doesn't match registry login screen"
					+ registryLoginClass));
		}
		getTerminalSession().doAction(hostAction, loginEntity);

		ScreenEntityDirectFieldAccessor fieldAccessor = new ScreenEntityDirectFieldAccessor(loginEntity);
		if (getTerminalSession().getEntity() instanceof LoginScreen) {
			Object value = fieldAccessor.getFieldValue(loginCache.getErrorField().getName());
			throw (new LoginException(value.toString()));
		}
		loggedIn = true;
	}

	private void lazyMetadataInit() {
		loginCache.initCache();
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
