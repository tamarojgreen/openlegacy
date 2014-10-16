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
import org.openlegacy.db.definitions.SimpleDbFieldDefinition;
import org.openlegacy.db.services.DbEntitiesRegistry;
import org.openlegacy.loaders.FieldLoader;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;

@Component
public class DbFieldsLoader implements FieldLoader {

	@SuppressWarnings("rawtypes")
	public boolean match(EntitiesRegistry entitiesRegistry, Field field) {
		return true;
	}

	@SuppressWarnings("rawtypes")
	public void load(EntitiesRegistry entitiesRegistry, Field field, Class<?> containingClass, int fieldOrder) {
		DbEntitiesRegistry dbEntitiesRegistry = (DbEntitiesRegistry)entitiesRegistry;

		DbEntityDefinition dbEntityDefinition = dbEntitiesRegistry.get(containingClass);

		SimpleDbFieldDefinition dbFieldDefinition = new SimpleDbFieldDefinition(field.getName());
		dbEntityDefinition.getFieldsDefinitions().put(field.getName(), dbFieldDefinition);

	}
}
