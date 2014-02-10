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
package org.openlegacy.modules.menu;

import java.util.List;

/**
 * Defines a menu item. Each menu item has a target entity, display name, and a tree of menu items.
 * 
 * @author Roi Mor
 * 
 */
public interface MenuItem {

	Class<?> getTargetEntity();

	String getTargetEntityName();

	String getDisplayName();

	List<MenuItem> getMenuItems();

	int getDepth();

	MenuItem clone();

}
