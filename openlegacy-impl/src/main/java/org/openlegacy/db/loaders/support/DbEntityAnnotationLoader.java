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

import java.lang.annotation.Annotation;
import java.text.MessageFormat;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openlegacy.EntitiesRegistry;
import org.openlegacy.annotations.db.DbEntity;
import org.openlegacy.db.definitions.DbEntityDefinition;
import org.openlegacy.db.definitions.SimpleDbEntityDefinition;
import org.openlegacy.loaders.support.AbstractClassAnnotationLoader;
import org.springframework.stereotype.Component;

/**
 * @author Ivan Bort
 * 
 */
@Component
public class DbEntityAnnotationLoader extends AbstractClassAnnotationLoader {

	private final static Log logger = LogFactory
			.getLog(DbEntityAnnotationLoader.class);

	@Override
	public boolean match(Annotation annotation) {
		return annotation.annotationType() == DbEntity.class;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void load(EntitiesRegistry entitiesRegistry, Annotation annotation,
			Class<?> containingClass) {
		DbEntityDefinition dbEntityDefinition = (DbEntityDefinition) entitiesRegistry
				.get(containingClass);

		if (dbEntityDefinition == null) {
			// entityName should be set while loading @Entity annotation
			String entityName = containingClass.getSimpleName();
			dbEntityDefinition = new SimpleDbEntityDefinition(entityName,
					containingClass);
		}

		if (dbEntityDefinition instanceof SimpleDbEntityDefinition) {
			DbEntity dbEntity = (DbEntity) annotation;
			((SimpleDbEntityDefinition) dbEntityDefinition)
					.setDisplayName(dbEntity.displayName());
			((SimpleDbEntityDefinition) dbEntityDefinition)
					.setPluralName(dbEntity.pluralName());
			((SimpleDbEntityDefinition) dbEntityDefinition).setChild(dbEntity
					.child());
			((SimpleDbEntityDefinition) dbEntityDefinition).setWindow(dbEntity
					.window());
		}
		logger.info(MessageFormat.format(
				"DB entity \"{0}\" was added to the db registry ({1})",
				dbEntityDefinition.getEntityName(), containingClass.getName()));

		entitiesRegistry.add(dbEntityDefinition);
	}

}
