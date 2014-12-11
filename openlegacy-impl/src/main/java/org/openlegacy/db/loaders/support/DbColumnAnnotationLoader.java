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

package org.openlegacy.db.loaders.support;

import org.openlegacy.EntitiesRegistry;
import org.openlegacy.FieldType;
import org.openlegacy.annotations.db.DbColumn;
import org.openlegacy.db.definitions.DbEntityDefinition;
import org.openlegacy.db.definitions.DbFieldDefinition;
import org.openlegacy.db.definitions.SimpleDbColumnFieldDefinition;
import org.openlegacy.loaders.FieldLoader;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * @author Ivan Bort
 * 
 */
@Component
public class DbColumnAnnotationLoader implements FieldLoader {

	@SuppressWarnings("rawtypes")
	@Override
	public boolean match(EntitiesRegistry entitiesRegistry, Field field) {
		return field.getAnnotation(DbColumn.class) != null;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void load(EntitiesRegistry entitiesRegistry, Field field, Class<?> containingClass, int fieldOrder) {
		DbEntityDefinition dbEntityDefinition = (DbEntityDefinition)entitiesRegistry.get(containingClass);
		if (dbEntityDefinition != null) {
			DbFieldDefinition dbFieldDefinition = dbEntityDefinition.getColumnFieldsDefinitions().get(field.getName());
			if (dbFieldDefinition == null) {
				dbFieldDefinition = new SimpleDbColumnFieldDefinition(field.getName(), FieldType.General.class);
			}
			if (dbFieldDefinition instanceof SimpleDbColumnFieldDefinition) {
				DbColumn column = field.getAnnotation(DbColumn.class);
				SimpleDbColumnFieldDefinition columnFieldDefinition = (SimpleDbColumnFieldDefinition)dbFieldDefinition;
				columnFieldDefinition.setDisplayName(column.displayName());
				columnFieldDefinition.setEditable(column.editable());
				columnFieldDefinition.setPassword(column.password());
				columnFieldDefinition.setSampleValue(column.sampleValue());
				columnFieldDefinition.setDefaultValue(column.defaultValue());
				columnFieldDefinition.setHelpText(column.helpText());
				columnFieldDefinition.setRightToLeft(column.rightToLeft());
				columnFieldDefinition.setInternal(column.internal());
				columnFieldDefinition.setMainDisplayField(column.mainDisplayField());
				Type genericFieldType = field.getGenericType();

				if (genericFieldType instanceof ParameterizedType) {
					ParameterizedType aType = (ParameterizedType)genericFieldType;
					Type[] fieldArgTypes = aType.getActualTypeArguments();
					columnFieldDefinition.setJavaType((Class)fieldArgTypes[fieldArgTypes.length - 1]);

				}

			}
			dbEntityDefinition.getColumnFieldsDefinitions().put(field.getName(), dbFieldDefinition);
		}
	}
}
