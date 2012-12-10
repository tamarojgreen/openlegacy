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
package org.openlegacy.plugins.support;

import org.openlegacy.exceptions.OpenLegacyRuntimeException;
import org.openlegacy.plugins.PluginsRegistry;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;

public class PluginProcessor implements BeanFactoryPostProcessor {

	private PluginsRegistry pluginsRegistry;

	public PluginProcessor() {}

	public PluginProcessor(PluginsRegistry pluginsRegistry) {
		this.pluginsRegistry = pluginsRegistry;
	}

	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
		if (pluginsRegistry == null) {
			throw new OpenLegacyRuntimeException(
					"PluginsRegistry cannot be null. You must set an instance of PluginRegistry as constructor's arg.");
		}
		registerPluginsRegistryBean(beanFactory, pluginsRegistry);
	}

	/**
	 * Register PluginsRegistry in Spring web application context as bean. BeanUtils.copyProperties used for filling registered
	 * bean with <b>pluginsRegistry</b> values.
	 * 
	 * @param beanFactory
	 *            - SpringFramework bean factory
	 * @param pluginsRegistry
	 *            - OpenLegacy plugins registry
	 */
	private static void registerPluginsRegistryBean(ConfigurableListableBeanFactory beanFactory, PluginsRegistry pluginsRegistry) {
		PluginsRegistry bean = null;
		try {
			bean = beanFactory.getBean(PluginsRegistry.class);
			BeanUtils.copyProperties(pluginsRegistry, bean);
		} catch (NoSuchBeanDefinitionException e) {
			((DefaultListableBeanFactory)beanFactory).registerBeanDefinition(PluginsRegistry.PLUGINS_REGISTRY_BEAN_ID,
					BeanDefinitionBuilder.genericBeanDefinition(DefaultPluginsRegistry.class.getName()).getBeanDefinition());

			// fill plugin registry bean with values
			bean = beanFactory.getBean(PluginsRegistry.class);
			BeanUtils.copyProperties(pluginsRegistry, bean);
		}
	}

}
