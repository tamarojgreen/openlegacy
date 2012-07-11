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
package org.openlegacy.designtime.terminal.analyzer.modules.menu;

import org.openlegacy.designtime.terminal.analyzer.ScreenFact;
import org.openlegacy.terminal.TerminalField;

import java.util.List;

public class MenuScreenFact implements ScreenFact {

	private TerminalField selectionField;
	private List<MenuItemFact> menuItems;

	public MenuScreenFact(TerminalField selelctionField, List<MenuItemFact> menuItems) {
		this.selectionField = selelctionField;
		this.menuItems = menuItems;
	}

	public TerminalField getSelectionField() {
		return selectionField;
	}

	public List<MenuItemFact> getMenuItems() {
		return menuItems;
	}

}
