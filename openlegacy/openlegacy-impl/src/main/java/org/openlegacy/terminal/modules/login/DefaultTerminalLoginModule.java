package org.openlegacy.terminal.modules.login;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openlegacy.exceptions.RegistryException;
import org.openlegacy.exceptions.SessionEndedException;
import org.openlegacy.modules.login.Login;
import org.openlegacy.modules.login.LoginException;
import org.openlegacy.terminal.ScreenEntity;
import org.openlegacy.terminal.ScreenPojoFieldAccessor;
import org.openlegacy.terminal.actions.TerminalAction;
import org.openlegacy.terminal.actions.TerminalActions;
import org.openlegacy.terminal.definitions.NavigationDefinition;
import org.openlegacy.terminal.definitions.ScreenEntityDefinition;
import org.openlegacy.terminal.spi.ScreenEntitiesRegistry;
import org.openlegacy.terminal.spi.ScreensRecognizer;
import org.openlegacy.terminal.support.TerminalSessionModuleAdapter;
import org.openlegacy.terminal.utils.SimpleScreenPojoFieldAccessor;
import org.openlegacy.utils.ProxyUtil;
import org.openlegacy.utils.ReflectionUtil;

import java.text.MessageFormat;

import javax.inject.Inject;

public class DefaultTerminalLoginModule extends TerminalSessionModuleAdapter implements Login {

	private static final long serialVersionUID = 1L;

	@Inject
	private LoginMetadata loginMetadata;

	@Inject
	private ScreensRecognizer screensRecognizer;

	@Inject
	private ScreenEntitiesRegistry screenEntitiesRegistry;

	private TerminalAction loginAction = TerminalActions.ENTER();

	private String loggedInUser = null;

	private TerminalAction defaultExitAction = TerminalActions.F3();

	// the maximum number of actions allowed in order to exit back to login screen
	private int maxActionsToLogin = 7;

	private final static Log logger = LogFactory.getLog(DefaultTerminalLoginModule.class);

	private static final String LOGIN_FAILED = "Login failed";

	public void login(String user, String password) throws LoginException {
		if (loggedInUser != null) {
			return;
		}

		lazyMetadataInit();

		try {
			ScreenEntity loginEntity = (ScreenEntity)loginMetadata.getLoginScreenDefinition().getEntityClass().newInstance();
			ScreenPojoFieldAccessor fieldAccessor = new SimpleScreenPojoFieldAccessor(loginEntity);
			fieldAccessor.setFieldValue(loginMetadata.getUserField().getName(), user);
			fieldAccessor.setFieldValue(loginMetadata.getPasswordField().getName(), password);
			login(loginEntity);
		} catch (LoginException e) {
			throw (e);
		} catch (Exception e) {
			throw (new IllegalStateException(e));
		}

	}

	public void login(Object loginEntity) throws LoginException, RegistryException {
		if (loggedInUser != null) {
			throw (new LoginException("User is already logged in"));
		}

		lazyMetadataInit();

		Class<?> registryLoginClass = loginMetadata.getLoginScreenDefinition().getEntityClass();
		if (!ProxyUtil.isClassesMatch(loginEntity.getClass(), registryLoginClass)) {
			throw (new RegistryException("LoginModule entity " + loginEntity.getClass() + " doesn't match registry login screen"
					+ registryLoginClass));
		}

		ScreenPojoFieldAccessor fieldAccessor = new SimpleScreenPojoFieldAccessor(loginEntity);
		String user = (String)fieldAccessor.getFieldValue(loginMetadata.getUserField().getName());

		Object currentEntity = getSession().doAction(loginAction, (ScreenEntity)loginEntity);

		Class<? extends Object> currentEntityClass = currentEntity.getClass();

		if (logger.isDebugEnabled()) {
			logger.debug(MessageFormat.format("After performing login action current entity is:{0}", currentEntityClass));
		}

		fieldAccessor = new SimpleScreenPojoFieldAccessor(currentEntity);

		// throw exception if after login screen is still login
		if (ProxyUtil.isClassesMatch(currentEntityClass, registryLoginClass)) {
			Object value = fieldAccessor.getFieldValue(loginMetadata.getErrorField().getName());
			String message = value != null ? value.toString() : LOGIN_FAILED;
			throw (new LoginException(message));
		} else {
			loggedInUser = user;
		}

	}

	private void lazyMetadataInit() {
		loginMetadata.initCache();
	}

	public boolean isLoggedIn() {
		return loggedInUser != null;
	}

	public void logoff() {

		lazyMetadataInit();

		logoffOnly();

		getSession().disconnect();
	}

	private void logoffOnly() {
		Class<?> loginClass = loginMetadata.getLoginScreenDefinition().getEntityClass();

		if (getSession().isConnected()) {
			backToLogin(loginClass);
		}
		loggedInUser = null;
	}

	private void backToLogin(Class<?> loginClass) {
		Class<?> currentEntityClass = screensRecognizer.match(getSession().getSnapshot());

		int exitActionsCount = 0;
		// while current entity is not login screen and haven't reach a maximum exit actions
		while (!ProxyUtil.isClassesMatch(currentEntityClass, loginClass) && exitActionsCount < maxActionsToLogin) {
			TerminalAction exitAction = defaultExitAction;
			exitActionsCount++;

			exitAction = findCurrentEntityExitAction(currentEntityClass, exitAction);

			if (logger.isDebugEnabled()) {
				logger.debug(MessageFormat.format("Exiting screen {0} using {1}", currentEntityClass, exitAction));
			}
			try {
				getSession().doAction(exitAction);
			} catch (SessionEndedException e) {
				break;
			}
			currentEntityClass = screensRecognizer.match(getSession().getSnapshot());
		}
	}

	private TerminalAction findCurrentEntityExitAction(Class<? extends Object> currentEntityClass, TerminalAction exitAction) {
		if (currentEntityClass != null) {
			ScreenEntityDefinition currentScreenDefinition = screenEntitiesRegistry.get(currentEntityClass);
			NavigationDefinition navigationDefinition = currentScreenDefinition.getNavigationDefinition();
			if (navigationDefinition != null && navigationDefinition.getExitAction() != null) {
				exitAction = navigationDefinition.getExitAction();
			}
		}
		return exitAction;
	}

	public void setLoginAction(Class<? extends TerminalAction> terminalAction) {
		this.loginAction = ReflectionUtil.newInstance(terminalAction);
	}

	public void setDefaultExitAction(Class<? extends TerminalAction> defaultExitAction) {
		this.defaultExitAction = ReflectionUtil.newInstance(defaultExitAction);
	}

	public void setMaxActionsToLogin(int maxActionsToLogin) {
		this.maxActionsToLogin = maxActionsToLogin;
	}

	public String getLoggedInUser() {
		return loggedInUser;
	}

	@Override
	public void destroy() {
		logoffOnly();
	}
}
