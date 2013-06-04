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
package org.openlegacy.terminal.loaders.support;

import org.openlegacy.EntitiesRegistry;
import org.openlegacy.exceptions.RegistryException;
import org.openlegacy.loaders.FieldLoader;
import org.openlegacy.terminal.definitions.ScreenEntityDefinition;
import org.openlegacy.terminal.definitions.ScreenTableDefinition;
import org.openlegacy.terminal.definitions.ScreenTableDefinition.ScreenColumnDefinition;
import org.openlegacy.terminal.services.ScreenEntitiesRegistry;
import org.openlegacy.utils.ReflectionUtil;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.text.MessageFormat;
import java.util.List;

@Component
public class TableFieldsLoader implements FieldLoader {

	@SuppressWarnings("rawtypes")
	public boolean match(EntitiesRegistry entitiesRegistry, Field field) {
		ScreenEntitiesRegistry screenEntitiesRegistry = (ScreenEntitiesRegistry)entitiesRegistry;
		Class<?> listType = ReflectionUtil.getListType(field);
		if (listType == null) {
			return false;
		}
		return (screenEntitiesRegistry.getTable(listType) != null);
	}

	@SuppressWarnings("rawtypes")
	public void load(EntitiesRegistry entitiesRegistry, Field field, Class<?> containingClass) {
		ScreenEntitiesRegistry screenEntitiesRegistry = (ScreenEntitiesRegistry)entitiesRegistry;

		Class<?> listType = ReflectionUtil.getListType(field);

		ScreenTableDefinition tableDefinition = screenEntitiesRegistry.getTable(listType);
		if (tableDefinition != null) {
			ScreenEntityDefinition screenEntityDefinition = screenEntitiesRegistry.get(containingClass);
			if (tableDefinition.getEndRow() > screenEntityDefinition.getScreenSize().getRows()) {
				throw (new RegistryException(MessageFormat.format("Table {0} exceeds screen {1} boundaries",
						tableDefinition.getTableEntityName(), screenEntityDefinition.getScreenSize())));
			}
			List<ScreenColumnDefinition> columns = tableDefinition.getColumnDefinitions();
			for (ScreenColumnDefinition screenColumnDefinition : columns) {
				if (screenColumnDefinition.getEndColumn() > screenEntityDefinition.getScreenSize().getColumns()) {
					throw (new RegistryException(MessageFormat.format("Column {0} in table {1} exceeds screen {2} boundaries",
							screenColumnDefinition.getName(), tableDefinition.getTableEntityName(),
							screenEntityDefinition.getScreenSize())));
				}
			}
			screenEntityDefinition.getTableDefinitions().put(field.getName(), tableDefinition);
		}

	}
}
