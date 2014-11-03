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
package org.openlegacy.terminal.modules.table;

import org.openlegacy.exceptions.RegistryException;
import org.openlegacy.modules.table.Table;
import org.openlegacy.modules.table.TableCollector;
import org.openlegacy.modules.table.drilldown.DrilldownAction;
import org.openlegacy.modules.table.drilldown.TableDrilldownPerformer;
import org.openlegacy.terminal.TerminalSession;
import org.openlegacy.terminal.definitions.NavigationDefinition;
import org.openlegacy.terminal.definitions.ScreenTableDefinition;
import org.openlegacy.terminal.definitions.ScreenTableDefinition.DrilldownDefinition;
import org.openlegacy.terminal.providers.TablesDefinitionProvider;
import org.openlegacy.terminal.services.ScreenEntitiesRegistry;
import org.openlegacy.terminal.support.TerminalSessionModuleAdapter;
import org.openlegacy.utils.ProxyUtil;
import org.openlegacy.utils.SpringUtil;
import org.springframework.context.ApplicationContext;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;

@SuppressWarnings("unchecked")
public class DefaultTerminalTableModule extends TerminalSessionModuleAdapter implements Table, Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private TablesDefinitionProvider tablesDefinitionProvider;

	@Inject
	private transient ApplicationContext applicationContext;

	@Override
	public <T> List<T> collectAll(Class<?> screenEntityClass, Class<T> rowClass) {
		TableCollector<TerminalSession, T> tableCollector = getTableCollector(screenEntityClass);
		return tableCollector.collectAll(getSession(), screenEntityClass, rowClass);
	}

	@Override
	public <T> List<T> collect(Class<?> screenEntityClass, Class<T> rowClass, int numberOfScreens) {
		TableCollector<TerminalSession, T> tableCollector = getTableCollector(screenEntityClass);
		return tableCollector.collect(getSession(), screenEntityClass, rowClass, numberOfScreens);
	}

	/**
	 * Method which should be used when NOT using open legacy navigation definitions: <code>@ScreenNavigation</code> and using a
	 * provider session navigator
	 */
	@Override
	public <T> T drillDown(Class<?> sourceEntityClass, Class<T> targetEntityClass, DrilldownAction<?> drilldownAction,
			Object... rowKeys) {

		if (ProxyUtil.isClassesMatch(getSession().getEntity().getClass(), targetEntityClass)) {
			return (T)getSession().getEntity();
		}

		ScreenTableDefinition tableDefinition = ScrollableTableUtil.getSingleScrollableTableDefinition(tablesDefinitionProvider,
				sourceEntityClass).getValue();
		DrilldownDefinition drilldownDefinition = tableDefinition.getDrilldownDefinition();

		TableDrilldownPerformer<TerminalSession> actualDrilldownPerformer = SpringUtil.getDefaultBean(applicationContext,
				drilldownDefinition.getDrilldownPerformer());

		return actualDrilldownPerformer.drilldown(drilldownDefinition, getSession(), sourceEntityClass, targetEntityClass,
				drilldownAction, rowKeys);
	}

	/**
	 * Method which should be used when using open legacy navigation definitions: <code>@ScreenNavigation</code>
	 */
	@Override
	public <T> T drillDown(Class<T> targetClass, DrilldownAction<?> drilldownAction, Object... rowKeys) throws RegistryException {
		ScreenEntitiesRegistry screenEntitiesRegistry = SpringUtil.getBean(applicationContext, ScreenEntitiesRegistry.class);
		NavigationDefinition navigationDefinition = screenEntitiesRegistry.get(targetClass).getNavigationDefinition();
		if (navigationDefinition == null) {
			throw (new RegistryException(targetClass.getName() + " has no navigation definition"));
		}
		Class<?> accessedFrom = navigationDefinition.getAccessedFrom();

		return drillDown(accessedFrom, targetClass, drilldownAction, rowKeys);
	}

	@Override
	public <T> List<T> collectOne(Class<?> screenEntityClass, Class<T> rowClass) {
		TableCollector<TerminalSession, T> tableCollector = getTableCollector(screenEntityClass);
		return tableCollector.collectOne(getSession(), screenEntityClass, rowClass);
	}

	private <T> TableCollector<TerminalSession, T> getTableCollector(Class<?> screenEntityClass) {
		ScreenTableDefinition tableDefinition = ScrollableTableUtil.getSingleScrollableTableDefinition(tablesDefinitionProvider,
				screenEntityClass).getValue();

		TableCollector<TerminalSession, T> tableCollector = SpringUtil.getDefaultBean(applicationContext,
				tableDefinition.getTableCollector());
		return tableCollector;
	}
}
