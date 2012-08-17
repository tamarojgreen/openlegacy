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
package org.openlegacy.loaders.support;

import org.openlegacy.EntitiesRegistry;
import org.openlegacy.definitions.support.SimpleEnumFieldTypeDefinition;
import org.openlegacy.loaders.FieldLoader;
import org.openlegacy.support.SimpleDisplayItem;
import org.openlegacy.terminal.definitions.ScreenEntityDefinition;
import org.openlegacy.terminal.definitions.SimpleScreenFieldDefinition;
import org.openlegacy.terminal.services.ScreenEntitiesRegistry;
import org.openlegacy.utils.ReflectionUtil;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;

@Component
public class ScreenEnumFieldsLoader implements FieldLoader {

	@SuppressWarnings("rawtypes")
	public boolean match(EntitiesRegistry entitiesRegistry, Field field) {
		return field.getType().isEnum();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void load(EntitiesRegistry entitiesRegistry, Field field, Class<?> containingClass) {
		ScreenEntitiesRegistry screenEntitiesRegistry = (ScreenEntitiesRegistry)entitiesRegistry;

		ScreenEntityDefinition entityDefintion = screenEntitiesRegistry.get(containingClass);
		if (entityDefintion == null) {
			return;
		}
		SimpleScreenFieldDefinition fieldDefinition = (SimpleScreenFieldDefinition)entityDefintion.getFieldsDefinitions().get(
				field.getName());
		SimpleEnumFieldTypeDefinition enumFieldTypeDefinition = new SimpleEnumFieldTypeDefinition();
		fieldDefinition.setFieldTypeDefinition(enumFieldTypeDefinition);
		Object[] enumValues = field.getType().getEnumConstants();
		for (Object enumValue : enumValues) {
			// getValue & getDisplay are generated method for OpenLegacy enum's
			String value = (String)ReflectionUtil.invoke(enumValue, "getValue");
			String display = (String)ReflectionUtil.invoke(enumValue, "getDisplay");

			enumFieldTypeDefinition.setEnumClass((Class<? extends Enum<?>>)field.getType());
			// use the key for display item the enum value. Binding is done to the pojo. (Spring MVC binds it this way as well).
			enumFieldTypeDefinition.getEnums().put(value, new SimpleDisplayItem(enumValue, display));
		}

	}

}
