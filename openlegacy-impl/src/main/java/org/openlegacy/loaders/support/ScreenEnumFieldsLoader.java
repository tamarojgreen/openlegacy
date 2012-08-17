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
import org.openlegacy.exceptions.OpenLegacyRuntimeException;
import org.openlegacy.loaders.FieldLoader;
import org.openlegacy.support.SimpleDisplayItem;
import org.openlegacy.terminal.definitions.ScreenEntityDefinition;
import org.openlegacy.terminal.definitions.SimpleScreenFieldDefinition;
import org.openlegacy.terminal.services.ScreenEntitiesRegistry;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;

@Component
public class ScreenEnumFieldsLoader implements FieldLoader {

	@SuppressWarnings("rawtypes")
	public boolean match(EntitiesRegistry entitiesRegistry, Field field) {
		return field.getType().isEnum();
	}

	@SuppressWarnings("rawtypes")
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
			String value = invokeMethod(enumValue, "getValue");
			String display = invokeMethod(enumValue, "getDisplay");
			// getKey & getValue are generated method for OpenLegacy enum's

			enumFieldTypeDefinition.getAsMap().put(enumValue.toString(), new SimpleDisplayItem(value, display));
		}

	}

	private static String invokeMethod(Object object, String methodName) {
		try {
			return (String)object.getClass().getMethod(methodName).invoke(object);
		} catch (Exception e) {
			throw (new OpenLegacyRuntimeException(e));
		}
	}
}
