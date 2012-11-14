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
import org.openlegacy.exceptions.OpenLegacyRuntimeException;
import org.openlegacy.plugins.Plugin;
import org.openlegacy.plugins.PluginsLoader;
import org.openlegacy.plugins.PluginsRegistry;
import org.openlegacy.utils.XmlSerializationUtil;

import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;

import javax.xml.bind.JAXBException;

/**
 * A simple implementation of plugin loader. Is responsible for extracting all ol-plugin.xml files and extract there information.
 * 
 * @author Imivan
 */
public class DefaultPluginsLoader implements PluginsLoader {

	private PluginsRegistry pluginsRegistry;

	public DefaultPluginsLoader() {
		this.pluginsRegistry = new DefaultPluginsRegistry();
	}

	public PluginsLoader load() {
		try {
			findAndProcessPluginsXmlFiles();
		} catch (OpenLegacyException e) {
			throw new OpenLegacyRuntimeException(e);
		}
		return this;
	}

	public PluginsRegistry getPluginsRegistry() {
		return this.pluginsRegistry;
	}

	private PluginsRegistry findAndProcessPluginsXmlFiles() throws OpenLegacyException {
		try {
			Enumeration<URL> resources = getClass().getClassLoader().getResources("ol-plugin.xml");
			while (resources.hasMoreElements()) {
				URL nextElement = resources.nextElement();
				Plugin plugin = XmlSerializationUtil.deserialize(SimplePlugin.class, nextElement.openStream());
				this.pluginsRegistry.addPlugin(plugin);
			}
		} catch (IOException e) {
			throw new OpenLegacyException(e);
		} catch (JAXBException e) {
			throw new OpenLegacyException(e);
		}
		return this.pluginsRegistry;
	}

	public static PluginsLoader create() {
		return new DefaultPluginsLoader();
	}
}
