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
package org.openlegacy.web;

import org.openlegacy.plugins.PluginsRegistry;
import org.openlegacy.plugins.support.DefaultPluginsLoader;
import org.openlegacy.plugins.support.PluginProcessor;
import org.openlegacy.utils.SpringUtil;
import org.springframework.web.context.ConfigurableWebApplicationContext;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.WebApplicationContextUtils;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;

public class OpenLegacyContextLoaderListener extends ContextLoaderListener {

	@Override
	protected void customizeContext(ServletContext servletContext, ConfigurableWebApplicationContext applicationContext) {

		PluginsRegistry pluginsRegistry = DefaultPluginsLoader.create().load().getPluginsRegistry();
		if (!pluginsRegistry.isEmpty()) {
			List<String> locations = new LinkedList<String>(Arrays.asList(applicationContext.getConfigLocations()));
			locations.addAll(pluginsRegistry.getSpringContextResources());
			applicationContext.setConfigLocations(locations.toArray(new String[] {}));

			// Note: add PluginProcessor dynamically. Not through bean declaration into spring context file
			PluginProcessor processor = new PluginProcessor(pluginsRegistry);
			applicationContext.addBeanFactoryPostProcessor(processor);
		}
	}

	@Override
	public void contextInitialized(ServletContextEvent event) {
		super.contextInitialized(event);
		SpringUtil.ApplicationContext = WebApplicationContextUtils.getRequiredWebApplicationContext(event.getServletContext());
	}

}
