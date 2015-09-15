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

import org.openlegacy.ServicesRegistry;
import org.openlegacy.loaders.ServiceRegistryPostProcessor;
import org.openlegacy.services.definitions.ServiceDefinition;
import org.openlegacy.utils.ClassUtils;
import org.openlegacy.utils.ClassUtils.FindInClassProcessor;
import org.openlegacy.utils.SpringUtil;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SimpleServicesRegistry implements ServicesRegistry {

	private List<ServiceDefinition> webServices = new ArrayList<ServiceDefinition>();
	private List<String> packages;
	private List<ServiceRegistryPostProcessor> postProcessors = new ArrayList<ServiceRegistryPostProcessor>();
	private boolean isLoaded;

	private FindInClassProcessor process = new FindInClassProcessor() {

		@Override
		public Object process(Class<?> clazz, Object... args) {
			return (clazz == args[0]) ? true : null;
		}
	};

	@Override
	public List<ServiceDefinition> getWebServices() {
		return webServices;
	}

	@Override
	public ServiceDefinition getServiceByName(String name) {
		if (webServices != null) {
			for (ServiceDefinition def : webServices) {
				if (def.getName().equals(name)) {
					return def;
				}
			}
		}
		return null;
	}

	public List<String> getPackages() {
		return packages;
	}

	public void setPackages(List<String> packages) {
		this.packages = packages;
	}

	public void setWebServices(List<ServiceDefinition> webServices) {
		this.webServices = webServices;
	}

	@Override
	public ServiceDefinition getServiceByClass(Class<?> clazz) {
		if (webServices != null && clazz != null) {
			for (ServiceDefinition def : webServices) {
				if (def.getWebServiceClass() == clazz) {
					return def;
				}
			}

			for (ServiceDefinition def : webServices) {
				if (ClassUtils.findInClass(def.getWebServiceClass(), process, clazz) != null) {
					return def;
				}
			}
		}
		return null;
	}

	@Override
	public List<ServiceRegistryPostProcessor> getPostProcessors() {
		return postProcessors;
	}

	@Override
	public void propertyChange(PropertyChangeEvent event) {
		if (!isLoaded) {
			return;
		}

		for (ServiceRegistryPostProcessor postProcess : postProcessors) {
			postProcess.postProcess(event);
		}
	}

	@Override
	public boolean isLoaded() {
		return isLoaded;
	}

	@Override
	public void onContextInitialized() {
		Map<String, ServiceRegistryPostProcessor> postProcessors = SpringUtil.getApplicationContext().getBeansOfType(
				ServiceRegistryPostProcessor.class);
		for (String key : postProcessors.keySet()) {
			getPostProcessors().add(postProcessors.get(key));
		}

		for (ServiceRegistryPostProcessor postProcessor : getPostProcessors()) {
			postProcessor.init();
			postProcessor.postLoad();
		}
		isLoaded = true;
	}
}
