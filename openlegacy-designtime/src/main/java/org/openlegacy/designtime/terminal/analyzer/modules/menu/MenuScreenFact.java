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
package org.openlegacy.designtime.terminal.analyzer.modules.menu;

import org.openlegacy.designtime.terminal.analyzer.ScreenFact;
import org.openlegacy.terminal.TerminalField;

import java.util.List;

public class MenuScreenFact implements ScreenFact {

	private List<TerminalField> selectionFields;
	private List<MenuItemFact> menuItems;

	public MenuScreenFact(List<TerminalField> selelctionFields, List<MenuItemFact> menuItems) {
		this.selectionFields = selelctionFields;
		this.menuItems = menuItems;
	}

	public List<TerminalField> getSelectionFields() {
		return selectionFields;
	}

	public List<MenuItemFact> getMenuItems() {
		return menuItems;
	}

}
