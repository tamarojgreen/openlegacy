/*******************************************************************************
 * Copyright (c) 2014 OpenLegacy Inc.
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
	@Override
	public void addPlugin(Plugin plugin) throws OpenLegacyException {
		if (cachePlugins.containsKey(plugin.getName())) {
			throw new OpenLegacyException(MessageFormat.format(
					"Cannot add plugin to registry. Default plugin registry already contains a plugin with name: {0}",
					plugin.getName()));
		}
		cachePlugins.put(plugin.getName(), plugin);
	}

	/**
	 * Clear registry
	 */
	@Override
	public void clear() {
		this.cachePlugins.clear();
	}

	/**
	 * Require for populating Spring bean
	 * 
	 * @return
	 */
	public Map<String, Plugin> getCachePlugins() {
		return this.cachePlugins;
	}

	/**
	 * Require for populating Spring bean
	 * 
	 * @param cachePlugins
	 */
	public void setCachePlugins(Map<String, Plugin> cachePlugins) {
		this.cachePlugins = cachePlugins;
	}

	/**
	 * Extract views from plugin resources to parent project.
	 * 
	 * @param rootPath
	 *            - the absolute path which used like root path for storing views inside parent project
	 */
	@Override
	public void extractViews(String rootPath) throws OpenLegacyException {
		if ((rootPath == null) || (rootPath.isEmpty())) {
			throw new OpenLegacyException("Cannot extract view files to parent. RootPath is empty.");
		}

		try {
			for (Plugin plugin : this.cachePlugins.values()) {
				if (plugin.isViewExtractedToParent()) {
					continue;
				}
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
					((SimplePlugin)plugin).setViewExtractedToParent(true);
				}
			}
		} catch (FileNotFoundException e) {
			throw new OpenLegacyException(e);
		} catch (IOException e) {
			throw new OpenLegacyException(e);
		}
	}

	@Override
	public Plugin getPlugin(String pluginName) {
		if (cachePlugins.containsKey(pluginName)) {
			return cachePlugins.get(pluginName);
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see org.openlegacy.plugins.PluginsRegistry#getPlugins()
	 */
	@Override
	public List<Plugin> getPlugins() {
		if (isEmpty()) {
			return new ArrayList<Plugin>();
		}
		return new ArrayList<Plugin>(cachePlugins.values());
	}

	@Override
	public List<String> getSpringContextResources() {
		List<String> list = new ArrayList<String>();
		Collection<Plugin> plugins = this.cachePlugins.values();
		for (Plugin plugin : plugins) {
			list.addAll(plugin.getSpringContextResources());
		}
		return list;
	}

	@Override
	public List<String> getSpringWebContextResources() {
		List<String> list = new ArrayList<String>();
		Collection<Plugin> plugins = this.cachePlugins.values();
		for (Plugin plugin : plugins) {
			list.addAll(plugin.getSpringWebContextResources());
		}
		return list;
	}

	@Override
	public List<String> getViewDeclarations() {
		List<String> list = new ArrayList<String>();
		Collection<Plugin> plugins = this.cachePlugins.values();
		for (Plugin plugin : plugins) {
			list.addAll(plugin.getViewDeclarations());
		}
		return list;
	}

	@Override
	public boolean isEmpty() {
		return cachePlugins.isEmpty();
	}

	@Override
	public void putPlugin(Plugin plugin) {
		cachePlugins.put(plugin.getName(), plugin);
	}

}
