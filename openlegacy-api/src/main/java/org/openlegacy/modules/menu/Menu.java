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
package org.openlegacy.modules.menu;

import org.openlegacy.EntitiesRegistry;
import org.openlegacy.EntityType;
import org.openlegacy.FieldType;
import org.openlegacy.modules.SessionModule;

/**
 * A menu module is responsible for constructing hierarchy menu items from multiple {@link MenuEntity} entities defines with the
 * {@link EntitiesRegistry}
 * 
 * @author Roi Mor
 * 
 */
public interface Menu extends SessionModule {

	public static final String SELECTION_LABEL = "Menu Selection";

	MenuItem getMenuTree();

	MenuItem getMenuTree(Class<?> menuEntityClass);

	public static class MenuEntity implements EntityType {
	}

	public static class MenuSelectionField implements FieldType {
	}
}
