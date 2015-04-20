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
import org.openlegacy.db.definitions.DbEntityDefinition;
import org.openlegacy.db.definitions.DbFieldDefinition;
import org.openlegacy.db.definitions.SimpleDbColumnFieldDefinition;
import org.openlegacy.db.definitions.SimpleDbFieldDefinition;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import javax.persistence.Column;

/**
 * @author Ivan Bort
 * 
 */
@Component
public class DbJpaColumnAnnotationLoader extends DbFieldsLoader {

	@SuppressWarnings("rawtypes")
	@Override
	public boolean match(EntitiesRegistry entitiesRegistry, Field field) {
		return field.getAnnotation(Column.class) != null;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void load(EntitiesRegistry entitiesRegistry, Field field, Class<?> containingClass, int fieldOrder) {
		DbEntityDefinition dbEntityDefinition = (DbEntityDefinition)entitiesRegistry.get(containingClass);
		if (dbEntityDefinition != null) {
			DbFieldDefinition dbFieldDefinition = dbEntityDefinition.getColumnFieldsDefinitions().get(field.getName());
			if (dbFieldDefinition == null) {
				dbFieldDefinition = createFrom(field,
						(SimpleDbFieldDefinition)dbEntityDefinition.getFieldsDefinitions().get(field.getName()));
			}
			if (dbFieldDefinition instanceof SimpleDbColumnFieldDefinition) {
				Column column = field.getAnnotation(Column.class);
				SimpleDbColumnFieldDefinition columnFieldDefinition = (SimpleDbColumnFieldDefinition)dbFieldDefinition;
				columnFieldDefinition.setNameAttr(column.name());
				columnFieldDefinition.setUnique(column.unique());
				columnFieldDefinition.setNullable(column.nullable());
				columnFieldDefinition.setInsertable(column.insertable());
				columnFieldDefinition.setUpdatable(column.updatable());
				columnFieldDefinition.setColumnDefinition(column.columnDefinition());
				columnFieldDefinition.setLength(column.length());
				columnFieldDefinition.setPrecision(column.precision());
				columnFieldDefinition.setScale(column.scale());
				
				Type genericFieldType = field.getGenericType();

				if (genericFieldType instanceof ParameterizedType) {
					ParameterizedType aType = (ParameterizedType)genericFieldType;
					Type[] fieldArgTypes = aType.getActualTypeArguments();
					columnFieldDefinition.setJavaType((Class)fieldArgTypes[fieldArgTypes.length - 1]);
					columnFieldDefinition.setJavaTypeName(((Class)fieldArgTypes[fieldArgTypes.length - 1]).getSimpleName());
				} else {
					columnFieldDefinition.setJavaType(field.getType());
					columnFieldDefinition.setJavaTypeName(field.getType().getSimpleName());
				}
			}
			dbEntityDefinition.getColumnFieldsDefinitions().put(field.getName(), dbFieldDefinition);
		}
	}

}
