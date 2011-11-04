package org.openlegacy.terminal.modules.login;

import org.openlegacy.HostAction;
import org.openlegacy.exceptions.RegistryException;
import org.openlegacy.modules.login.Login;
import org.openlegacy.modules.login.LoginException;
import org.openlegacy.terminal.TerminalScreen;
import org.openlegacy.terminal.actions.SendKeyActions;
import org.openlegacy.terminal.definitions.NavigationDefinition;
import org.openlegacy.terminal.definitions.ScreenEntityDefinition;
import org.openlegacy.terminal.spi.ScreenEntitiesRegistry;
import org.openlegacy.terminal.spi.ScreensRecognizer;
import org.openlegacy.terminal.support.TerminalSessionModuleAdapter;
import org.openlegacy.terminal.utils.ScreenEntityDirectFieldAccessor;
import org.openlegacy.utils.ProxyUtil;
import org.springframework.beans.factory.annotation.Autowired;

public class DefaultLoginModule extends TerminalSessionModuleAdapter implements Login {

	@Autowired
	private LoginCache loginCache;

	@Autowired
	private ScreensRecognizer screensRecognizer;

	@Autowired
	private ScreenEntitiesRegistry screenEntitiesRegistry;

	private HostAction hostAction = SendKeyActions.ENTER;
	private boolean loggedIn = false;

	private HostAction defaultExitAction;

	// the maximum number of actions allowed in order to exit back to login screen
	private int maxActionsToLogin = 7;

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

		Class<?> loginClass = loginCache.getLoginScreenDefinition().getEntityClass();

		TerminalScreen snapshot = getTerminalSession().getSnapshot();
		Class<?> currentEntityClass = screensRecognizer.match(snapshot);

		int exitActionsCount = 0;
		// while current entity is not login screen and haven't reach a maximum exit actions
		while (!ProxyUtil.isClassesMatch(currentEntityClass, loginClass) && exitActionsCount < maxActionsToLogin) {
			HostAction exitAction = defaultExitAction;
			exitActionsCount++;

			exitAction = findCurrentEntityExitAction(currentEntityClass, exitAction);

			getTerminalSession().doAction(exitAction, null);
			snapshot = getTerminalSession().getSnapshot();
			currentEntityClass = screensRecognizer.match(snapshot);

		}

		getTerminalSession().disconnect();

		loggedIn = false;
	}

	private HostAction findCurrentEntityExitAction(Class<? extends Object> currentEntityClass, HostAction exitAction) {
		if (currentEntityClass != null) {
			ScreenEntityDefinition currentScreenDefinition = screenEntitiesRegistry.get(currentEntityClass);
			NavigationDefinition navigationDefinition = currentScreenDefinition.getNavigationDefinition();
			if (navigationDefinition != null && navigationDefinition.getExitAction() != null) {
				exitAction = navigationDefinition.getExitAction();
			}
		}
		return exitAction;
	}

	public void setHostAction(HostAction hostAction) {
		this.hostAction = hostAction;
	}

	public void setDefaultExitAction(HostAction defaultExitAction) {
		this.defaultExitAction = defaultExitAction;
	}

	public void setMaxActionsToLogin(int maxActionsToLogin) {
		this.maxActionsToLogin = maxActionsToLogin;
	}
}
