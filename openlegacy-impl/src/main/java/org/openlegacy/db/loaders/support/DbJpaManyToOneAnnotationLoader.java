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
import org.openlegacy.db.definitions.SimpleDbJoinColumnDefinition;
import org.openlegacy.db.definitions.SimpleDbManyToOneDefinition;
import org.openlegacy.loaders.FieldLoader;

import java.lang.reflect.Field;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * @author Ivan Bort
 * 
 */
public class DbJpaManyToOneAnnotationLoader implements FieldLoader {

	@SuppressWarnings("rawtypes")
	@Override
	public boolean match(EntitiesRegistry entitiesRegistry, Field field) {
		return field.getAnnotation(ManyToOne.class) != null;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void load(EntitiesRegistry entitiesRegistry, Field field, Class<?> containingClass, int fieldOrder) {
		if (field.getAnnotation(DbColumn.class) == null) {
			return;
		}
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

				JoinColumn joinColumn = field.getAnnotation(JoinColumn.class);
				if (joinColumn != null) {
					SimpleDbJoinColumnDefinition joinColumnDefinition = new SimpleDbJoinColumnDefinition();
					joinColumnDefinition.setName(joinColumn.name());
					joinColumnDefinition.setReferencedColumnName(joinColumn.referencedColumnName());
					joinColumnDefinition.setUnique(joinColumn.unique());
					joinColumnDefinition.setNullable(joinColumn.nullable());
					joinColumnDefinition.setInsertable(joinColumn.insertable());
					joinColumnDefinition.setUpdatable(joinColumn.updatable());
					joinColumnDefinition.setColumnDefinition(joinColumn.columnDefinition());
					joinColumnDefinition.setTable(joinColumn.table());

					simpleDbManyToOne.setDbJoinColumnDefinition(joinColumnDefinition);
				}

				SimpleDbColumnFieldDefinition columnFieldDefinition = (SimpleDbColumnFieldDefinition)dbFieldDefinition;
				columnFieldDefinition.setManyToOneDefinition(simpleDbManyToOne);
			}
			dbEntityDefinition.getColumnFieldsDefinitions().put(field.getName(), dbFieldDefinition);
		}

	}

}
