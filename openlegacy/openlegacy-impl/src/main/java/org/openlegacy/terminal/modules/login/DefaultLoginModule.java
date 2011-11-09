package org.openlegacy.terminal.modules.login;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openlegacy.HostAction;
import org.openlegacy.exceptions.RegistryException;
import org.openlegacy.modules.login.Login;
import org.openlegacy.modules.login.LoginException;
import org.openlegacy.terminal.ScreenEntity;
import org.openlegacy.terminal.ScreenEntityFieldAccessor;
import org.openlegacy.terminal.actions.SendKeyActions;
import org.openlegacy.terminal.definitions.NavigationDefinition;
import org.openlegacy.terminal.definitions.ScreenEntityDefinition;
import org.openlegacy.terminal.spi.ScreenEntitiesRegistry;
import org.openlegacy.terminal.spi.ScreensRecognizer;
import org.openlegacy.terminal.support.TerminalSessionModuleAdapter;
import org.openlegacy.terminal.utils.SimpleScreenEntityFieldAccessor;
import org.openlegacy.utils.ProxyUtil;
import org.openlegacy.utils.ReflectionUtil;

import java.text.MessageFormat;

import javax.inject.Inject;

public class DefaultLoginModule extends TerminalSessionModuleAdapter implements Login {

	@Inject
	private LoginMetadata loginCache;

	@Inject
	private ScreensRecognizer screensRecognizer;

	@Inject
	private ScreenEntitiesRegistry screenEntitiesRegistry;

	private HostAction loginHostAction = SendKeyActions.ENTER;

	private String loggedInUser = null;

	private HostAction defaultExitAction = SendKeyActions.F3;

	// the maximum number of actions allowed in order to exit back to login screen
	private int maxActionsToLogin = 7;

	private final static Log logger = LogFactory.getLog(DefaultLoginModule.class);

	public void login(String user, String password) throws LoginException {
		if (loggedInUser != null) {
			return;
		}

		lazyMetadataInit();

		try {
			ScreenEntity loginEntity = (ScreenEntity)loginCache.getLoginScreenDefinition().getEntityClass().newInstance();
			ScreenEntityFieldAccessor fieldAccessor = new SimpleScreenEntityFieldAccessor(loginEntity);
			fieldAccessor.setFieldValue(loginCache.getUserField().getName(), user);
			fieldAccessor.setFieldValue(loginCache.getPasswordField().getName(), password);
			login(loginEntity);
		} catch (LoginException e) {
			throw (e);
		} catch (Exception e) {
			throw (new IllegalStateException(e));
		}

	}

	public void login(ScreenEntity loginEntity) throws LoginException, RegistryException {
		if (loggedInUser != null) {
			throw (new LoginException("User is already logged in"));
		}

		lazyMetadataInit();

		Class<?> registryLoginClass = loginCache.getLoginScreenDefinition().getEntityClass();
		if (!ProxyUtil.isClassesMatch(loginEntity.getClass(), registryLoginClass)) {
			throw (new RegistryException("LoginModule entity " + loginEntity.getClass() + " doesn't match registry login screen"
					+ registryLoginClass));
		}

		ScreenEntityFieldAccessor fieldAccessor = new SimpleScreenEntityFieldAccessor(loginEntity);
		String user = (String)fieldAccessor.getFieldValue(loginCache.getUserField().getName());

		Object currentEntity = getTerminalSession().doAction(loginHostAction, loginEntity);

		Class<? extends Object> currentEntityClass = currentEntity.getClass();

		if (logger.isDebugEnabled()) {
			logger.debug(MessageFormat.format("After performing login action current entity is:{0}", currentEntityClass));
		}

		fieldAccessor = new SimpleScreenEntityFieldAccessor(currentEntity);

		// throw exception if after login screen is still login
		if (ProxyUtil.isClassesMatch(currentEntityClass, registryLoginClass)) {
			Object value = fieldAccessor.getFieldValue(loginCache.getErrorField().getName());
			throw (new LoginException(value.toString()));
		} else {
			loggedInUser = user;
		}

	}

	private void lazyMetadataInit() {
		loginCache.initCache();
	}

	public boolean isLoggedIn() {
		return loggedInUser != null;
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

		loggedInUser = null;
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

	public void setLoginHostAction(Class<? extends HostAction> hostAction) {
		this.loginHostAction = ReflectionUtil.newInstance(hostAction);
	}

	public void setDefaultExitAction(Class<? extends HostAction> defaultExitAction) {
		this.defaultExitAction = ReflectionUtil.newInstance(defaultExitAction);
	}

	public void setMaxActionsToLogin(int maxActionsToLogin) {
		this.maxActionsToLogin = maxActionsToLogin;
	}

	public String getLoggedInUser() {
		return loggedInUser;
	}
}
