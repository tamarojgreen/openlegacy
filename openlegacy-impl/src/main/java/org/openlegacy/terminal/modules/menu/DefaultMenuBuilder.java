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
package org.openlegacy.terminal.modules.menu;

import org.apache.commons.lang.StringUtils;
import org.openlegacy.modules.menu.Menu.MenuEntity;
import org.openlegacy.modules.menu.MenuBuilder;
import org.openlegacy.modules.menu.MenuItem;
import org.openlegacy.modules.support.menu.SimpleMenuItem;
import org.openlegacy.terminal.definitions.FieldAssignDefinition;
import org.openlegacy.terminal.definitions.NavigationDefinition;
import org.openlegacy.terminal.definitions.ScreenEntityDefinition;
import org.openlegacy.terminal.services.ScreenEntitiesRegistry;
import org.springframework.util.Assert;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

public class DefaultMenuBuilder implements MenuBuilder {

	@Inject
	private ScreenEntitiesRegistry screenEntitiesRegistry;

	private Map<Class<?>, List<Class<?>>> allMenusOptions = null;

	private MenuItemsComparator menuItemsComparator;

	public MenuItem getMenuTree(Class<?> menuEntityClass) {
		sortToMenus();
		return buildMenu(menuEntityClass);
	}

	public MenuItem getMenuTree() {

		sortToMenus();

		Class<?> rootClass = findRoot();
		return buildMenu(rootClass);
	}

	private void sortToMenus() {

		if (allMenusOptions != null) {
			return;
		}

		allMenusOptions = new HashMap<Class<?>, List<Class<?>>>();

		Collection<ScreenEntityDefinition> entitiesDefinitions = screenEntitiesRegistry.getEntitiesDefinitions();

		for (ScreenEntityDefinition entityDefinition : entitiesDefinitions) {
			NavigationDefinition navigationDefintion = entityDefinition.getNavigationDefinition();
			if (navigationDefintion == null) {
				continue;
			}
			Class<?> accessedFrom = navigationDefintion.getAccessedFrom();
			Assert.notNull(accessedFrom);
			ScreenEntityDefinition accessedFromDefinition = screenEntitiesRegistry.get(accessedFrom);
			if (accessedFromDefinition.getType() == MenuEntity.class) {
				addMenuOption(accessedFrom, entityDefinition.getEntityClass());
			}
		}
	}

	private void addMenuOption(Class<?> accessedFrom, Class<?> screenEntityClass) {
		List<Class<?>> menuOptions = allMenusOptions.get(accessedFrom);
		if (menuOptions == null) {
			menuOptions = new ArrayList<Class<?>>();
			allMenusOptions.put(accessedFrom, menuOptions);
		}
		menuOptions.add(screenEntityClass);
	}

	private Class<?> findRoot() {
		Set<Class<?>> menuEntityClasses = allMenusOptions.keySet();

		Class<?> rootMenuClass = null;
		for (Class<?> menuEntityClass : menuEntityClasses) {
			ScreenEntityDefinition menuDefintion = screenEntitiesRegistry.get(menuEntityClass);
			if (menuDefintion.getNavigationDefinition() == null) {
				Assert.isNull(rootMenuClass,
						MessageFormat.format("Found more then one root menu entries:{0}, {1}", rootMenuClass, menuEntityClass));
				rootMenuClass = menuEntityClass;
			}
		}

		return rootMenuClass;
	}

	private MenuItem buildMenu(Class<?> rootClass) {
		if (rootClass == null) {
			return null;
		}

		ScreenEntityDefinition screenEntityDefinition = screenEntitiesRegistry.get(rootClass);

		NavigationDefinition navigationDefinition = screenEntityDefinition.getNavigationDefinition();

		// don't include in the menu screens which needs parameters to access them
		if (navigationDefinition != null && navigationDefinition.isRequiresParameters()) {
			return null;
		}

		String displayName = screenEntityDefinition.getDisplayName();
		MenuItem menuItem = new SimpleMenuItem(rootClass, displayName);

		if (!allMenusOptions.containsKey(rootClass)) {
			return menuItem;
		}

		List<Class<?>> menuOptions = allMenusOptions.get(rootClass);

		Collections.sort(menuOptions, getMenuItemsComparator());
		for (Class<?> menuOption : menuOptions) {
			MenuItem childMenuItem = buildMenu(menuOption);
			menuItem.getMenuItems().add(childMenuItem);
		}
		return menuItem;
	}

	private Comparator<Class<?>> getMenuItemsComparator() {
		if (menuItemsComparator == null) {
			menuItemsComparator = new MenuItemsComparator(screenEntitiesRegistry);
		}
		return menuItemsComparator;

	}

	/**
	 * Compare menu items by comparing the assigned fields value of the navigation definition. For example: <br/>
	 * Screen A: @ScreenNavigation(... assignedFields = { @AssignedField(field = "selection", value = "1") } ...) <br/>
	 * Screen B: @ScreenNavigation(... assignedFields = { @AssignedField(field = "selection", value = "2") } ...)
	 * 
	 * @author RoiM
	 * 
	 */
	private static class MenuItemsComparator implements Comparator<Class<?>> {

		private ScreenEntitiesRegistry screenEntitiesRegistry;

		public MenuItemsComparator(ScreenEntitiesRegistry screenEntitiesRegistry) {
			this.screenEntitiesRegistry = screenEntitiesRegistry;
		}

		public int compare(Class<?> o1, Class<?> o2) {
			NavigationDefinition menuItem1NavigationDefinition = screenEntitiesRegistry.get(o1).getNavigationDefinition();
			NavigationDefinition menuItem2NavigationDefinition = screenEntitiesRegistry.get(o2).getNavigationDefinition();

			List<FieldAssignDefinition> assignedFields1 = menuItem1NavigationDefinition.getAssignedFields();
			List<FieldAssignDefinition> assignedFields2 = menuItem2NavigationDefinition.getAssignedFields();

			if (assignedFields1.size() != 1 || assignedFields2.size() != 1) {
				return 0;
			}
			String assignedField1Value = assignedFields1.get(0).getValue();
			String assignedField2Value = assignedFields2.get(0).getValue();

			if (StringUtils.isNumeric(assignedField1Value) && StringUtils.isNumeric(assignedField1Value)) {
				return Integer.valueOf(assignedField1Value) - Integer.valueOf(assignedField2Value);
			}
			return assignedField1Value.compareTo(assignedField2Value);
		}
	}

}
