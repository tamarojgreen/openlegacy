package org.openlegacy.terminal.modules.menu;

import apps.inventory.screens.InventoryManagement;
import apps.inventory.screens.ItemsList;
import apps.inventory.screens.MainMenu;
import apps.inventory.screens.WarehousesList;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openlegacy.AbstractTest;
import org.openlegacy.modules.menu.Menu;
import org.openlegacy.modules.menu.MenuItem;
import org.openlegacy.terminal.TerminalSession;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

@ContextConfiguration("/test-mock-context.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class DefaultTerminalMenuModuleTest extends AbstractTest {

	@Test
	public void testRootMenu() {

		TerminalSession terminalSession = newTerminalSession();

		MenuItem menuItem = terminalSession.getModule(Menu.class).getMenuTree();

		Assert.assertEquals(MainMenu.class, menuItem.getTargetEntity());
		Assert.assertEquals(1, menuItem.getMenuItems().size());
		MenuItem subMenuItem = menuItem.getMenuItems().get(0);
		asserSubMenu(subMenuItem);
	}

	@Test
	public void testMenu() {

		TerminalSession terminalSession = newTerminalSession();

		MenuItem menuItem = terminalSession.getModule(Menu.class).getMenuTree(InventoryManagement.class);

		asserSubMenu(menuItem);
	}

	@Test
	public void testFlatMenu() {

		TerminalSession terminalSession = newTerminalSession();

		List<MenuItem> menuEntries = terminalSession.getModule(Menu.class).getFlatMenuEntries();

		Assert.assertEquals(1, menuEntries.size());
		List<MenuItem> menuItems = menuEntries.get(0).getMenuItems();
		Assert.assertEquals(2, menuItems.size());

	}

	private static void asserSubMenu(MenuItem subMenuItem) {
		Assert.assertEquals(InventoryManagement.class, subMenuItem.getTargetEntity());
		List<MenuItem> subMenuItems = subMenuItem.getMenuItems();

		Assert.assertEquals(2, subMenuItems.size());
		Assert.assertEquals(ItemsList.class, subMenuItems.get(0).getTargetEntity());
		Assert.assertEquals(WarehousesList.class, subMenuItems.get(1).getTargetEntity());
	}
}
