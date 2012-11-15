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

import org.openlegacy.exceptions.OpenLegacyException;
import org.openlegacy.plugins.Plugin;
import org.openlegacy.plugins.PluginsRegistry;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple implementation of plugins registry. Is responsible for storing plugins information.
 * 
 * @author Imivan
 */

public class DefaultPluginsRegistry implements PluginsRegistry {

	private Map<String, Plugin> cachePlugins = new HashMap<String, Plugin>();

	public DefaultPluginsRegistry() {}

	/**
	 * Add plugin to registry
	 * 
	 * @param plugin
	 *            - OpenLegacy plugin
	 */
	public void addPlugin(Plugin plugin) throws OpenLegacyException {
		if (getCachePlugins().containsKey(plugin.getName())) {
			throw new OpenLegacyException(MessageFormat.format(
					"Cannot add plugin to registry. Default plugin registry already contains a plugin with name: {0}",
					plugin.getName()));
		}
		getCachePlugins().put(plugin.getName(), plugin);
	}

	/**
	 * Clear registry
	 */
	public void clear() {
		this.cachePlugins.clear();
	}

	/**
	 * Extract views from plugin resources to parent project.
	 * 
	 * @param rootPath
	 *            - the absolute path which used like root path for storing views inside parent project
	 */
	public void extractViews(String rootPath) throws OpenLegacyException {
		if ((rootPath == null) || (rootPath.isEmpty())) {
			throw new OpenLegacyException("Plugin cannot be loaded. Cannot extract view files. RootPath is empty.");
		}

		try {
			for (Plugin plugin : this.cachePlugins.values()) {
				List<String> views = plugin.getViews();
				for (String view : views) {
					// extract filename
					int filenameBeginIndex = view.lastIndexOf("/") != -1 ? view.lastIndexOf("/") : 0;
					String viewFileName = view.substring(filenameBeginIndex, view.length());
					if (viewFileName.startsWith("/")) {
						viewFileName = viewFileName.replaceFirst("/", "");
					}
					// extract file path
					String filePath = view.replace(viewFileName, "").replace(plugin.getExcludedViewPathPart(), "");
					if (filePath.startsWith("/")) {
						filePath = filePath.replaceFirst("/", "");
					}
					if (filePath.endsWith("/")) {
						filePath = filePath.substring(0, filePath.lastIndexOf("/"));
					}

					// remove leading slash from view path if present
					if (view.startsWith("/")) {
						view = view.replaceFirst("/", "");
					}
					URL resource = getClass().getClassLoader().getResource(view);
					if (resource == null) {
						throw new FileNotFoundException(view);
					}
					File file = new File(MessageFormat.format("{0}/{1}/{2}/{3}/{4}", rootPath, "WEB-INF", filePath, "plugins",
							plugin.getName()));
					if (!file.exists()) {
						file.mkdirs();
					}
					InputStream in = resource.openStream();
					FileOutputStream fos = new FileOutputStream(MessageFormat.format("{0}/{1}", file.getAbsolutePath(),
							viewFileName));
					byte[] buf = new byte[1024];
					int len;
					while ((len = in.read(buf)) > 0) {
						fos.write(buf, 0, len);
					}
					in.close();
					fos.close();
				}
			}
		} catch (FileNotFoundException e) {
			throw new OpenLegacyException(e);
		} catch (IOException e) {
			throw new OpenLegacyException(e);
		}
	}

	public Map<String, Plugin> getCachePlugins() {
		return cachePlugins;
	}

	public Map<String, String> getCssHtml(String contextPath) {
		Map<String, String> retVal = new HashMap<String, String>();
		for (Plugin plugin : this.cachePlugins.values()) {
			retVal.putAll(generateCssHtml(plugin, contextPath));
		}
		return retVal;
	}

	public Map<String, String> getJsHtml(String contextPath) {
		Map<String, String> retVal = new HashMap<String, String>();
		for (Plugin plugin : this.cachePlugins.values()) {
			retVal.putAll(generateJsHtml(plugin, contextPath));
		}
		return retVal;
	}

	public Map<String, String> getMenuHtml() {
		Map<String, String> retVal = new HashMap<String, String>();
		for (Plugin plugin : this.cachePlugins.values()) {
			retVal.putAll(generateMenuHtml(plugin));
		}
		return retVal;
	}

	public Plugin getPlugin(String pluginName) {
		if (getCachePlugins().containsKey(pluginName)) {
			return getCachePlugins().get(pluginName);
		}
		return null;
	}

	public List<String> getSpringContextResources() {
		List<String> list = new ArrayList<String>();
		Collection<Plugin> plugins = this.cachePlugins.values();
		for (Plugin plugin : plugins) {
			list.addAll(plugin.getSpringContextResources());
		}
		return list;
	}

	public List<String> getSpringWebContextResources() {
		List<String> list = new ArrayList<String>();
		Collection<Plugin> plugins = this.cachePlugins.values();
		for (Plugin plugin : plugins) {
			list.addAll(plugin.getSpringWebContextResources());
		}
		return list;
	}

	public List<String> getViewDeclarations() {
		List<String> list = new ArrayList<String>();
		Collection<Plugin> plugins = this.cachePlugins.values();
		for (Plugin plugin : plugins) {
			list.addAll(plugin.getViewDeclarations());
		}
		return list;
	}

	public boolean isEmpty() {
		return cachePlugins.isEmpty();
	}

	public void putPlugin(Plugin plugin) {
		getCachePlugins().put(plugin.getName(), plugin);
	}

	public void setCachePlugins(Map<String, Plugin> cachePlugins) {
		this.cachePlugins = cachePlugins;
	}

	private static Map<String, String> generateCssHtml(Plugin plugin, String contextPath) {
		Map<String, String> retVal = new HashMap<String, String>();

		String jstlKey = plugin.getCssJstlKey();
		String path = plugin.getCssPath();

		if ((jstlKey == null) || (path == null) || (plugin.getCssItems() == null)) {
			return retVal;
		}
		StringBuilder sb = new StringBuilder();
		for (String item : plugin.getCssItems()) {
			sb.append("<link rel=\"stylesheet\" type=\"text/css\" href=\"");
			sb.append(contextPath);
			sb.append(path);
			sb.append(item);
			sb.append("\"/>\n");
		}
		retVal.put(jstlKey, sb.toString());
		return retVal;
	}

	private static Map<String, String> generateJsHtml(Plugin plugin, String contextPath) {
		Map<String, String> retVal = new HashMap<String, String>();

		String jstlKey = plugin.getJsJstlKey();
		String path = plugin.getJsPath();
		if ((jstlKey == null) || (path == null) || (plugin.getJsItems() == null)) {
			return retVal;
		}
		StringBuilder sb = new StringBuilder();
		for (String item : plugin.getJsItems()) {
			sb.append("<script type=\"text/javascript\" src=\"");
			sb.append(contextPath);
			sb.append(path);
			sb.append(item);
			sb.append("\"><!----></script>\n");
		}
		retVal.put(jstlKey, sb.toString());
		return retVal;
	}

	@SuppressWarnings("unchecked")
	private static Map<String, String> generateMenuHtml(Plugin plugin) {
		Map<String, String> retVal = new HashMap<String, String>();

		String jstlKey = plugin.getMenuJstlKey();
		String html = null;
		if ((jstlKey == null) || (plugin.getMenuItems() == null)) {
			return retVal;
		}

		Map<String, String> categories = new HashMap<String, String>();
		// map contains: category, text, action
		for (Object object : plugin.getMenuItems()) {
			Map<String, String> map = (Map<String, String>)object;
			StringBuilder sb = new StringBuilder();
			if (!categories.containsKey(map.get(Plugin.MENU_CATEGORY))) {
				sb.append("<div data-dojo-type=\"dijit.layout.ContentPane\" title=\"");
				sb.append(map.get(Plugin.MENU_CATEGORY));
				sb.append("\" class=\"element\">\n\t");
				sb.append("<div class=\"links\">\n\t\t");
				sb.append("<ul>\n\t\t\t");
				sb.append("<li><a href=\"");
				sb.append(map.get(Plugin.MENU_ACTION));
				sb.append("\">");
				sb.append(map.get(Plugin.MENU_TEXT));
				sb.append("</a></li>\n\t\t\t");
				categories.put(map.get(Plugin.MENU_CATEGORY), sb.toString());
			} else {
				sb.append(categories.get(map.get(Plugin.MENU_CATEGORY)));
				sb.append("<li><a href=\"");
				sb.append(map.get(Plugin.MENU_ACTION));
				sb.append("\">");
				sb.append(map.get(Plugin.MENU_TEXT));
				sb.append("</a></li>\n\t\t\t");
				categories.put(map.get(Plugin.MENU_CATEGORY), sb.toString());
			}
		}
		// close divs
		for (String category : categories.values()) {
			StringBuilder sb = new StringBuilder();
			sb.append(html != null ? html : "");
			sb.append(category);
			sb.append("</ul></div></div>");
			html = sb.toString();
		}
		retVal.put(jstlKey, html);
		return retVal;
	}

}
