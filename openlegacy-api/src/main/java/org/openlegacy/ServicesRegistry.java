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

package org.openlegacy;

import org.openlegacy.loaders.ServiceRegistryPostProcessor;
import org.openlegacy.services.definitions.ServiceDefinition;

import java.beans.PropertyChangeListener;
import java.util.List;

public interface ServicesRegistry extends PropertyChangeListener {

	/**
	 * return web service definitions list
	 */
	List<ServiceDefinition> getServices();

	/**
	 * return web service definition by name
	 */
	ServiceDefinition getServiceByName(String name);

	/**
	 * package list that will processed by registry
	 * */
	List<String> getPackages();

	/**
	 * return web service by given implementor or interface
	 */
	ServiceDefinition getServiceByClass(Class<?> clazz);

	/**
	 * return list of post processors
	 */
	List<ServiceRegistryPostProcessor> getPostProcessors();

	/**
	 * set to true in onContextInitialized
	 */
	boolean isLoaded();

	/**
	 * implement need logic and set load flag to true if all success
	 */
	void onContextInitialized();
}
