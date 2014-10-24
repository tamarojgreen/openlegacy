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

package org.openlegacy.db.modules.menu;

import org.openlegacy.modules.menu.Menu;
import org.openlegacy.modules.menu.MenuBuilder;
import org.openlegacy.modules.menu.MenuItem;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;

/**
 * @author Ivan Bort
 * 
 */
public class DefaultDbMenuModule implements Menu, Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private MenuBuilder menuBuilder;

	public void destroy() {}

	public MenuItem getMenuTree() {
		return menuBuilder.getMenuTree();
	}

	public List<MenuItem> getFlatMenuEntries() {
		return menuBuilder.getFlatMenuEntries();
	}

	public static class DbMenuItem {

	}

}
