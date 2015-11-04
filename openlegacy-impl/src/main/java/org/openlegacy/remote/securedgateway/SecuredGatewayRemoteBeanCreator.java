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

package org.openlegacy.remote.securedgateway;

import org.springframework.beans.BeansException;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.config.TypedStringValue;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.core.annotation.Order;
import org.springframework.remoting.httpinvoker.HttpInvokerServiceExporter;

import java.util.HashMap;
import java.util.Map;

@Order(10)
public class SecuredGatewayRemoteBeanCreator implements BeanFactoryPostProcessor {

	private static final String SERVICE = "service";
	private static final String SERVICE_INTERFACE = SERVICE + "Interface";

	SecuredGatewayProperties properties;

	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
		properties = (SecuredGatewayProperties)beanFactory.getBean("securedGatewayProperties");
		// createRemoteBeanDefinition(beanFactory, SecuredGatewayUtils.getBeanNameForType(properties.getConnectionFactory()));
	}

	protected void createRemoteBeanDefinition(ConfigurableListableBeanFactory beanFactory, String beanName) {
		GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
		String remoteBeanName = properties.getRemoteConnectionFactoryBeanName();
		beanDefinition.setBeanClassName(HttpInvokerServiceExporter.class.getName());
		Map<String, Object> props = new HashMap<String, Object>();
		props.put(SERVICE, new RuntimeBeanReference(beanName));
		props.put(SERVICE_INTERFACE, new TypedStringValue(properties.getConnectionFactoryInterface().getName()));
		beanDefinition.setPropertyValues(new MutablePropertyValues(props));

		((DefaultListableBeanFactory)beanFactory).registerBeanDefinition("/" + remoteBeanName, beanDefinition);
	}
}
