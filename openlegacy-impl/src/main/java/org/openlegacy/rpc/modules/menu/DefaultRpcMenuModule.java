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
package org.openlegacy.rpc.modules.menu;

import org.openlegacy.modules.menu.Menu;
import org.openlegacy.modules.menu.MenuBuilder;
import org.openlegacy.modules.menu.MenuItem;
import org.openlegacy.rpc.support.RpcSessionModuleAdapter;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;

public class DefaultRpcMenuModule extends RpcSessionModuleAdapter implements Menu, Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private MenuBuilder menuBuilder;

	@Override
	public MenuItem getMenuTree() {
		return menuBuilder.getMenuTree();
	}

	public MenuItem getMenuTree(Class<?> menuEntityClass) {
		return menuBuilder.getMenuTree(menuEntityClass);

	}

	@Override
	public List<MenuItem> getFlatMenuEntries() {
		return menuBuilder.getFlatMenuEntries();
	}

	public static class RpcMenuItem {
	}
}
