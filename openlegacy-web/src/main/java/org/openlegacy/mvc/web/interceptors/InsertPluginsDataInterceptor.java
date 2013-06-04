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
package org.openlegacy.mvc.web.interceptors;

import org.openlegacy.plugins.Plugin;
import org.openlegacy.plugins.PluginsRegistry;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Intercepter class for spring MVC. Injects commonly used beans into the page context so they can be accessed via the web page
 * 
 * @author Imivan
 * 
 */
public class InsertPluginsDataInterceptor extends HandlerInterceptorAdapter {

	@Inject
	private PluginsRegistry pluginsRegistry;

	public InsertPluginsDataInterceptor() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView)
			throws Exception {

		if (modelAndView == null) {
			return;
		}

		if ((modelAndView.getViewName().startsWith("redirect"))) {
			return;
		}

		insertPluginsData(modelAndView);
		pluginsRegistry.extractViews(request.getSession().getServletContext().getRealPath("/"));
	}

	private void insertPluginsData(ModelAndView model) {
		List<String> cssList = new ArrayList<String>();
		List<String> jsList = new ArrayList<String>();

		Map<String, List<Map<String, String>>> menuMap = new HashMap<String, List<Map<String, String>>>();

		List<Plugin> plugins = this.pluginsRegistry.getPlugins();

		for (Plugin plugin : plugins) {
			List<String> cssItems = plugin.getCssItems();
			for (String cssItem : cssItems) {
				cssList.add(MessageFormat.format("{0}{1}", plugin.getCssPath(), cssItem));
			}

			List<String> jsItems = plugin.getJsItems();
			for (String jsItem : jsItems) {
				jsList.add(MessageFormat.format("{0}{1}", plugin.getJsPath(), jsItem));
			}

			List<Map<String, String>> menuItems = plugin.getMenuItems();

			for (Map<String, String> map : menuItems) {
				List<Map<String, String>> categoryValues = null;
				if (!menuMap.containsKey(map.get(Plugin.MENU_CATEGORY))) {
					categoryValues = new ArrayList<Map<String, String>>();
				} else {
					categoryValues = menuMap.get(map.get(Plugin.MENU_CATEGORY));
				}
				categoryValues.add(map);
				menuMap.put(map.get(Plugin.MENU_CATEGORY), categoryValues);
			}
		}

		model.addObject("plugins_css", cssList);
		model.addObject("plugins_js", jsList);
		model.addObject("plugins_menu", menuMap);
	}

}
