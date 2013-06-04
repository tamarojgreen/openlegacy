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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openlegacy.EntitiesRegistry;
import org.openlegacy.definitions.EnumGetValue;
import org.openlegacy.definitions.support.SimpleEnumFieldTypeDefinition;
import org.openlegacy.exceptions.RegistryException;
import org.openlegacy.loaders.FieldLoader;
import org.openlegacy.support.SimpleDisplayItem;
import org.openlegacy.terminal.definitions.ScreenEntityDefinition;
import org.openlegacy.terminal.definitions.SimpleScreenFieldDefinition;
import org.openlegacy.terminal.services.ScreenEntitiesRegistry;
import org.openlegacy.utils.ReflectionUtil;
import org.openlegacy.utils.StringUtil;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.text.MessageFormat;

@Component
public class ScreenEnumFieldsLoader implements FieldLoader {

	private final static Log logger = LogFactory.getLog(ScreenEnumFieldsLoader.class);

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
		for (int i = 0; i < enumValues.length; i++) {
			Object enumValue = enumValues[i];
			// getValue & toString() are generated method for OpenLegacy enum's
			String value = null;
			Class<? extends Enum<?>> enumClass = (Class<? extends Enum<?>>)field.getType();
			try {
				if (EnumGetValue.class.isAssignableFrom(enumClass)) {
					value = (String)ReflectionUtil.invoke(enumValue, "getValue");
				} else {
					// use 1 based value - very common
					int ordinal = i + 1;
					logger.warn(MessageFormat.format(
							"Loading enum ordinal value {0} for {1}.{2}. Implement EnumGetValue if you wish to control the assigment",
							ordinal, enumClass.getSimpleName(), enumValue));
					value = String.valueOf(ordinal);
				}
			} catch (Exception e) {
				throw (new RegistryException("Enum fields should contains a getValue() method that represent the host values", e));
			}
			String display = enumValue.toString();
			try {
				// is toString implemented for the enum
				enumClass.getDeclaredMethod("toString");
			} catch (Exception e) {
				display = StringUtil.toDisplayName(enumValue.toString());
			}

			if (value == null) {
				throw (new RegistryException(
						MessageFormat.format("Enum field {0}.getValue() cannot be null", enumClass.getName())));
			}
			enumFieldTypeDefinition.setEnumClass(enumClass);
			// use the key for display item the enum value. Binding is done to the pojo. (Spring MVC binds it this way as well).
			enumFieldTypeDefinition.getEnums().put(value, new SimpleDisplayItem(enumValue, display));
		}

	}
}
