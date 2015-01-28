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
import org.openlegacy.EntityDefinition;
import org.openlegacy.FieldType;
import org.openlegacy.db.definitions.DbEntityDefinition;
import org.openlegacy.db.definitions.DbFieldDefinition;
import org.openlegacy.db.definitions.SimpleDbColumnFieldDefinition;
import org.openlegacy.db.definitions.SimpleDbManyToOneDefinition;
import org.openlegacy.loaders.FieldLoader;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import javax.persistence.ManyToOne;

/**
 * @author Ivan Bort
 * 
 */
@Component
public class DbJpaManyToOneAnnotationLoader implements FieldLoader {

	@SuppressWarnings("rawtypes")
	@Override
	public boolean match(EntitiesRegistry entitiesRegistry, Field field) {
		return field.getAnnotation(ManyToOne.class) != null;
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
				ManyToOne manyToOne = field.getAnnotation(ManyToOne.class);

				SimpleDbManyToOneDefinition simpleDbManyToOne = new SimpleDbManyToOneDefinition();
				simpleDbManyToOne.setTargetEntity(manyToOne.targetEntity());
				simpleDbManyToOne.setTargetEntityClassName(manyToOne.targetEntity().getSimpleName());
				simpleDbManyToOne.setCascade(manyToOne.cascade());
				simpleDbManyToOne.setFetch(manyToOne.fetch());
				simpleDbManyToOne.setOptional(manyToOne.optional());

				((SimpleDbColumnFieldDefinition)dbFieldDefinition).setManyToOneDefinition(simpleDbManyToOne);
			}
			dbEntityDefinition.getColumnFieldsDefinitions().put(field.getName(), dbFieldDefinition);

			Type genericFieldType = field.getGenericType();
			if (genericFieldType instanceof ParameterizedType) {
				ParameterizedType pType = (ParameterizedType)genericFieldType;
				Type[] fieldArgTypes = pType.getActualTypeArguments();
				Class actualClass = (Class)fieldArgTypes[fieldArgTypes.length - 1];
				EntityDefinition entityDefinition = entitiesRegistry.get(actualClass);
				if (entityDefinition != null) {
					dbEntityDefinition.getChildEntitiesDefinitions().add(entityDefinition);
				}
			}
		}

	}

}
