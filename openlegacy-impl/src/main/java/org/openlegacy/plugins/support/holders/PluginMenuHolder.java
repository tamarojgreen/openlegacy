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
package org.openlegacy.plugins.support.holders;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Imivan
 * 
 */
@XmlRootElement(name = "menu")
public class PluginMenuHolder {

	private List<PluginMenuEntryHolder> menuEntries;

	public PluginMenuHolder() {}

	@XmlElement(name = "menu-entry", type = PluginMenuEntryHolder.class)
	public List<PluginMenuEntryHolder> getMenuEntries() {
		return menuEntries;
	}

	public void setMenuEntries(List<PluginMenuEntryHolder> menuEntries) {
		this.menuEntries = menuEntries;
	}

}
