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

import org.openlegacy.rpc.RpcConnectionFactory;
import org.openlegacy.utils.ProxyUtil;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.remoting.httpinvoker.CommonsHttpInvokerRequestExecutor;
import org.springframework.remoting.httpinvoker.HttpInvokerProxyFactoryBean;
import org.springframework.stereotype.Component;

@Component
public class SecuredGatewayPostProcessor implements BeanPostProcessor {

	@Autowired(required = false)
	SecuredGatewayProperties properties;

	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		return bean;
	}

	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		if (properties.getHost() != null) {
			if (properties.getConnectionFactory().isAssignableFrom(bean.getClass())) {
				Object remote = getSecuredGatewayConnectionFactory();
				if (remote != null) {
					ProxyUtil.registerProxyClassPartity(bean.getClass(), remote.getClass());
					return remote;
				}
			}
		}
		return bean;
	}

	private Object getSecuredGatewayConnectionFactory() {
		try {
			HttpInvokerProxyFactoryBean factoryBean = new HttpInvokerProxyFactoryBean();
			factoryBean.setServiceInterface(properties.getConnectionFactoryInterface());
			factoryBean.setHttpInvokerRequestExecutor(new CommonsHttpInvokerRequestExecutor());
			factoryBean.setServiceUrl(properties.getRemoteConnectionFactoryUrl());
			factoryBean.afterPropertiesSet();
			return (RpcConnectionFactory)factoryBean.getObject();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}
