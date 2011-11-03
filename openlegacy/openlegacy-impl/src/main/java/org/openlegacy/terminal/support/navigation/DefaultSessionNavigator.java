package org.openlegacy.terminal.support.navigation;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openlegacy.exceptions.HostEntityNotAccessibleException;
import org.openlegacy.terminal.TerminalSession;
import org.openlegacy.terminal.definitions.FieldAssignDefinition;
import org.openlegacy.terminal.definitions.NavigationDefinition;
import org.openlegacy.terminal.definitions.ScreenEntityDefinition;
import org.openlegacy.terminal.spi.ScreenEntitiesRegistry;
import org.openlegacy.terminal.spi.SessionNavigator;
import org.openlegacy.terminal.utils.ScreenEntityDirectFieldAccessor;
import org.openlegacy.terminal.utils.ScreenSyncValidator;
import org.openlegacy.utils.ProxyUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DefaultSessionNavigator implements SessionNavigator {

	@Autowired
	private ScreenEntitiesRegistry screenEntitiesRegistry;

	@Autowired
	private NavigationCache navigationCache;

	private final static Log logger = LogFactory.getLog(DefaultSessionNavigator.class);

	public void navigate(TerminalSession terminalSession, Class<?> targetScreenEntity) throws HostEntityNotAccessibleException {

		Object currentEntity = terminalSession.getEntity();

		if (ProxyUtil.isClassesMatch(currentEntity.getClass(), targetScreenEntity)) {
			return;
		}

		ScreenEntityDefinition currentEntityDefinition = screenEntitiesRegistry.get(currentEntity.getClass());
		ScreenEntityDefinition targetEntityDefinition = screenEntitiesRegistry.get(targetScreenEntity);

		List<NavigationDefinition> navigationSteps = navigationCache.get(currentEntityDefinition, targetEntityDefinition);

		while (navigationSteps == null) {
			navigationSteps = findDirectNavigationPath(currentEntityDefinition, targetEntityDefinition);

			if (navigationSteps == null) {
				NavigationDefinition currentScreenNavigationDefinition = currentEntityDefinition.getNavigationDefinition();
				if (currentScreenNavigationDefinition == null) {
					break;
				}
				exitCurrentScreen(terminalSession, currentEntity, currentScreenNavigationDefinition);
				currentEntity = terminalSession.getEntity();
				currentEntityDefinition = screenEntitiesRegistry.get(currentEntity.getClass());
			}
		}

		if (navigationSteps == null) {
			ScreenSyncValidator.validateCurrentScreen(targetScreenEntity, currentEntity.getClass());
		}

		navigationCache.add(currentEntityDefinition, targetEntityDefinition, navigationSteps);

		performDirectNavigation(terminalSession, currentEntity, navigationSteps);
	}

	private static void exitCurrentScreen(TerminalSession terminalSession, Object currentEntity,
			NavigationDefinition currentScreenNavigationDefinition) {
		if (logger.isDebugEnabled()) {
			logger.debug(MessageFormat.format("Steping back of screen {0} using {1}", currentEntity.getClass(),
					currentScreenNavigationDefinition.getExitAction()));
		}
		terminalSession.doAction(currentScreenNavigationDefinition.getExitAction(), null);
	}

	private static void performDirectNavigation(TerminalSession terminalSession, Object currentEntity,
			List<NavigationDefinition> navigationSteps) {
		Collections.reverse(navigationSteps);
		for (NavigationDefinition navigationDefinition : navigationSteps) {
			ScreenEntityDirectFieldAccessor fieldAccessor = new ScreenEntityDirectFieldAccessor(currentEntity);
			List<FieldAssignDefinition> assignedFields = navigationDefinition.getAssignedFields();
			if (logger.isDebugEnabled()) {
				Class<? extends Object> currentEntityClass = ProxyUtil.getOriginalClass(currentEntity.getClass());
				logger.debug("Performing navigation actions from screen " + currentEntityClass);
			}
			for (FieldAssignDefinition fieldAssignDefinition : assignedFields) {
				fieldAccessor.setFieldValue(fieldAssignDefinition.getName(), fieldAssignDefinition.getValue());
			}
			terminalSession.doAction(navigationDefinition.getHostAction(), currentEntity);
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
