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
package org.openlegacy.terminal.loaders.support;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openlegacy.EntitiesRegistry;
import org.openlegacy.annotations.screen.ScreenBinder;
import org.openlegacy.exceptions.RegistryException;
import org.openlegacy.loaders.support.AbstractClassAnnotationLoader;
import org.openlegacy.terminal.ScreenEntityBinder;
import org.openlegacy.terminal.definitions.SimpleScreenEntityDefinition;
import org.openlegacy.terminal.services.ScreenEntitiesRegistry;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

@Component
public class ScreenBinderAnnotationLoader extends AbstractClassAnnotationLoader {

	private final static Log logger = LogFactory.getLog(ScreenBinderAnnotationLoader.class);

	public boolean match(Annotation annotation) {
		return annotation.annotationType() == ScreenBinder.class;
	}

	@SuppressWarnings("rawtypes")
	public void load(EntitiesRegistry entitiesRegistry, Annotation annotation, Class<?> containingClass) {

		ScreenBinder screenBinder = (ScreenBinder)annotation;
		ScreenEntitiesRegistry screenEntitiesRegistry = (ScreenEntitiesRegistry)entitiesRegistry;

		SimpleScreenEntityDefinition screenEntityDefinition = (SimpleScreenEntityDefinition)screenEntitiesRegistry.get(containingClass);

		List<ScreenEntityBinder> binders = new ArrayList<ScreenEntityBinder>();
		Class<? extends ScreenEntityBinder>[] binderClasses = screenBinder.binders();
		for (Class<? extends ScreenEntityBinder> class1 : binderClasses) {
			try {
				binders.add(class1.newInstance());
			} catch (Exception e) {
				throw (new RegistryException(e));
			}
		}
		screenEntityDefinition.setBinders(binders);
		screenEntityDefinition.setPerformDefaultBinding(screenBinder.performDefaultBinding());

		logger.info("Added screen binders information to the registry for:" + containingClass);
	}
}
