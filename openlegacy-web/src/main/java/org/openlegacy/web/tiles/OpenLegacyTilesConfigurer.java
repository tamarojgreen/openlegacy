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
package org.openlegacy.web.tiles;

import org.apache.commons.lang.ArrayUtils;
import org.openlegacy.exceptions.OpenLegacyException;
import org.openlegacy.exceptions.OpenLegacyRuntimeException;
import org.openlegacy.plugins.PluginsRegistry;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.servlet.view.tiles2.TilesConfigurer;

import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;

public class OpenLegacyTilesConfigurer extends TilesConfigurer {

	private String[] defs;

	private ServletContext servletContext;

	@Override
	public void setDefinitions(String[] definitions) {
		this.defs = mergeDefinitions(definitions);
		super.setDefinitions(this.defs);
	}

	@Override
	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
		setDefinitions(this.defs);
		processPlugins();
		super.setServletContext(servletContext);
	}

	private String[] mergeDefinitions(String[] definitions) {
		return (String[])ArrayUtils.addAll(definitions, extractDefinitionsFromPlugin());
	}

	private String[] extractDefinitionsFromPlugin() {
		if (servletContext == null) {
			return null;
		}
		WebApplicationContext wac = WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext);
		PluginsRegistry pluginsRegistry = wac.getBean(PluginsRegistry.class);

		if (pluginsRegistry.isEmpty()) {
			return null;
		}

		List<String> viewResources = pluginsRegistry.getViewDeclarations();

		return viewResources.toArray(new String[] {});
	}

	/**
	 * Extracts plugins information and sets them into ServletContext as attribute
	 */
	private void processPlugins() {
		if (servletContext == null) {
			return;
		}
		WebApplicationContext wac = WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext);
		PluginsRegistry pluginsRegistry = wac.getBean(PluginsRegistry.class);
		if (pluginsRegistry.isEmpty()) {
			return;
		}

		Map<String, String> jsHtml = pluginsRegistry.getJsHtml(servletContext.getContextPath());
		if ((jsHtml != null) && !jsHtml.isEmpty()) {
			for (String key : jsHtml.keySet()) {
				servletContext.setAttribute(key, jsHtml.get(key));
			}
		}
		Map<String, String> cssHtml = pluginsRegistry.getCssHtml(servletContext.getContextPath());
		if ((cssHtml != null) && !cssHtml.isEmpty()) {
			for (String key : cssHtml.keySet()) {
				servletContext.setAttribute(key, cssHtml.get(key));
			}
		}

		Map<String, String> menuHtml = pluginsRegistry.getMenuHtml();
		if ((menuHtml != null) && !menuHtml.isEmpty()) {
			for (String key : menuHtml.keySet()) {
				servletContext.setAttribute(key, menuHtml.get(key));
			}
		}
		try {
			pluginsRegistry.extractViews(servletContext.getRealPath("/"));
		} catch (OpenLegacyException e) {
			throw new OpenLegacyRuntimeException(e);
		}
	}

}
