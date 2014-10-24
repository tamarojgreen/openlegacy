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
import org.openlegacy.annotations.db.DbNavigation;
import org.openlegacy.db.definitions.DbEntityDefinition;
import org.openlegacy.db.definitions.SimpleDbNavigationDefinition;
import org.openlegacy.loaders.support.AbstractClassAnnotationLoader;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.lang.annotation.Annotation;
import java.text.MessageFormat;

/**
 * @author Ivan Bort
 * 
 */
@Component
public class DbNavigationAnnotationLoader extends AbstractClassAnnotationLoader {

	public boolean match(Annotation annotation) {
		return annotation.annotationType() == DbNavigation.class;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void load(EntitiesRegistry entitiesRegistry, Annotation annotation, Class<?> containingClass) {

		DbEntityDefinition dbEntityDefinition = (DbEntityDefinition)entitiesRegistry.get(containingClass);
		Assert.notNull(dbEntityDefinition, MessageFormat.format(
				"Db entity definition for class {0} not found. Verify @DbNavigation is defined along @DbEntity annotation",
				containingClass.getName()));

		DbNavigation dbNavigation = (DbNavigation)annotation;

		SimpleDbNavigationDefinition dbNavigationDefinition = (SimpleDbNavigationDefinition)dbEntityDefinition.getNavigationDefinition();
		dbNavigationDefinition.setCategory(dbNavigation.category());
	}

}
