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
	public static final List<Class<? extends Annotation>> SUPPORTED = new ArrayList<Class<? extends Annotation>>(Arrays.asList(
			Active.class, Deprecated.class, Invalid.class, Paused.class));

	public static final List<String> SUPPORTED_NAMES = new ArrayList<String>(Arrays.asList(Active.class.getSimpleName(),
			Deprecated.class.getSimpleName(), Invalid.class.getSimpleName(), Paused.class.getSimpleName()));

	@Override
	public boolean match(Annotation annotation) {
		return SUPPORTED.contains(annotation.annotationType());
	}

	@Override
	public void load(EntitiesRegistry entitiesRegistry, Annotation annotation, Class<?> containingClass) {
		AbstractEntityDefinition<?> entityDefinition = (AbstractEntityDefinition<?>)entitiesRegistry.get(containingClass);
		setEntityStatus(entityDefinition, annotation);
	}

	public static void setEntityStatus(AbstractEntityDefinition<?> entityDef, Annotation annotation) {
		Status status = entityDef.getStatus();
		if (!status.isDefault()) {
			return;
		}
		entityDef.setStatus(getStatus(SUPPORTED.indexOf(annotation.annotationType())));
	}

	public static void setEntityStatus(AbstractEntityDefinition<?> entityDef, String annotation) {
		Status status = entityDef.getStatus();
		if (!status.isDefault()) {
			return;
		}
		entityDef.setStatus(getStatus(SUPPORTED_NAMES.indexOf(annotation)));
	}

	public static Status getStatus(int idx) {
		switch (idx) {
			case 0:
				return Status.ACTIVE;
			case 1:
				return Status.DEPRECATED;
			case 2:
				return Status.INVALID;
			case 3:
				return Status.PAUSED;
			default:
				return Status.DEFAULT;
		}
	}
}
