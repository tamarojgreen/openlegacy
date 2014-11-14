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
package org.openlegacy.terminal.loaders.support;

import org.openlegacy.EntitiesRegistry;
import org.openlegacy.loaders.FieldLoader;
import org.openlegacy.terminal.definitions.ScreenEntityDefinition;
import org.openlegacy.terminal.definitions.ScreenTableDefinition;
import org.openlegacy.terminal.services.ScreenEntitiesRegistry;
import org.openlegacy.utils.ReflectionUtil;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;

@Component
public class TableFieldsLoader implements FieldLoader {

	@Override
	@SuppressWarnings("rawtypes")
	public boolean match(EntitiesRegistry entitiesRegistry, Field field) {
		ScreenEntitiesRegistry screenEntitiesRegistry = (ScreenEntitiesRegistry)entitiesRegistry;
		Class<?> listType = ReflectionUtil.getListType(field);
		if (listType == null) {
			return false;
		}
		return (screenEntitiesRegistry.getTable(listType) != null);
	}

	@Override
	@SuppressWarnings("rawtypes")
	public void load(EntitiesRegistry entitiesRegistry, Field field, Class<?> containingClass, int fieldOrder) {
		ScreenEntitiesRegistry screenEntitiesRegistry = (ScreenEntitiesRegistry)entitiesRegistry;

		Class<?> listType = ReflectionUtil.getListType(field);

		ScreenTableDefinition tableDefinition = screenEntitiesRegistry.getTable(listType);
		if (tableDefinition != null) {
			ScreenEntityDefinition screenEntityDefinition = screenEntitiesRegistry.get(containingClass);
			screenEntityDefinition.getTableDefinitions().put(field.getName(), tableDefinition);
		}

	}
}
