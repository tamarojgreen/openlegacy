/*******************************************************************************
 * Copyright (c) 2014 OpenLegacy Inc.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     OpenLegacy Inc. - initial API and implementation
 *******************************************************************************/
package org.openlegacy.terminal.modules.login;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openlegacy.exceptions.OpenLegacyRuntimeException;
import org.openlegacy.exceptions.RegistryException;
import org.openlegacy.exceptions.SessionEndedException;
import org.openlegacy.modules.login.Login;
import org.openlegacy.modules.login.LoginException;
import org.openlegacy.modules.login.User;
import org.openlegacy.terminal.ScreenEntity;
import org.openlegacy.terminal.ScreenPojoFieldAccessor;
import org.openlegacy.terminal.actions.TerminalAction;
import org.openlegacy.terminal.actions.TerminalActions;
import org.openlegacy.terminal.definitions.NavigationDefinition;
import org.openlegacy.terminal.definitions.ScreenEntityDefinition;
import org.openlegacy.terminal.services.ScreenEntitiesRegistry;
import org.openlegacy.terminal.services.ScreensRecognizer;
import org.openlegacy.terminal.support.TerminalSessionModuleAdapter;
import org.openlegacy.terminal.support.wait_conditions.WaitForNonEmptyField;
import org.openlegacy.terminal.utils.SimpleScreenPojoFieldAccessor;
import org.openlegacy.terminal.wait_conditions.WaitConditionFactory;
import org.openlegacy.utils.ProxyUtil;
import org.openlegacy.utils.ReflectionUtil;
import org.openlegacy.utils.SpringUtil;
import org.springframework.context.ApplicationContext;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.text.MessageFormat;

import javax.inject.Inject;

public class DefaultTerminalLoginModule extends TerminalSessionModuleAdapter implements Login, Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private LoginMetadata loginMetadata;

	@Inject
	private ScreensRecognizer screensRecognizer;

	@Inject
	private transient ApplicationContext applicationContext;

	@Inject
	private WaitConditionFactory waitConditionFactory;

	private TerminalAction loginAction = TerminalActions.ENTER();

	private User loggedInUser = null;

	private TerminalAction defaultExitAction = TerminalActions.F3();

	private long loginTimeout = 0;

	// the maximum number of actions allowed in order to exit back to login screen
	private int maxActionsToLogin = 7;

	private boolean backToLoginOnDisconnect = true;

	private final static Log logger = LogFactory.getLog(DefaultTerminalLoginModule.class);

	private static final String LOGIN_FAILED = "Login failed";

	public void login(String user, String password) throws LoginException {
		if (loggedInUser != null) {
			return;
		}

		lazyMetadataInit();

		if (loginMetadata.getLoginScreenDefinition() == null) {
			throw (new RegistryException(
					"LoginModule entity doesn't contain a login screen definition. Verify one of your screen entity classes is defined as @ScreenEntity(screenType = Login.LoginEntity.class)"));
		}

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

		if (loginMetadata.getLoginScreenDefinition() == null) {
			throw (new RegistryException(
					"LoginModule entity doesn't contain a login screen definition. Verify one of your screen entity classes is defined as @ScreenEntity(screenType = Login.LoginEntity.class)"));
		}
		Class<?> registryLoginClass = loginMetadata.getLoginScreenDefinition().getEntityClass();
		if (!ProxyUtil.isClassesMatch(loginEntity.getClass(), registryLoginClass)) {
			throw (new RegistryException("LoginModule entity " + loginEntity.getClass() + " doesn't match registry login screen"
					+ registryLoginClass));
		}

		ScreenPojoFieldAccessor fieldAccessor = new SimpleScreenPojoFieldAccessor(loginEntity);
		String user = (String)fieldAccessor.getFieldValue(loginMetadata.getUserField().getName());

		loggedInUser = new PersistedUser(user);

		// construct a wait while login error message is NOT shown (or next screen whows up)
		String errorFieldName = loginMetadata.getErrorField().getName();
		WaitForNonEmptyField waitForNonEmptyLoginError = waitConditionFactory.create(WaitForNonEmptyField.class,
				registryLoginClass, errorFieldName);

		ScreenEntity firstScreenEntity = (ScreenEntity)getSession().getEntity(
				loginMetadata.getLoginScreenDefinition().getEntityClass());
		Assert.notNull(firstScreenEntity, "Login entity not recognized as the 1st application screen");

		Object currentEntity = getSession().doAction(loginAction, (ScreenEntity)loginEntity, waitForNonEmptyLoginError);

		Class<? extends Object> currentEntityClass = null;
		if (currentEntity != null) {
			currentEntityClass = currentEntity.getClass();
			fieldAccessor = new SimpleScreenPojoFieldAccessor(currentEntity);

			if (logger.isDebugEnabled()) {
				logger.debug(MessageFormat.format("After performing login action current entity is:{0}", currentEntityClass));
			}
		}

		if (currentEntityClass != null && ProxyUtil.isClassesMatch(currentEntityClass, registryLoginClass)) {
			try {
				Thread.sleep(loginTimeout);
			} catch (InterruptedException e) {
				throw (new OpenLegacyRuntimeException(e));
			}
		}
		// throw exception if after login screen is still login
		if (currentEntityClass != null && ProxyUtil.isClassesMatch(currentEntityClass, registryLoginClass)) {
			Object value = fieldAccessor.getFieldValue(errorFieldName);
			String message = value != null ? value.toString() : LOGIN_FAILED;
			fieldAccessor = new SimpleScreenPojoFieldAccessor(loginEntity);
			fieldAccessor.setFieldValue(errorFieldName, message);
			loggedInUser = null;
			throw (new LoginException(message));
		} else {
			getSession().getProperties().getProperties().put(Login.USER_NAME_PROPERTY, loggedInUser.getUserName());
		}

	}

	private void lazyMetadataInit() {
		loginMetadata.initCache();
	}

	public boolean isLoggedIn() {
		return loggedInUser != null;
	}

	public void logoff() {

		logoffOnly();

		getSession().disconnect();
	}

	private void logoffOnly() {
		if (loggedInUser == null) {
			return;
		}

		lazyMetadataInit();
		if (loginMetadata.getLoginScreenDefinition() == null) {
			return;
		}
		Class<?> loginClass = loginMetadata.getLoginScreenDefinition().getEntityClass();

		if (getSession().isConnected()) {
			try {
				if (backToLoginOnDisconnect) {
					backToLogin(loginClass);
				}
			} catch (Exception e) {
				// ok
				logger.warn("Failed to return to login screen - " + e.getMessage(), e);
			}
		}
		loggedInUser = null;
	}

	private void backToLogin(Class<?> loginClass) {
		Class<?> currentEntityClass = screensRecognizer.match(getSession().getSnapshot());

		int exitActionsCount = 0;
		// while current entity is not login screen and haven't reach a maximum exit actions
		while (!ProxyUtil.isClassesMatch(currentEntityClass, loginClass, true) && exitActionsCount < maxActionsToLogin) {
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
			ScreenEntitiesRegistry screenEntitiesRegistry = SpringUtil.getBean(applicationContext, ScreenEntitiesRegistry.class);
			ScreenEntityDefinition currentScreenDefinition = screenEntitiesRegistry.get(currentEntityClass);
			NavigationDefinition navigationDefinition = currentScreenDefinition.getNavigationDefinition();
			if (navigationDefinition != null && navigationDefinition.getExitAction() != null) {
				exitAction = navigationDefinition.getExitAction();
			}
		}
		return exitAction;
	}

	public void setLoginActionClass(Class<? extends TerminalAction> terminalAction) {
		this.loginAction = ReflectionUtil.newInstance(terminalAction);
	}

	public void setDefaultExitActionClass(Class<? extends TerminalAction> defaultExitAction) {
		this.defaultExitAction = ReflectionUtil.newInstance(defaultExitAction);
	}

	public void setMaxActionsToLogin(int maxActionsToLogin) {
		this.maxActionsToLogin = maxActionsToLogin;
	}

	public void setLoginTimeout(long loginTimeout) {
		this.loginTimeout = loginTimeout;
	}

	public User getLoggedInUser() {
		return loggedInUser;
	}

	public void setBackToLoginOnDisconnect(boolean backToLoginOnDisconnect) {
		this.backToLoginOnDisconnect = backToLoginOnDisconnect;
	}

	@Override
	public void destroy() {
		try {
			logoffOnly();
		} catch (Exception e) {
			logger.warn("Disconnected with error", e);
		}
	}
}
