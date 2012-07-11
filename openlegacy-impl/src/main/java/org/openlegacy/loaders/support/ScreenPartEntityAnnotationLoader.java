/*******************************************************************************
 * Copyright (c) 2012 OpenLegacy Inc.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     OpenLegacy Inc. - initial API and implementation
 *******************************************************************************/
package org.openlegacy.loaders.support;

import org.openlegacy.EntitiesRegistry;
import org.openlegacy.annotations.screen.ScreenPart;
import org.openlegacy.terminal.definitions.SimpleScreenPartEntityDefinition;
import org.openlegacy.terminal.spi.ScreenEntitiesRegistry;
import org.openlegacy.utils.StringUtil;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;

@Component
@Order(2)
public class ScreenPartEntityAnnotationLoader extends AbstractClassAnnotationLoader {

	public boolean match(Annotation annotation) {
		return annotation.annotationType() == ScreenPart.class;
	}

	@SuppressWarnings("rawtypes")
	public void load(EntitiesRegistry entitiesRegistry, Annotation annotation, Class<?> containingClass) {

		ScreenPart screenPartAnnotation = (ScreenPart)annotation;
		ScreenEntitiesRegistry screenEntitiesRegistry = (ScreenEntitiesRegistry)entitiesRegistry;

		SimpleScreenPartEntityDefinition screenPartEntityDefinition = new SimpleScreenPartEntityDefinition(containingClass);
		String name = screenPartAnnotation.name().length() > 0 ? screenPartAnnotation.name()
				: StringUtil.toJavaFieldName(containingClass.getSimpleName());
		screenPartEntityDefinition.setPartName(name);
		screenEntitiesRegistry.addPart(screenPartEntityDefinition);
	}
}
