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
import org.openlegacy.annotations.db.DbActions;
import org.openlegacy.loaders.support.AbstractClassAnnotationLoader;

import java.lang.annotation.Annotation;

/**
 * @author Ivan Bort
 * 
 */
public class DbActionsAnnotationLoader extends AbstractClassAnnotationLoader {

	public boolean match(Annotation annotation) {
		return annotation.annotationType() == DbActions.class;
	}

	@SuppressWarnings("rawtypes")
	public void load(EntitiesRegistry entitiesRegistry, Annotation annotation, Class<?> containingClass) {

	}

}
