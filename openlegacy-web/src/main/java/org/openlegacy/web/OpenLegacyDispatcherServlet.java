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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openlegacy.plugins.PluginsRegistry;
import org.openlegacy.plugins.support.DefaultPluginsLoader;
import org.springframework.web.context.ConfigurableWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class OpenLegacyDispatcherServlet extends DispatcherServlet {

	private static Log logger = LogFactory.getLog(OpenLegacyDispatcherServlet.class);

	private static final long serialVersionUID = 9131827894188300344L;

	@Override
	protected void configureAndRefreshWebApplicationContext(ConfigurableWebApplicationContext wac) {
		PluginsRegistry pluginsRegistry = DefaultPluginsLoader.create().load().getPluginsRegistry();

		if (!pluginsRegistry.isEmpty()) {
			List<String> locations = new LinkedList<String>(Arrays.asList(wac.getConfigLocations()));
			locations.addAll(pluginsRegistry.getSpringWebContextResources());
			wac.setConfigLocations(locations.toArray(new String[] {}));
			if (logger.isDebugEnabled()) {
				logger.debug("Modified config locations of web application context");
			}
		}

		super.configureAndRefreshWebApplicationContext(wac);
	}

}
