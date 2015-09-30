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

package org.openlegacy.entity.loaders.support;

import org.openlegacy.EntitiesRegistry;
import org.openlegacy.annotations.LifeCycle;
import org.openlegacy.definitions.support.AbstractEntityDefinition;
import org.openlegacy.loaders.support.AbstractClassAnnotationLoader;

import java.lang.annotation.Annotation;

public class LifeCycleAnnotationLoader extends AbstractClassAnnotationLoader {

	@Override
	public boolean match(Annotation annotation) {
		return annotation.annotationType() == LifeCycle.class;
	}

	@Override
	public void load(EntitiesRegistry entitiesRegistry, Annotation annotation, Class<?> containingClass) {
		LifeCycle lifeCycleAnnotation = (LifeCycle)annotation;
		AbstractEntityDefinition<?> entityDefinition = (AbstractEntityDefinition<?>)entitiesRegistry.get(containingClass);
		entityDefinition.setLifeCycle(lifeCycleAnnotation.value);
	}
}
