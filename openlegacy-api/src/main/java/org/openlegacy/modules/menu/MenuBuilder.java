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

public interface MenuBuilder {

	/**
	 * Retrieve a menu tree from the given menu class
	 * 
	 * @param menuEntityClass
	 * @return
	 */
	MenuItem getMenuTree(Class<?> menuEntityClass);

	/**
	 * Find the root menu and retrieve a full menu tree
	 * 
	 * @return
	 */
	MenuItem getMenuTree();
}
