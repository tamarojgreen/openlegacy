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
