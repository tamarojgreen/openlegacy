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
package org.openlegacy.terminal.support.navigation;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openlegacy.modules.login.Login;
import org.openlegacy.modules.login.Login.LoginEntity;
import org.openlegacy.modules.login.User;
import org.openlegacy.modules.navigation.Navigation;
import org.openlegacy.modules.table.Table;
import org.openlegacy.modules.table.drilldown.DrilldownAction;
import org.openlegacy.terminal.ScreenEntity;
import org.openlegacy.terminal.ScreenEntity.NONE;
import org.openlegacy.terminal.ScreenPojoFieldAccessor;
import org.openlegacy.terminal.TerminalSession;
import org.openlegacy.terminal.actions.TerminalAction;
import org.openlegacy.terminal.actions.TerminalMappedAction;
import org.openlegacy.terminal.definitions.FieldAssignDefinition;
import org.openlegacy.terminal.definitions.NavigationDefinition;
import org.openlegacy.terminal.definitions.ScreenEntityDefinition;
import org.openlegacy.terminal.exceptions.ScreenEntityNotAccessibleException;
import org.openlegacy.terminal.services.ScreenEntitiesRegistry;
import org.openlegacy.terminal.services.SessionNavigator;
import org.openlegacy.terminal.table.TerminalDrilldownAction;
import org.openlegacy.terminal.utils.ScreenNavigationUtil;
import org.openlegacy.terminal.utils.SimpleScreenPojoFieldAccessor;
import org.openlegacy.utils.EntityUtils;
import org.openlegacy.utils.ProxyUtil;
import org.openlegacy.utils.SpringUtil;
import org.springframework.context.ApplicationContext;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.inject.Inject;

public class DefaultSessionNavigator implements SessionNavigator, Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private NavigationMetadata navigationMetadata;

	@Inject
	private transient ApplicationContext applicationContext;

	@Inject
	private EntityUtils entityUtils;

	@Inject
	private ScreenEntitiesRegistry screenEntitiesRegistry;

	private final static Log logger = LogFactory.getLog(DefaultSessionNavigator.class);

	public void navigate(TerminalSession terminalSession, Class<?> targetScreenEntityClass, Object... keys)
			throws ScreenEntityNotAccessibleException {

		ScreenEntity currentEntity = terminalSession.getEntity();

		if (currentEntity == null) {
			return;
		}

		if (entityUtils.isEntitiesEquals(screenEntitiesRegistry, currentEntity, targetScreenEntityClass, keys)) {
			return;
		}

		ScreenEntitiesRegistry screenEntitiesRegistry = SpringUtil.getBean(applicationContext, ScreenEntitiesRegistry.class);

		Class<?> currentEntityClass = currentEntity.getClass();
		ScreenEntityDefinition currentEntityDefinition = screenEntitiesRegistry.get(currentEntityClass);
		ScreenEntityDefinition targetEntityDefinition = screenEntitiesRegistry.get(targetScreenEntityClass);

		List<NavigationDefinition> navigationSteps = navigationMetadata.get(currentEntityDefinition, targetEntityDefinition);

		while (navigationSteps == null) {
			if (entityUtils.isEntitiesEquals(screenEntitiesRegistry, currentEntity, targetScreenEntityClass, keys)) {
				return;
			}

			NavigationDefinition navigationDefinition = targetEntityDefinition.getNavigationDefinition();
			// invoke custom navigation (NONE screen)
			if (navigationDefinition != null && navigationDefinition.getAccessedFrom() == NONE.class) {
				navigationSteps = new ArrayList<NavigationDefinition>();
				navigationSteps.add(navigationDefinition);
			} else {
				navigationSteps = findDirectNavigationPath(currentEntityDefinition, targetEntityDefinition,
						screenEntitiesRegistry);
			}

			if (navigationSteps == null) {
				NavigationDefinition currentScreenNavigationDefinition = currentEntityDefinition.getNavigationDefinition();
				TerminalAction defaultExitAction = terminalSession.getModule(Navigation.class).getDefaultExitAction();

				TerminalAction exitAction;
				if (currentScreenNavigationDefinition != null) {
					exitAction = currentScreenNavigationDefinition.getExitAction();
				} else {
					exitAction = defaultExitAction;
					if (logger.isDebugEnabled()) {
						logger.debug(MessageFormat.format("Using default exit action {0} to step back of screen {1}", exitAction,
								currentEntityClass));
					}
				}

				Class<?> beforeEntityClass = currentEntityClass;

				exitCurrentScreen(terminalSession, currentEntityClass, exitAction);

				currentEntity = terminalSession.getEntity();
				currentEntityClass = currentEntity.getClass();
				currentEntityDefinition = screenEntitiesRegistry.get(currentEntityClass);

				if (ProxyUtil.isClassesMatch(beforeEntityClass, currentEntityClass)) {
					logger.error(MessageFormat.format(
							"Exiting from screen {0} using {1} was not effective. Existing navigation attempt to {2}",
							currentEntityClass, exitAction, targetScreenEntityClass));

					ScreenNavigationUtil.validateCurrentScreen(targetScreenEntityClass, currentEntityClass);
					break;
				}
				if (currentEntityDefinition.getType() == LoginEntity.class) {
					logger.error(MessageFormat.format("Reached login screen during attempt to navigate to {0}",
							targetScreenEntityClass));

					ScreenNavigationUtil.validateCurrentScreen(targetScreenEntityClass, currentEntityClass);
					break;

				}
			}

			if (navigationSteps != null) {
				Collections.reverse(navigationSteps);
				navigationMetadata.add(currentEntityDefinition, targetEntityDefinition, navigationSteps);
			}
		}
		if (navigationSteps != null) {
			performDirectNavigation(terminalSession, currentEntityClass, navigationSteps, keys);
			ScreenEntity entity = terminalSession.getEntity();
			if (entity != null) {
				ScreenNavigationUtil.validateCurrentScreen(targetScreenEntityClass, entity.getClass());
			}
		}

	}

	private static void exitCurrentScreen(TerminalSession terminalSession, Class<?> currentEntityClass, TerminalAction exitAction) {
		if (logger.isDebugEnabled()) {
			logger.debug(MessageFormat.format("Steping back of screen {0} using {1}", currentEntityClass, exitAction));
		}
		terminalSession.doAction(exitAction);
	}

	private static void performDirectNavigation(TerminalSession terminalSession, Class<?> currentEntityClass,
			List<NavigationDefinition> navigationSteps, Object... keys) {
		ScreenEntity currentEntity = terminalSession.getEntity();
		User currentUser = terminalSession.getModule(Login.class).getLoggedInUser();

		for (NavigationDefinition navigationDefinition : navigationSteps) {
			ScreenPojoFieldAccessor fieldAccessor = new SimpleScreenPojoFieldAccessor(currentEntity);
			List<FieldAssignDefinition> assignedFields = navigationDefinition.getAssignedFields();
			if (logger.isDebugEnabled()) {
				currentEntityClass = ProxyUtil.getOriginalClass(currentEntity.getClass());
				logger.debug("Performing navigation actions from screen " + currentEntityClass);
			}
			Collections.sort(assignedFields, new Comparator<FieldAssignDefinition>() {

				public int compare(FieldAssignDefinition o1, FieldAssignDefinition o2) {
					// make sure fields with roles are set last
					return o1.getRole() == null ? -1 : 1;
				}
			});

			for (FieldAssignDefinition fieldAssignDefinition : assignedFields) {
				String value = fieldAssignDefinition.getValue();

				if (value != null) {
					assignField(currentUser, fieldAccessor, fieldAssignDefinition, value);
				}
			}
			if (assignedFields.size() == 0) {
				currentEntity = null;
			}
			TerminalAction terminalAction = navigationDefinition.getTerminalAction();
			if (terminalAction instanceof DrilldownAction) {
				terminalSession.getModule(Table.class).drillDown(navigationDefinition.getTargetEntity(),
						(TerminalDrilldownAction)terminalAction, keys);
			} else if (terminalAction instanceof TerminalMappedAction) {
				terminalSession.doAction(terminalAction, currentEntity);
			} else {
				terminalAction.perform(terminalSession, currentEntity, keys);

			}
			currentEntity = terminalSession.getEntity();
		}
	}

	private static void assignField(User currentUser, ScreenPojoFieldAccessor fieldAccessor,
			FieldAssignDefinition fieldAssignDefinition, String value) {
		boolean doAssign = true;
		if (fieldAssignDefinition.getRole() != null) {
			if (currentUser != null) {
				Object userRole = currentUser.getProperties().get(Login.USER_ROLE_PROPERTY);
				if (userRole != null) {
					if (!userRole.equals(fieldAssignDefinition.getRole())) {
						doAssign = false;
					}
				}
			}
		}
		if (!doAssign) {
			return;
		}

		String fieldName = fieldAssignDefinition.getName();
		fieldAccessor.setFieldValue(fieldName, value);
		fieldAccessor.setFocusField(fieldName);

	}

	/**
	 * Look for a direct path from the target screen back to the current screen
	 */
	private static List<NavigationDefinition> findDirectNavigationPath(ScreenEntityDefinition sourceEntityDefinition,
			ScreenEntityDefinition targetEntityDefinition, ScreenEntitiesRegistry screenEntitiesRegistry) {
		Class<?> currentNavigationNode = targetEntityDefinition.getEntityClass();
		NavigationDefinition navigationDefinition = targetEntityDefinition.getNavigationDefinition();

		if (navigationDefinition == null) {
			return null;
		}

		List<NavigationDefinition> navigationSteps = new ArrayList<NavigationDefinition>();
		navigationSteps.add(navigationDefinition);
		while (currentNavigationNode != null) {
			currentNavigationNode = navigationDefinition.getAccessedFrom();
			if (currentNavigationNode == NONE.class) {
				logger.debug(MessageFormat.format(
						"Found NONE for accessedFrom in @ScreenNavigation withing screen {0}. Ignoring direct navigation path",
						currentNavigationNode.getName()));
				return null;
			}
			ScreenEntityDefinition screenEntityDefinition = screenEntitiesRegistry.get(currentNavigationNode);
			navigationDefinition = screenEntityDefinition.getNavigationDefinition();
			if (screenEntityDefinition.getEntityClass() == sourceEntityDefinition.getEntityClass()) {
				// target reached
				break;
			}
			// no navigation definition found, exit
			if (navigationDefinition == null) {
				return null;
			}

			navigationSteps.add(navigationDefinition);
		}
		if (currentNavigationNode != null) {
			return navigationSteps;
		}
		return null;
	}

}
