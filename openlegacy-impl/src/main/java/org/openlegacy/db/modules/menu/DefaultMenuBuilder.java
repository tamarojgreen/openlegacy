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

package org.openlegacy.db.modules.menu;

import org.openlegacy.db.definitions.DbEntityDefinition;
import org.openlegacy.db.modules.menu.DefaultDbMenuModule.DbMenuItem;
import org.openlegacy.db.services.DbEntitiesRegistry;
import org.openlegacy.modules.menu.MenuBuilder;
import org.openlegacy.modules.menu.MenuItem;
import org.openlegacy.modules.support.menu.SimpleMenuItem;
import org.openlegacy.utils.SpringUtil;
import org.springframework.context.ApplicationContext;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.inject.Inject;

public class DefaultMenuBuilder implements MenuBuilder, Serializable {

	private static final long serialVersionUID = 1L;

	private static final String MAIN = "Main";

	@Inject
	private transient ApplicationContext applicationContext;

	private Map<String, List<Class<?>>> menuOptions = new TreeMap<String, List<Class<?>>>();

	@Override
	public MenuItem getMenuTree(Class<?> menuEntityClass) {
		return getMenuTree();
	}

	@Override
	public MenuItem getMenuTree() {
		Collection<DbEntityDefinition> entityDefinitions = getEntitiesRegistry().getEntitiesDefinitions();
		for (DbEntityDefinition dbEntityDefinition : entityDefinitions) {
			String category = dbEntityDefinition.getNavigationDefinition().getCategory();
			if (category != null) {
				if (!menuOptions.containsKey(category)) {
					List<Class<?>> items = new ArrayList<Class<?>>();
					menuOptions.put(category, items);
				}
				List<Class<?>> items = menuOptions.get(category);
				if (!items.contains(dbEntityDefinition.getEntityClass())) {
					items.add(dbEntityDefinition.getEntityClass());
				}
			}
		}
		SimpleMenuItem menuItem = new SimpleMenuItem(DbMenuItem.class, MAIN, 1);
		menuItem.getMenuItems().addAll(buildMenu());
		return menuItem;
	}

	@Override
	public List<MenuItem> getFlatMenuEntries() {
		return buildMenu();
	}

	@Override
	public List<MenuItem> getFlatMenuEntries(Class<?> menuEntityClass) {
		return getFlatMenuEntries();
	}

	private DbEntitiesRegistry getEntitiesRegistry() {
		return SpringUtil.getBean(applicationContext, DbEntitiesRegistry.class);
	}

	private List<MenuItem> buildMenu() {
		Collection<String> menuCategories = menuOptions.keySet();
		List<MenuItem> menuItems = new ArrayList<MenuItem>();
		for (String menuCategory : menuCategories) {
			MenuItem menuSubTree = new SimpleMenuItem(DbMenuItem.class, menuCategory, 1);
			menuItems.add(menuSubTree);
			List<Class<?>> entries = menuOptions.get(menuCategory);
			for (Class<?> entry : entries) {
				DbEntityDefinition definition = getEntitiesRegistry().get(entry);
				MenuItem menuItem = new SimpleMenuItem(entry, definition.getDisplayName(), 2);
				menuSubTree.getMenuItems().add(menuItem);
			}
		}
		return menuItems;
	}

}
