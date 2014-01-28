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
package org.openlegacy.plugins.support.holders;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Imivan
 * 
 */
@XmlRootElement(name = "views")
public class PluginViewsHolder {

	private List<String> xmls;
	private List<String> views;
	private String excludedPath;

	public PluginViewsHolder() {}

	@XmlElement(name = "xml")
	public List<String> getXmls() {
		return xmls;
	}

	public void setXmls(List<String> xmls) {
		this.xmls = xmls;
	}

	@XmlElement(name = "view")
	public List<String> getViews() {
		return views;
	}

	public void setViews(List<String> views) {
		this.views = views;
	}

	@XmlElement(name = "exclude-view-path-part")
	public String getExcludedPath() {
		return excludedPath;
	}

	public void setExcludedPath(String excludedPath) {
		this.excludedPath = excludedPath;
	}

}
