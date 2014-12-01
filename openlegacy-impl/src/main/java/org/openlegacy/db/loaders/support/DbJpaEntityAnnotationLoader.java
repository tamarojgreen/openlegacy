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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openlegacy.EntitiesRegistry;
import org.openlegacy.db.definitions.DbEntityDefinition;
import org.openlegacy.db.definitions.SimpleDbEntityDefinition;
import org.openlegacy.db.services.DbEntitiesRegistry;
import org.openlegacy.loaders.support.AbstractClassAnnotationLoader;
import org.openlegacy.utils.StringUtil;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.text.MessageFormat;

import javax.persistence.Entity;

@Component
@Order(1)
public class DbJpaEntityAnnotationLoader extends AbstractClassAnnotationLoader {

	private final static Log logger = LogFactory.getLog(DbJpaEntityAnnotationLoader.class);

	@Override
	public boolean match(Annotation annotation) {
		return annotation.annotationType() == Entity.class;
	}

	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void load(EntitiesRegistry entitiesRegistry, Annotation annotation, Class<?> containingClass) {

		Entity entity = (Entity)annotation;
		DbEntitiesRegistry dbEntitiesRegistry = (DbEntitiesRegistry)entitiesRegistry;

		String entityName = entity.name().length() > 0 ? entity.name() : containingClass.getSimpleName();

		DbEntityDefinition dbEntityDefinition = (DbEntityDefinition)entitiesRegistry.get(containingClass);
		if (dbEntityDefinition == null) {
			String displayName = StringUtil.toDisplayName(entityName);
			dbEntityDefinition = new SimpleDbEntityDefinition(entityName, containingClass);
			((SimpleDbEntityDefinition)dbEntityDefinition).setDisplayName(displayName);
		}
		if (dbEntityDefinition instanceof SimpleDbEntityDefinition) {
			((SimpleDbEntityDefinition)dbEntityDefinition).setEntityName(entityName);
		}
		logger.info(MessageFormat.format("DB entity \"{0}\" was added to the db registry ({1})", entityName,
				containingClass.getName()));

		dbEntitiesRegistry.add(dbEntityDefinition);
	}
}
