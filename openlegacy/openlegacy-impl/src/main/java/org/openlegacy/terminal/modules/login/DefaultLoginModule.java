package org.openlegacy.terminal.modules.login;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openlegacy.HostAction;
import org.openlegacy.exceptions.RegistryException;
import org.openlegacy.modules.login.Login;
import org.openlegacy.modules.login.LoginException;
import org.openlegacy.terminal.actions.SendKeyActions;
import org.openlegacy.terminal.definitions.NavigationDefinition;
import org.openlegacy.terminal.definitions.ScreenEntityDefinition;
import org.openlegacy.terminal.spi.ScreenEntitiesRegistry;
import org.openlegacy.terminal.spi.ScreensRecognizer;
import org.openlegacy.terminal.support.TerminalSessionModuleAdapter;
import org.openlegacy.terminal.utils.ScreenEntityDirectFieldAccessor;
import org.openlegacy.utils.ProxyUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.MessageFormat;

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

	private final static Log logger = LogFactory.getLog(DefaultLoginModule.class);

	public void login(String user, String password) throws LoginException {
		if (loggedIn) {
			return;
		}

		lazyMetadataInit();

		try {
			Object loginEntity = loginCache.getLoginScreenDefinition().getEntityClass().newInstance();
			ScreenEntityDirectFieldAccessor fieldAccessor = new ScreenEntityDirectFieldAccessor(loginEntity);
			fieldAccessor.setFieldValue(loginCache.getUserField().getName(), user);
			fieldAccessor.setFieldValue(loginCache.getPasswordField().getName(), password);
			login(loginEntity);
		} catch (LoginException e) {
			throw (e);
		} catch (Exception e) {
			throw (new IllegalStateException(e));
		}

	}

	public void login(Object loginEntity) throws LoginException, RegistryException {
		if (loggedIn) {
			return;
		}

		lazyMetadataInit();

		Class<?> registryLoginClass = loginCache.getLoginScreenDefinition().getEntityClass();
		if (!ProxyUtil.isClassesMatch(loginEntity.getClass(), registryLoginClass)) {
			throw (new RegistryException("LoginModule entity " + loginEntity.getClass() + " doesn't match registry login screen"
					+ registryLoginClass));
		}
		getTerminalSession().doAction(hostAction, loginEntity);

		ScreenEntityDirectFieldAccessor fieldAccessor = new ScreenEntityDirectFieldAccessor(loginEntity);
		Object currentEntity = getTerminalSession().getEntity(false);

		Class<? extends Object> currentEntityClass = currentEntity.getClass();

		if (logger.isDebugEnabled()) {
			logger.debug(MessageFormat.format("After performing login action current entity is:{0}", currentEntityClass));
		}

		// throw exception if after login screen is still login
		if (ProxyUtil.isClassesMatch(currentEntityClass, registryLoginClass)) {
			fieldAccessor = new ScreenEntityDirectFieldAccessor(currentEntity);
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

		Class<?> currentEntityClass = screensRecognizer.match(getTerminalSession().getSnapshot());

		int exitActionsCount = 0;
		// while current entity is not login screen and haven't reach a maximum exit actions
		while (!ProxyUtil.isClassesMatch(currentEntityClass, loginClass) && exitActionsCount < maxActionsToLogin) {
			HostAction exitAction = defaultExitAction;
			exitActionsCount++;

			exitAction = findCurrentEntityExitAction(currentEntityClass, exitAction);

			if (logger.isDebugEnabled()) {
				logger.debug(MessageFormat.format("Exiting screen {0} using {1}", currentEntityClass, exitAction));
			}
			getTerminalSession().doAction(exitAction, null);
			currentEntityClass = screensRecognizer.match(getTerminalSession().getSnapshot());

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
