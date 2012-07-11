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
package org.openlegacy.terminal.modules.menu;

import org.openlegacy.modules.menu.Menu;
import org.openlegacy.modules.menu.MenuBuilder;
import org.openlegacy.modules.menu.MenuItem;
import org.openlegacy.terminal.support.TerminalSessionModuleAdapter;

import javax.inject.Inject;

public class DefaultTerminalMenuModule extends TerminalSessionModuleAdapter implements Menu {

	private static final long serialVersionUID = 1L;

	@Inject
	private MenuBuilder menuBuilder;

	public MenuItem getMenuTree() {
		return menuBuilder.getMenuTree();
	}

	public MenuItem getMenuTree(Class<?> menuEntityClass) {
		return menuBuilder.getMenuTree(menuEntityClass);

	}

}
