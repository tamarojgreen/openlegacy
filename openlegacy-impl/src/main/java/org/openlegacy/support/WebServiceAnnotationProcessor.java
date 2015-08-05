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
import org.openlegacy.loaders.WebServicesRegistryLoader;
import org.openlegacy.loaders.WsClassAnnotationsLoader;
import org.openlegacy.loaders.WsMethodAnnotationsLoader;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;

import java.util.Collection;
import java.util.List;

public class WebServiceAnnotationProcessor implements BeanFactoryPostProcessor {

	protected List<String> loadersPackages;

	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
		Collection<WsClassAnnotationsLoader> classAnnotationsLoaders = beanFactory.getBeansOfType(WsClassAnnotationsLoader.class).values();
		Collection<WsMethodAnnotationsLoader> methodAnnotationsLoaders = beanFactory.getBeansOfType(
				WsMethodAnnotationsLoader.class).values();

		SimpleWebServicesRegistry registry = (SimpleWebServicesRegistry)beanFactory.getBean(WebServicesRegistry.class);

		filterByPackage(classAnnotationsLoaders);
		filterByPackage(methodAnnotationsLoaders);

		DefaultWebServicesRegistryLoader registryLoader = (DefaultWebServicesRegistryLoader)beanFactory.getBean(WebServicesRegistryLoader.class);
		registryLoader.setClassAnnotationsLoaders(classAnnotationsLoaders);
		registryLoader.setMethodAnnotationsLoaders(methodAnnotationsLoaders);
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

}
