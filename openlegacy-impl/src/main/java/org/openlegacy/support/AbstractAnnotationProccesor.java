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
package org.openlegacy.support;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.openlegacy.EntitiesRegistry;
import org.openlegacy.loaders.ClassAnnotationsLoader;
import org.openlegacy.loaders.FieldAnnotationsLoader;
import org.openlegacy.loaders.FieldLoader;
import org.openlegacy.loaders.RegistryLoader;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;

import java.util.Collection;
import java.util.List;

public abstract class AbstractAnnotationProccesor implements BeanFactoryPostProcessor {

	protected List<String> loadersPackages;

	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
		Collection<ClassAnnotationsLoader> classAnnotationsLoaders = beanFactory.getBeansOfType(ClassAnnotationsLoader.class).values();
		Collection<FieldAnnotationsLoader> fieldAnnotationLoaders = beanFactory.getBeansOfType(FieldAnnotationsLoader.class).values();
		Collection<FieldLoader> fieldLoaders = beanFactory.getBeansOfType(FieldLoader.class).values();

		EntitiesRegistry<?, ?> entitiesRegistry = getEntitiesRegistry(beanFactory);

		filterByPackage(classAnnotationsLoaders);
		filterByPackage(fieldAnnotationLoaders);
		filterByPackage(fieldLoaders);

		DefaultRegistryLoader registryLoader = (DefaultRegistryLoader)beanFactory.getBean(RegistryLoader.class);

		registryLoader.setAnnotationLoaders(classAnnotationsLoaders);
		registryLoader.setFieldAnnotationLoaders(fieldAnnotationLoaders);
		registryLoader.setFieldLoaders(fieldLoaders);
		registryLoader.setBeanFactory(beanFactory);

		registryLoader.load(entitiesRegistry);
	}

	private void filterByPackage(Collection<?> annotationsLoaders) {
		CollectionUtils.filter(annotationsLoaders, new Predicate() {

			public boolean evaluate(Object object) {
				return loadersPackages.contains(object.getClass().getPackage().getName());
			}
		});

	}

	protected abstract EntitiesRegistry<?, ?> getEntitiesRegistry(BeanFactory beanFactory);

	public void setLoadersPackages(List<String> loadersPackages) {
		this.loadersPackages = loadersPackages;
	}

}
