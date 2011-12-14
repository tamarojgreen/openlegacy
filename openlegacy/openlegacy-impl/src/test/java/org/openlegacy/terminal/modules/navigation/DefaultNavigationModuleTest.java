package org.openlegacy.terminal.modules.navigation;

import apps.inventory.screens.InventoryManagement;
import apps.inventory.screens.ItemsList;
import apps.inventory.screens.MainMenu;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openlegacy.AbstractTest;
import org.openlegacy.EntityDescriptor;
import org.openlegacy.modules.login.Login;
import org.openlegacy.modules.login.LoginException;
import org.openlegacy.modules.navigation.Navigation;
import org.openlegacy.terminal.TerminalSession;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import junit.framework.Assert;

@ContextConfiguration(locations = "/test-mock-context.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class DefaultNavigationModuleTest extends AbstractTest {

	@Test
	public void testPathEntries() throws LoginException {
		TerminalSession terminalSession = newTerminalSession();

		terminalSession.getModule(Login.class).login("user", "pwd");
		terminalSession.getEntity(ItemsList.class);

		List<EntityDescriptor> path = terminalSession.getModule(Navigation.class).getPathFromRoot();

		Assert.assertEquals(path.get(0).getEntityClass(), MainMenu.class);
		Assert.assertFalse(path.get(0).isCurrent());

		Assert.assertEquals(path.get(1).getEntityClass(), InventoryManagement.class);

		Assert.assertEquals(path.get(2).getEntityClass(), ItemsList.class);
		Assert.assertEquals(path.get(2).getDisplayName(), "Items List");
		Assert.assertEquals(path.get(2).getEntityName(), "ItemsList");
		Assert.assertTrue(path.get(2).isCurrent());
	}
}
