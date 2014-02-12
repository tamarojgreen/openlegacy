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
package org.openlegacy.modules.support.menu;

import org.openlegacy.modules.menu.MenuItem;
import org.openlegacy.utils.ProxyUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SimpleMenuItem implements MenuItem, Serializable, Cloneable {

	private static final long serialVersionUID = 1L;

	private transient Class<?> targetEntity;
	private String targetEntityName;

	private List<MenuItem> menuItems = new ArrayList<MenuItem>();

	private String displayName;
	private int depth;

	public SimpleMenuItem(Class<?> targetEntity, String displayName, int depth) {
		this.targetEntity = targetEntity;
		this.targetEntityName = ProxyUtil.getOriginalClass(targetEntity).getSimpleName();
		this.displayName = displayName;
		this.depth = depth;
	}

	public Class<?> getTargetEntity() {
		return targetEntity;
	}

	public String getTargetEntityName() {
		return targetEntityName;
	}

	public List<MenuItem> getMenuItems() {
		return menuItems;
	}

	public String getDisplayName() {
		return displayName;
	}

	public int getDepth() {
		return depth;
	}

	@Override
	public String toString() {
		return getDisplayName();
	}

	@Override
	public MenuItem clone() {
		SimpleMenuItem menuItem = new SimpleMenuItem(targetEntity, displayName, depth);
		List<MenuItem> items = getMenuItems();
		for (MenuItem menuItem2 : items) {
			menuItem.getMenuItems().add(menuItem2);
		}
		return menuItem;
	}
}
