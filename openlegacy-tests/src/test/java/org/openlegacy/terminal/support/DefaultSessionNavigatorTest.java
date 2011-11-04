package org.openlegacy.terminal.support;

import com.someorg.examples.screens.InventoryManagement;
import com.someorg.examples.screens.ItemsList;
import com.someorg.examples.screens.WarehouseDetails;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openlegacy.AbstractTest;
import org.openlegacy.modules.login.Login;
import org.openlegacy.terminal.definitions.ScreenEntityDefinition;
import org.openlegacy.terminal.spi.ScreenEntitiesRegistry;
import org.openlegacy.terminal.support.navigation.NavigationCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import junit.framework.Assert;

@ContextConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
public class DefaultSessionNavigatorTest extends AbstractTest {

	@Autowired
	private NavigationCache navigationCache;

	@Autowired
	private ScreenEntitiesRegistry screenEntitiesRegistry;

	@Test
	public void testSimpleNavigation() {
		terminalSession.getModule(Login.class).login("user", "pwd");

		Assert.assertTrue(terminalSession.isConnected());

		// direct navigation
		ItemsList itemsList = terminalSession.getEntity(ItemsList.class);
		Assert.assertNotNull(itemsList);

		// indirect navigation
		WarehouseDetails warehouseDetails = terminalSession.getEntity(WarehouseDetails.class);
		Assert.assertNotNull(warehouseDetails);

		// check caching
		ScreenEntityDefinition source = screenEntitiesRegistry.get(InventoryManagement.class);
		ScreenEntityDefinition target = screenEntitiesRegistry.get(WarehouseDetails.class);
		Assert.assertNotNull(navigationCache.get(source, target));
	}

}
