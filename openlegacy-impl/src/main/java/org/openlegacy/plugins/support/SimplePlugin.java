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

import org.openlegacy.plugins.Plugin;
import org.openlegacy.plugins.support.holders.PluginCssHolder;
import org.openlegacy.plugins.support.holders.PluginJsHolder;
import org.openlegacy.plugins.support.holders.PluginMenuEntryHolder;
import org.openlegacy.plugins.support.holders.PluginMenuHolder;
import org.openlegacy.plugins.support.holders.PluginViewsHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "plug-in")
public class SimplePlugin implements Plugin {

	private String name;
	private String description;
	private String creator;
	private String version;
	private PluginJsHolder jsHolder;
	private PluginCssHolder cssHolder;
	private PluginMenuHolder menuHolder;
	private List<String> springWebContextResources;
	private List<String> springContextResources;
	private PluginViewsHolder viewsHolder;
	private boolean isViewExtractedToParent = false;

	public SimplePlugin() {}

	@XmlElement
	public String getCreator() {
		return this.creator;
	}

	@XmlElement(name = "css-list")
	public PluginCssHolder getCssHolder() {
		return this.cssHolder;
	}

	public List<String> getCssItems() {
		if (this.cssHolder != null) {
			return this.cssHolder.getItems();
		}
		return new ArrayList<String>();
	}

	public String getCssPath() {
		if (this.cssHolder != null) {
			return this.cssHolder.getPath();
		}
		return null;
	}

	@XmlElement
	public String getDescription() {
		return this.description;
	}

	public String getExcludedViewPathPart() {
		if (this.viewsHolder != null) {
			return this.viewsHolder.getExcludedPath();
		}
		return "";
	}

	@XmlElement(name = "javascript-list")
	public PluginJsHolder getJsHolder() {
		return this.jsHolder;
	}

	public List<String> getJsItems() {
		if (this.jsHolder != null) {
			return this.jsHolder.getItems();
		}
		return new ArrayList<String>();
	}

	public String getJsPath() {
		if (this.jsHolder != null) {
			return this.jsHolder.getPath();
		}
		return null;
	}

	@XmlElement(name = "menu")
	public PluginMenuHolder getMenuHolder() {
		return menuHolder;
	}

	public List<Map<String, String>> getMenuItems() {
		if ((this.menuHolder != null) && (this.menuHolder.getMenuEntries() != null)
				&& (this.menuHolder.getMenuEntries().size() > 0)) {
			List<Map<String, String>> list = new ArrayList<Map<String, String>>();
			for (PluginMenuEntryHolder entry : this.menuHolder.getMenuEntries()) {
				Map<String, String> map = new HashMap<String, String>();
				map.put(Plugin.MENU_CATEGORY, entry.getCategory());
				map.put(Plugin.MENU_TEXT, entry.getText());
				map.put(Plugin.MENU_ACTION, entry.getAction());
				list.add(map);
			}
			return list;
		}
		return new ArrayList<Map<String, String>>();
	}

	@XmlElement(required = true)
	public String getName() {
		return this.name;
	}

	@XmlElementWrapper(name = "spring-context-resources")
	@XmlElement(name = "resource")
	public List<String> getSpringContextResources() {
		return springContextResources;
	}

	@XmlElementWrapper(name = "spring-web-context-resources")
	@XmlElement(name = "resource")
	public List<String> getSpringWebContextResources() {
		return springWebContextResources;
	}

	@XmlElement
	public String getVersion() {
		return this.version;
	}

	public List<String> getViewDeclarations() {
		if (this.viewsHolder != null) {
			return this.viewsHolder.getXmls();
		}
		return new ArrayList<String>();
	}

	public List<String> getViews() {
		if (this.viewsHolder != null) {
			return this.viewsHolder.getViews();
		}
		return new ArrayList<String>();
	}

	@XmlElement(name = "views")
	public PluginViewsHolder getViewsHolder() {
		return viewsHolder;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public void setCssHolder(PluginCssHolder holder) {
		this.cssHolder = holder;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setJsHolder(PluginJsHolder holder) {
		this.jsHolder = holder;
	}

	public void setMenuHolder(PluginMenuHolder holder) {
		this.menuHolder = holder;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setSpringContextResources(List<String> springContextResources) {
		this.springContextResources = springContextResources;
	}

	public void setSpringWebContextResources(List<String> springWebContextResources) {
		this.springWebContextResources = springWebContextResources;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public void setViewsHolder(PluginViewsHolder viewsHolder) {
		this.viewsHolder = viewsHolder;
	}

	public boolean isViewExtractedToParent() {
		return isViewExtractedToParent;
	}

	public void setViewExtractedToParent(boolean isViewExtractedToParent) {
		this.isViewExtractedToParent = isViewExtractedToParent;
	}

}
