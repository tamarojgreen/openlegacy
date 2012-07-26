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
package org.openlegacy.terminal.support;

import org.openlegacy.loaders.ClassAnnotationsLoader;
import org.openlegacy.loaders.FieldAnnotationsLoader;
import org.openlegacy.loaders.FieldLoader;
import org.openlegacy.loaders.RegistryLoader;
import org.openlegacy.terminal.services.ScreenEntitiesRegistry;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;

import java.util.Collection;

/**
 * Open legacy integration point with spring component-scan. The classes are scanned for @ScreenEntity annotation and all the
 * annotations information is extracted and kept in ScreenEntitiesRegistry
 * 
 * @param <T>
 */
public class ScreenAnnotationProccesor<T> implements BeanFactoryPostProcessor {

	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
		Collection<ClassAnnotationsLoader> classAnnotationsLoaders = beanFactory.getBeansOfType(ClassAnnotationsLoader.class).values();
		Collection<FieldAnnotationsLoader> fieldAnnotationLoaders = beanFactory.getBeansOfType(
				FieldAnnotationsLoader.class).values();
		Collection<FieldLoader> fieldLoaders = beanFactory.getBeansOfType(FieldLoader.class).values();

		ScreenEntitiesRegistry screenEntitiesRegistry = beanFactory.getBean(ScreenEntitiesRegistry.class);

		RegistryLoader registryLoader = beanFactory.getBean(RegistryLoader.class);
		registryLoader.load(screenEntitiesRegistry, classAnnotationsLoaders, fieldAnnotationLoaders, fieldLoaders);
	}

}
