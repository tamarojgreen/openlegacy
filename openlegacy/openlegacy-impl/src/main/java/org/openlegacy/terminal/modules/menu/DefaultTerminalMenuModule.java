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
