package org.openlegacy.terminal.support;

import org.openlegacy.exceptions.HostEntityNotAccessibleException;
import org.openlegacy.terminal.TerminalSession;
import org.openlegacy.terminal.definitions.FieldAssignDefinition;
import org.openlegacy.terminal.definitions.NavigationDefinition;
import org.openlegacy.terminal.definitions.ScreenEntityDefinition;
import org.openlegacy.terminal.spi.ScreenEntitiesRegistry;
import org.openlegacy.terminal.spi.SessionNavigator;
import org.openlegacy.terminal.utils.ScreenEntityDirectFieldAccessor;
import org.openlegacy.utils.ProxyUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DefaultSessionNavigator implements SessionNavigator {

	@Autowired
	private ScreenEntitiesRegistry screenEntitiesRegistry;

	public void navigate(TerminalSession terminalSession, Class<?> targetScreenEntity) throws HostEntityNotAccessibleException {

		Object currentEntity = terminalSession.getEntity();

		if (ProxyUtil.isClassesMatch(currentEntity.getClass(), targetScreenEntity)) {
			return;
		}

		ScreenEntityDefinition currentEntityDefinition = screenEntitiesRegistry.get(currentEntity.getClass());
		ScreenEntityDefinition targetEntityDefinition = screenEntitiesRegistry.get(targetScreenEntity);

		List<NavigationDefinition> navigationSteps = findNavigationPath(currentEntityDefinition, targetEntityDefinition);
		if (navigationSteps != null) {
			Collections.reverse(navigationSteps);
			for (NavigationDefinition navigationDefinition : navigationSteps) {
				ScreenEntityDirectFieldAccessor fieldAccessor = new ScreenEntityDirectFieldAccessor(currentEntity);
				List<FieldAssignDefinition> assignedFields = navigationDefinition.getAssignedFields();
				for (FieldAssignDefinition fieldAssignDefinition : assignedFields) {
					fieldAccessor.setFieldValue(fieldAssignDefinition.getName(), fieldAssignDefinition.getValue());
				}
				terminalSession.doAction(navigationDefinition.getHostAction(), currentEntity);
			}
		}
	}

	/**
	 * Look for a path from a
	 */
	private List<NavigationDefinition> findNavigationPath(ScreenEntityDefinition sourceEntityDefinition,
			ScreenEntityDefinition targetEntityDefinition) {
		Class<?> currentNavigationNode = targetEntityDefinition.getEntityClass();
		NavigationDefinition navigationDefinition = targetEntityDefinition.getNavigationDefinition();

		List<NavigationDefinition> navigationSteps = new ArrayList<NavigationDefinition>();
		navigationSteps.add(navigationDefinition);
		while (currentNavigationNode != null) {
			currentNavigationNode = navigationDefinition.getAccessedFrom();
			ScreenEntityDefinition screenEntityDefinition = screenEntitiesRegistry.get(currentNavigationNode);
			navigationDefinition = screenEntityDefinition.getNavigationDefinition();
			if (screenEntityDefinition.getEntityClass() == sourceEntityDefinition.getEntityClass()) {
				break;
			}

			navigationSteps.add(navigationDefinition);
		}
		if (currentNavigationNode != null) {
			return navigationSteps;
		}
		return null;
	}

}
