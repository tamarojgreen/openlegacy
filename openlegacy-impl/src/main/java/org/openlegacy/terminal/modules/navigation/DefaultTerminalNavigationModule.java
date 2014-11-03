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
package org.openlegacy.terminal.modules.navigation;

import org.openlegacy.EntityDescriptor;
import org.openlegacy.SimpleEntityDescriptor;
import org.openlegacy.modules.navigation.Navigation;
import org.openlegacy.terminal.actions.TerminalAction;
import org.openlegacy.terminal.actions.TerminalActions;
import org.openlegacy.terminal.definitions.NavigationDefinition;
import org.openlegacy.terminal.definitions.ScreenEntityDefinition;
import org.openlegacy.terminal.services.ScreenEntitiesRegistry;
import org.openlegacy.terminal.support.TerminalSessionModuleAdapter;
import org.openlegacy.utils.ReflectionUtil;
import org.openlegacy.utils.SpringUtil;
import org.springframework.context.ApplicationContext;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

public class DefaultTerminalNavigationModule extends TerminalSessionModuleAdapter implements Navigation {

	private static final long serialVersionUID = 1L;

	@Inject
	private transient ApplicationContext applicationContext;

	private TerminalAction defaultExitAction = TerminalActions.F3();

	private int maxPath = 7;

	@Override
	public List<EntityDescriptor> getPaths() {

		Object currentEntity = getSession().getEntity();

		List<EntityDescriptor> pathEntries = new ArrayList<EntityDescriptor>();
		if (currentEntity == null) {
			return pathEntries;
		}

		ScreenEntitiesRegistry screenEntitiesRegistry = SpringUtil.getBean(applicationContext, ScreenEntitiesRegistry.class);

		ScreenEntityDefinition currentEntityDefinition = screenEntitiesRegistry.get(currentEntity.getClass());

		boolean first = true;

		int pathCount = 0;

		while (currentEntityDefinition != null && pathCount <= maxPath) {
			NavigationDefinition navigationDefinition = currentEntityDefinition.getNavigationDefinition();
			boolean requiresParameters = navigationDefinition == null ? false : navigationDefinition.isRequiresParameters();
			SimpleEntityDescriptor pathEntry = new SimpleEntityDescriptor(currentEntityDefinition.getEntityClass(),
					currentEntityDefinition.getEntityName(), currentEntityDefinition.getDisplayName(), requiresParameters);
			pathEntry.setCurrent(first);

			if (currentEntityDefinition.isChild()) {
				currentEntityDefinition = navigationDefinition != null ? screenEntitiesRegistry.get(navigationDefinition.getAccessedFrom())
						: null;
				continue;
			}

			first = false;

			currentEntityDefinition = navigationDefinition != null ? screenEntitiesRegistry.get(navigationDefinition.getAccessedFrom())
					: null;

			pathEntries.add(pathEntry);

			pathCount++;
		}

		Collections.reverse(pathEntries);

		return pathEntries;
	}

	public void setDefaultExitActionClass(Class<? extends TerminalAction> defaultExitAction) {
		this.defaultExitAction = ReflectionUtil.newInstance(defaultExitAction);
	}

	@Override
	public TerminalAction getDefaultExitAction() {
		return defaultExitAction;
	}

	public void setMaxPath(int maxPath) {
		this.maxPath = maxPath;
	}
}
