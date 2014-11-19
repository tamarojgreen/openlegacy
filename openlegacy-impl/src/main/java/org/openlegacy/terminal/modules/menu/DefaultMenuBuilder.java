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

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.openlegacy.modules.menu.Menu.MenuEntity;
import org.openlegacy.modules.menu.MenuBuilder;
import org.openlegacy.modules.menu.MenuItem;
import org.openlegacy.modules.support.menu.SimpleMenuItem;
import org.openlegacy.terminal.definitions.FieldAssignDefinition;
import org.openlegacy.terminal.definitions.NavigationDefinition;
import org.openlegacy.terminal.definitions.ScreenEntityDefinition;
import org.openlegacy.terminal.services.ScreenEntitiesRegistry;
import org.openlegacy.utils.ProxyUtil;
import org.openlegacy.utils.SpringUtil;
import org.springframework.context.ApplicationContext;
import org.springframework.util.Assert;

import java.io.Serializable;
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

public class DefaultMenuBuilder implements MenuBuilder, Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private transient ApplicationContext applicationContext;

	private Map<Class<?>, List<Class<?>>> allMenusOptions = null;
	private Map<Class<?>, Integer> allMenusDepths = null;

	private MenuItemsComparator menuItemsComparator;

	ScreenEntitiesRegistry screenEntitiesRegistry;

	@Override
	public MenuItem getMenuTree(Class<?> menuEntityClass) {
		if (menuEntityClass == null) {
			return getMenuTree();
		}
		ScreenEntityDefinition menuDefinition = getScreenEntitiesRegistry().get(menuEntityClass);

		Assert.notNull(menuDefinition,
				MessageFormat.format("Class {0} is not a registered screen entity", menuEntityClass.getName()));
		Assert.isTrue(menuDefinition.getType() == MenuEntity.class,
				MessageFormat.format("{0} is not a screen entity", menuEntityClass.getName()));

		sortToMenus();
		Integer subMenuDepth = allMenusDepths.get(ProxyUtil.getOriginalClass(menuEntityClass));
		if (subMenuDepth == null) {
			return new SimpleMenuItem(menuEntityClass, menuDefinition.getDisplayName(), 0);
		}
		return buildMenu(menuEntityClass, subMenuDepth);
	}

	@Override
	public MenuItem getMenuTree() {

		sortToMenus();

		List<Class<?>> rootClasses = findRoots();
		if (rootClasses.size() > 1) {
			throw (new IllegalStateException("More then one root menus exists"));
		}
		if (rootClasses.size() == 0) {
			return null;
		}
		return getMenuTree(rootClasses.get(0));
	}

	private void sortToMenus() {

		// fetch an updated bean -
		screenEntitiesRegistry = getScreenEntitiesRegistry();

		if (allMenusOptions != null && !screenEntitiesRegistry.isDirty()) {
			return;
		}

		allMenusOptions = new HashMap<Class<?>, List<Class<?>>>();
		allMenusDepths = new HashMap<Class<?>, Integer>();

		Collection<ScreenEntityDefinition> entitiesDefinitions = screenEntitiesRegistry.getEntitiesDefinitions();

		for (ScreenEntityDefinition entityDefinition : entitiesDefinitions) {
			NavigationDefinition navigationDefintion = entityDefinition.getNavigationDefinition();
			if (navigationDefintion == null) {
				continue;
			}
			Class<?> accessedFrom = navigationDefintion.getAccessedFrom();
			Assert.notNull(accessedFrom);
			ScreenEntityDefinition accessedFromDefinition = screenEntitiesRegistry.get(accessedFrom);
			if (accessedFromDefinition != null && accessedFromDefinition.getType() == MenuEntity.class) {
				addMenuOption(accessedFrom, entityDefinition.getEntityClass());
			}
		}
	}

	private ScreenEntitiesRegistry getScreenEntitiesRegistry() {
		return SpringUtil.getBean(applicationContext, ScreenEntitiesRegistry.class);
	}

	private void addMenuOption(Class<?> accessedFrom, Class<?> screenEntityClass) {
		List<Class<?>> menuOptions = allMenusOptions.get(accessedFrom);
		if (menuOptions == null) {
			menuOptions = new ArrayList<Class<?>>();
			allMenusOptions.put(accessedFrom, menuOptions);
			allMenusDepths.put(accessedFrom, calculateDepth(accessedFrom));

		}
		menuOptions.add(screenEntityClass);
		Collections.sort(menuOptions, new Comparator<Class<?>>() {

			@Override
			public int compare(Class<?> o1, Class<?> o2) {
				return compareEntitiesByAssignFields(screenEntitiesRegistry.get(o1), screenEntitiesRegistry.get(o2));
			}

		});

	}

	private static int compareEntitiesByAssignFields(ScreenEntityDefinition screenEntityDefinition1,
			ScreenEntityDefinition screenEntityDefinition2) {

		NavigationDefinition navDefinition1 = screenEntityDefinition1.getNavigationDefinition();
		NavigationDefinition navDefinition2 = screenEntityDefinition2.getNavigationDefinition();
		if (navDefinition1 != null && navDefinition1.getAccessedFrom() == screenEntityDefinition2.getEntityClass()) {
			return 1;
		}
		if (navDefinition2 != null && navDefinition2.getAccessedFrom() == screenEntityDefinition1.getEntityClass()) {
			return -1;
		}
		if (navDefinition1 == null || navDefinition2 == null) {
			return 0;
		}
		List<FieldAssignDefinition> assignedFields1 = navDefinition1.getAssignedFields();
		List<FieldAssignDefinition> assignedFields2 = navDefinition2.getAssignedFields();
		if (assignedFields1.size() == 0 || assignedFields2.size() == 0) {
			return 0;
		}
		String optionValue1 = assignedFields1.get(0).getValue();
		String optionValue2 = assignedFields2.get(0).getValue();
		if (NumberUtils.isNumber(optionValue1) && NumberUtils.isNumber(optionValue2)) {
			return Integer.parseInt(optionValue1) - Integer.parseInt(optionValue2);
		}
		return optionValue1.compareTo(optionValue2);
	}

	private Integer calculateDepth(Class<?> entityClass) {
		int depth = 1;
		ScreenEntityDefinition entityDefinition = screenEntitiesRegistry.get(entityClass);
		while (entityDefinition.getNavigationDefinition() != null) {
			depth++;
			entityDefinition = screenEntitiesRegistry.get(entityDefinition.getNavigationDefinition().getAccessedFrom());
		}
		return depth;
	}

	private List<Class<?>> findRoots() {
		Set<Class<?>> menuEntityClasses = allMenusOptions.keySet();

		List<Class<?>> rootMenuClasses = new ArrayList<Class<?>>();
		for (Class<?> menuEntityClass : menuEntityClasses) {
			ScreenEntityDefinition menuDefintion = screenEntitiesRegistry.get(menuEntityClass);
			if (menuDefintion.getNavigationDefinition() == null) {
				rootMenuClasses.add(menuEntityClass);
			}
		}

		return rootMenuClasses;
	}

	private MenuItem buildMenu(Class<?> rootClass, int depth) {
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
		MenuItem menuItem = new SimpleMenuItem(rootClass, displayName, depth);

		if (!allMenusOptions.containsKey(ProxyUtil.getOriginalClass(rootClass))) {
			return menuItem;
		}

		List<Class<?>> menuOptions = allMenusOptions.get(ProxyUtil.getOriginalClass(rootClass));

		Collections.sort(menuOptions, getMenuItemsComparator());
		for (Class<?> menuOption : menuOptions) {
			MenuItem childMenuItem = buildMenu(menuOption, depth + 1);
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

		@Override
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

	@Override
	public List<MenuItem> getFlatMenuEntries(Class<?> menuEntityClass) {

		sortToMenus();

		Set<Class<?>> subMenus = allMenusOptions.keySet();

		List<MenuItem> flatMenuEntries = new ArrayList<MenuItem>();
		for (Class<?> subMenu : subMenus) {
			List<Class<?>> leafs = allMenusOptions.get(subMenu);
			if (hasLeafs(leafs)) {
				ScreenEntityDefinition subMenuEntityDefinition = screenEntitiesRegistry.get(subMenu);
				Integer subMenuDepth = allMenusDepths.get(subMenu);
				if (!isSibling(menuEntityClass, subMenu)) {
					continue;
				}
				MenuItem subMenuItem = new SimpleMenuItem(subMenu, subMenuEntityDefinition.getDisplayName(), subMenuDepth);
				for (Class<?> leaf : leafs) {
					ScreenEntityDefinition screenEntityDefinition = screenEntitiesRegistry.get(leaf);
					if (screenEntityDefinition.getType() == MenuEntity.class) {
						continue;
					}
					NavigationDefinition navigationDefinition = screenEntityDefinition.getNavigationDefinition();

					// don't include in the menu screens which needs parameters to access them
					if (navigationDefinition == null || navigationDefinition.isRequiresParameters()) {
						continue;
					}
					MenuItem menuLeaf = new SimpleMenuItem(leaf, screenEntityDefinition.getDisplayName(), subMenuDepth + 1);
					subMenuItem.getMenuItems().add(menuLeaf);
				}
				flatMenuEntries.add(subMenuItem);

				Collections.sort(flatMenuEntries, new Comparator<MenuItem>() {

					@Override
					public int compare(MenuItem o1, MenuItem o2) {
						return compareEntitiesByAssignFields(screenEntitiesRegistry.get(o1.getTargetEntity()),
								screenEntitiesRegistry.get(o2.getTargetEntity()));
					}
				});
			}
		}

		return flatMenuEntries;
	}

	private boolean isSibling(Class<?> rootMenuClass, Class<?> subMenuClass) {
		if (rootMenuClass == null) {
			return true;
		}
		while (true) {
			ScreenEntityDefinition subMenuEntityDefinition = screenEntitiesRegistry.get(subMenuClass);
			if (ProxyUtil.isClassesMatch(subMenuEntityDefinition.getEntityClass(), rootMenuClass)) {
				return true;
			}
			if (subMenuEntityDefinition.getNavigationDefinition() == null) {
				return false;
			}
			subMenuClass = subMenuEntityDefinition.getNavigationDefinition().getAccessedFrom();
		}
	}

	@Override
	public List<MenuItem> getFlatMenuEntries() {
		return getFlatMenuEntries(null);
	}

	/**
	 * Used to determine if the leafs list has a non menu entity
	 * 
	 * @param leafs
	 * @return
	 */
	private boolean hasLeafs(List<Class<?>> leafs) {
		for (Class<?> leaf : leafs) {
			if (screenEntitiesRegistry.get(leaf).getType() != MenuEntity.class) {
				return true;
			}

		}
		return false;
	}

}
