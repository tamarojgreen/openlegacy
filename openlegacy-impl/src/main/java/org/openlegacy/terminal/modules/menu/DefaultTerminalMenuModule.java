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
package org.openlegacy.terminal.modules.menu;

import org.openlegacy.ApplicationConnection;
import org.openlegacy.RemoteAction;
import org.openlegacy.Snapshot;
import org.openlegacy.modules.menu.Menu;
import org.openlegacy.modules.menu.MenuBuilder;
import org.openlegacy.modules.menu.MenuItem;
import org.openlegacy.terminal.ScreenEntity;
import org.openlegacy.terminal.definitions.ScreenEntityDefinition;
import org.openlegacy.terminal.services.ScreenEntitiesRegistry;
import org.openlegacy.terminal.support.TerminalSessionModuleAdapter;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;

public class DefaultTerminalMenuModule extends TerminalSessionModuleAdapter implements Menu, Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private MenuBuilder menuBuilder;

	@Inject
	private ScreenEntitiesRegistry entitiesRegistry;

	private Class<? extends ScreenEntity> currentMainMenu;

	@Override
	public void afterAction(ApplicationConnection<?, ?> connection, RemoteAction action, Snapshot result) {

		if (getSession().getEntity() == null) {
			return;
		}
		ScreenEntity entity = getSession().getEntity();

		ScreenEntityDefinition definitions = entitiesRegistry.get(entity.getClass());
		if (definitions.getType() == MenuEntity.class && definitions.getNavigationDefinition() == null) {
			currentMainMenu = entity.getClass();
		}
	}

	public MenuItem getMenuTree() {
		return menuBuilder.getMenuTree(currentMainMenu);
	}

	public MenuItem getMenuTree(Class<?> menuEntityClass) {
		return menuBuilder.getMenuTree(menuEntityClass);

	}

	public List<MenuItem> getFlatMenuEntries() {
		return menuBuilder.getFlatMenuEntries(currentMainMenu);
	}

	public List<MenuItem> getFlatMenuEntries(Class<?> menuEntityClass) {
		return menuBuilder.getFlatMenuEntries(menuEntityClass);
	}
}
