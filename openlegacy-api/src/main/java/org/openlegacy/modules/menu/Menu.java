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

import org.openlegacy.EntitiesRegistry;
import org.openlegacy.EntityType;
import org.openlegacy.FieldType;
import org.openlegacy.modules.SessionModule;

import java.util.List;

/**
 * A menu module is responsible for constructing hierarchy menu items from multiple {@link MenuEntity} entities defines with the
 * {@link EntitiesRegistry}
 * 
 * @author Roi Mor
 * 
 */
public interface Menu extends SessionModule {

	public static final String SELECTION_LABEL = "Menu Selection";

	/**
	 * Gets all the menus from the root down to all leafs
	 * 
	 * @return root menu tree
	 */
	MenuItem getMenuTree();

	/**
	 * Gets all sub menus with leafs
	 * 
	 * @return
	 */
	List<MenuItem> getFlatMenuEntries();

	public static class MenuEntity implements EntityType {
	}

	public static class MenuSelectionField implements FieldType {
	}
}
