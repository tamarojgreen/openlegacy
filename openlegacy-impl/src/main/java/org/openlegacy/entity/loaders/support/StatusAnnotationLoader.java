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
import org.openlegacy.annoations.entity.support.Status;
import org.openlegacy.annotations.entity.Active;
import org.openlegacy.annotations.entity.Invalid;
import org.openlegacy.annotations.entity.Paused;
import org.openlegacy.definitions.support.AbstractEntityDefinition;
import org.openlegacy.loaders.support.AbstractClassAnnotationLoader;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StatusAnnotationLoader extends AbstractClassAnnotationLoader {

	@SuppressWarnings("unchecked")
	private static final List<Class<? extends Annotation>> SUPPORTED = new ArrayList<Class<? extends Annotation>>(Arrays.asList(
			Active.class, Deprecated.class, Invalid.class, Paused.class));

	@Override
	public boolean match(Annotation annotation) {
		return SUPPORTED.contains(annotation.annotationType());
	}

	@Override
	public void load(EntitiesRegistry entitiesRegistry, Annotation annotation, Class<?> containingClass) {
		AbstractEntityDefinition<?> entityDefinition = (AbstractEntityDefinition<?>)entitiesRegistry.get(containingClass);
		Status status = entityDefinition.getStatus();
		if (!status.isDefault()) {
			return;
		}
		switch (SUPPORTED.indexOf(annotation.annotationType())) {
			case 0:
				status = Status.ACTIVE;
				break;
			case 1:
				status = Status.DEPRECATED;
				break;
			case 2:
				status = Status.INVALID;
				break;
			case 3:
				status = Status.PAUSED;
				break;
		}
		entityDefinition.setStatus(status);
	}
}
