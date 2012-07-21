/*******************************************************************************
 * Copyright (c) 2012 OpenLegacy Inc.
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
import org.openlegacy.modules.navigation.Navigation;
import org.openlegacy.terminal.ScreenEntity;
import org.openlegacy.terminal.ScreenPojoFieldAccessor;
import org.openlegacy.terminal.TerminalSession;
import org.openlegacy.terminal.TerminalSnapshot;
import org.openlegacy.terminal.actions.TerminalAction;
import org.openlegacy.terminal.definitions.FieldAssignDefinition;
import org.openlegacy.terminal.definitions.NavigationDefinition;
import org.openlegacy.terminal.definitions.ScreenEntityDefinition;
import org.openlegacy.terminal.exceptions.ScreenEntityNotAccessibleException;
import org.openlegacy.terminal.services.ScreenEntitiesRegistry;
import org.openlegacy.terminal.services.ScreensRecognizer;
import org.openlegacy.terminal.services.SessionNavigator;
import org.openlegacy.terminal.utils.ScreenNavigationUtil;
import org.openlegacy.terminal.utils.SimpleScreenPojoFieldAccessor;
import org.openlegacy.utils.ProxyUtil;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

public class DefaultSessionNavigator implements SessionNavigator {

	@Inject
	private ScreenEntitiesRegistry screenEntitiesRegistry;

	@Inject
	private ScreensRecognizer screensRecognizer;

	@Inject
	private NavigationMetadata navigationMetadata;

	private final static Log logger = LogFactory.getLog(DefaultSessionNavigator.class);

	public void navigate(TerminalSession terminalSession, Class<?> targetScreenEntityClass) throws ScreenEntityNotAccessibleException {

		TerminalSnapshot snapshot = terminalSession.getSnapshot();
		Class<?> currentEntityClass = screensRecognizer.match(snapshot);

		if (currentEntityClass == null) {
			return;
		}

		if (ProxyUtil.isClassesMatch(currentEntityClass, targetScreenEntityClass)) {
			return;
		}

		ScreenEntityDefinition currentEntityDefinition = screenEntitiesRegistry.get(currentEntityClass);
		ScreenEntityDefinition targetEntityDefinition = screenEntitiesRegistry.get(targetScreenEntityClass);

		List<NavigationDefinition> navigationSteps = navigationMetadata.get(currentEntityDefinition, targetEntityDefinition);

		while (navigationSteps == null) {
			if (currentEntityDefinition.getEntityClass() == targetEntityDefinition.getEntityClass()) {
				return;
			}

			navigationSteps = findDirectNavigationPath(currentEntityDefinition, targetEntityDefinition);

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

				ScreenEntityDefinition beforeEntityClass = currentEntityDefinition;

				exitCurrentScreen(terminalSession, currentEntityClass, exitAction);

				snapshot = terminalSession.getSnapshot();
				currentEntityClass = screensRecognizer.match(snapshot);
				currentEntityDefinition = screenEntitiesRegistry.get(currentEntityClass);

				if (currentEntityDefinition == beforeEntityClass) {
					logger.error(MessageFormat.format(
							"Exiting from screen {0} using {1} was not effective. Existing navigation attempt to {2}",
							currentEntityClass, exitAction, targetScreenEntityClass));

					ScreenNavigationUtil.validateCurrentScreen(targetScreenEntityClass, currentEntityClass);
					break;
				}
			}
		}

		if (navigationSteps != null) {
			navigationMetadata.add(currentEntityDefinition, targetEntityDefinition, navigationSteps);
			performDirectNavigation(terminalSession, currentEntityClass, navigationSteps);
		}
	}

	private static void exitCurrentScreen(TerminalSession terminalSession, Class<?> currentEntityClass, TerminalAction exitAction) {
		if (logger.isDebugEnabled()) {
			logger.debug(MessageFormat.format("Steping back of screen {0} using {1}", currentEntityClass, exitAction));
		}
		terminalSession.doAction(exitAction);
	}

	private static void performDirectNavigation(TerminalSession terminalSession, Class<?> currentEntityClass,
			List<NavigationDefinition> navigationSteps) {
		Collections.reverse(navigationSteps);
		ScreenEntity currentEntity = terminalSession.getEntity();
		for (NavigationDefinition navigationDefinition : navigationSteps) {
			ScreenPojoFieldAccessor fieldAccessor = new SimpleScreenPojoFieldAccessor(currentEntity);
			List<FieldAssignDefinition> assignedFields = navigationDefinition.getAssignedFields();
			if (logger.isDebugEnabled()) {
				currentEntityClass = ProxyUtil.getOriginalClass(currentEntity.getClass());
				logger.debug("Performing navigation actions from screen " + currentEntityClass);
			}
			for (FieldAssignDefinition fieldAssignDefinition : assignedFields) {
				String value = fieldAssignDefinition.getValue();
				if (value != null) {
					fieldAccessor.setFieldValue(fieldAssignDefinition.getName(), value);
				}
				fieldAccessor.setFocusField(fieldAssignDefinition.getName());
			}
			if (assignedFields.size() == 0) {
				currentEntity = null;
			}
			terminalSession.doAction(navigationDefinition.getTerminalAction(), currentEntity);
			currentEntity = terminalSession.getEntity();
		}
	}

	/**
	 * Look for a direct path from the target screen back to the current screen
	 */
	private List<NavigationDefinition> findDirectNavigationPath(ScreenEntityDefinition sourceEntityDefinition,
			ScreenEntityDefinition targetEntityDefinition) {
		Class<?> currentNavigationNode = targetEntityDefinition.getEntityClass();
		NavigationDefinition navigationDefinition = targetEntityDefinition.getNavigationDefinition();

		if (navigationDefinition == null) {
			return null;
		}

		List<NavigationDefinition> navigationSteps = new ArrayList<NavigationDefinition>();
		navigationSteps.add(navigationDefinition);
		while (currentNavigationNode != null) {
			currentNavigationNode = navigationDefinition.getAccessedFrom();
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
