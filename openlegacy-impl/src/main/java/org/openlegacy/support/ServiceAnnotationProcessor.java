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

package org.openlegacy.support;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.openlegacy.WebServicesRegistry;
import org.openlegacy.loaders.ServiceClassAnnotationsLoader;
import org.openlegacy.loaders.ServiceMethodAnnotationsLoader;
import org.openlegacy.loaders.ServiceMethodParamLoader;
import org.openlegacy.loaders.ServicesRegistryLoader;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;

import java.util.Collection;
import java.util.List;

public class ServiceAnnotationProcessor implements BeanFactoryPostProcessor {

	private static BeanFactory beanFactory;
	protected List<String> loadersPackages;

	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
		ServiceAnnotationProcessor.beanFactory = beanFactory;

		Collection<ServiceClassAnnotationsLoader> classAnnotationsLoaders = beanFactory.getBeansOfType(
				ServiceClassAnnotationsLoader.class).values();
		Collection<ServiceMethodAnnotationsLoader> methodAnnotationsLoaders = beanFactory.getBeansOfType(
				ServiceMethodAnnotationsLoader.class).values();
		Collection<ServiceMethodParamLoader> methodParamloaders = beanFactory.getBeansOfType(ServiceMethodParamLoader.class).values();

		SimpleServicesRegistry registry = (SimpleServicesRegistry)beanFactory.getBean(WebServicesRegistry.class);

		filterByPackage(classAnnotationsLoaders);
		filterByPackage(methodAnnotationsLoaders);
		filterByPackage(methodParamloaders);

		DefaultServicesRegistryLoader registryLoader = (DefaultServicesRegistryLoader)beanFactory.getBean(ServicesRegistryLoader.class);
		registryLoader.setClassAnnotationsLoaders(classAnnotationsLoaders);
		registryLoader.setMethodAnnotationsLoaders(methodAnnotationsLoaders);
		registryLoader.setMethodParamLoaders(methodParamloaders);
		registryLoader.load(registry);
	}

	private void filterByPackage(Collection<?> annotationsLoaders) {
		CollectionUtils.filter(annotationsLoaders, new Predicate() {

			@Override
			public boolean evaluate(Object object) {
				return loadersPackages.contains(object.getClass().getPackage().getName());
			}
		});
	}

	public void setLoadersPackages(List<String> loadersPackages) {
		this.loadersPackages = loadersPackages;
	}

	public static BeanFactory getBeanFactory() {
		return beanFactory;
	}

}
