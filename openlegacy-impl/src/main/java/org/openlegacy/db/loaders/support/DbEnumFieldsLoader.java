/*******************************************************************************
 * Copyright (c) 2015 OpenLegacy Inc.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     OpenLegacy Inc. - initial API and implementation
 *******************************************************************************/
package org.openlegacy.db.loaders.support;

import org.openlegacy.EntitiesRegistry;
import org.openlegacy.db.definitions.DbEntityDefinition;
import org.openlegacy.db.definitions.SimpleDbColumnFieldDefinition;
import org.openlegacy.definitions.support.SimpleEnumFieldTypeDefinition;
import org.openlegacy.loaders.support.EnumFieldsLoader;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;

/**
 * @author Aleksey Yeremeyev
 * 
 */
@Component
public class DbEnumFieldsLoader extends EnumFieldsLoader {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void load(EntitiesRegistry entitiesRegistry, Field field, Class<?> containingClass, int fieldOrder) {
		DbEntityDefinition entityDefintion = (DbEntityDefinition) entitiesRegistry.get(containingClass);
		if (entityDefintion == null) {
			return;
		}
		SimpleDbColumnFieldDefinition fieldDefinition = (SimpleDbColumnFieldDefinition) entityDefintion.getColumnFieldsDefinitions().get(
				field.getName());
		if (fieldDefinition == null) {
			return;
		}
		fieldDefinition.setFieldTypeDefinition(new SimpleEnumFieldTypeDefinition());
		loadFieldDefinition(fieldDefinition, field);
	}
}
