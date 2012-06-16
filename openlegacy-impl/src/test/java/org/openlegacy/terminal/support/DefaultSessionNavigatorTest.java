package org.openlegacy.terminal.support;

import apps.inventory.screens.InventoryManagement;
import apps.inventory.screens.ItemsList;
import apps.inventory.screens.WarehousesList;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openlegacy.AbstractTest;
import org.openlegacy.modules.login.Login;
import org.openlegacy.modules.login.LoginException;
import org.openlegacy.terminal.TerminalSession;
import org.openlegacy.terminal.definitions.ScreenEntityDefinition;
import org.openlegacy.terminal.spi.ScreenEntitiesRegistry;
import org.openlegacy.terminal.support.navigation.NavigationMetadata;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.inject.Inject;

import junit.framework.Assert;

@ContextConfiguration("DefaultSessionNavigatorTest-context.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class DefaultSessionNavigatorTest extends AbstractTest {

	@Inject
	private NavigationMetadata navigationCache;

	@Inject
	private ScreenEntitiesRegistry screenEntitiesRegistry;

	@Test
	public void testSimpleNavigation() throws LoginException {
		TerminalSession terminalSession = newTerminalSession();

		terminalSession.getModule(Login.class).login("user", "pwd");

		Assert.assertTrue(terminalSession.isConnected());

		// direct navigation
		ItemsList itemsList = terminalSession.getEntity(ItemsList.class);
		Assert.assertNotNull(itemsList);

		// indirect navigation
		WarehousesList warehouseDetails = terminalSession.getEntity(WarehousesList.class);
		Assert.assertNotNull(warehouseDetails);

		// check caching
		ScreenEntityDefinition source = screenEntitiesRegistry.get(InventoryManagement.class);
		ScreenEntityDefinition target = screenEntitiesRegistry.get(WarehousesList.class);
		Assert.assertNotNull(navigationCache.get(source, target));
	}

}
