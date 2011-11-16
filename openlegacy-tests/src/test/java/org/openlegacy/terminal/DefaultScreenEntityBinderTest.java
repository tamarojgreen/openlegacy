package org.openlegacy.terminal;

import apps.inventory.screens.InventoryManagement;
import apps.inventory.screens.ItemDetails1;
import apps.inventory.screens.ItemsList;
import apps.inventory.screens.MainMenu;
import apps.inventory.screens.SignOn;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openlegacy.AbstractTest;
import org.openlegacy.exceptions.SessionEndedException;
import org.openlegacy.terminal.actions.SendKeyActions;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;

@ContextConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
public class DefaultScreenEntityBinderTest extends AbstractTest {

	@Test
	public void testScreenBinder() throws IOException {

		SignOn signOn = terminalSession.getEntity(SignOn.class);
		Assert.assertNotNull(signOn);
		Assert.assertNotNull(signOn.getTerminalScreen());
		Assert.assertNotNull(signOn.getUserField());

		signOn.setUser("user");
		signOn.setPassword("pwd");
		signOn.setFocusField("programProcedure");

		// tests doAction with expected class type
		MainMenu mainMenu = terminalSession.doAction(SendKeyActions.ENTER, signOn, MainMenu.class);
		Assert.assertNotNull(mainMenu);
		Assert.assertEquals("101", mainMenu.getCompany());

		InventoryManagement inventoryManagement = terminalSession.getEntity(InventoryManagement.class);
		Assert.assertNotNull(inventoryManagement);

		ItemsList itemList = terminalSession.getEntity(ItemsList.class);
		Assert.assertNotNull(itemList);

		ItemDetails1 itemDetails1 = terminalSession.doAction(SendKeyActions.ENTER, null, ItemDetails1.class);
		Assert.assertNotNull(itemDetails1);
		Assert.assertEquals("2000", itemDetails1.getItemNumber());

		Assert.assertEquals("2000", itemDetails1.getItemDetails2().getItemNumber());

		// tests @ScreenPart & related screen entity
		Assert.assertEquals("17/01/2005", itemDetails1.getItemDetails2().getAuditDetails().getCreatedDate());
		Assert.assertNotNull(itemDetails1.getItemDetails2().getAuditDetails().getCreatedDateField());
		Assert.assertEquals("STUDENT2", itemDetails1.getItemDetails2().getAuditDetails().getCreatedBy());

		// make sure no extra fetch is made
		Assert.assertEquals("2000", itemDetails1.getItemDetails2().getItemNumber());

		itemDetails1.getItemDetails2().getStockInfo().setListPrice("10");
		itemDetails1.getItemDetails2().getStockInfo().setStandardUnitCost("1");

		try {
			terminalSession.doAction(SendKeyActions.ENTER, itemDetails1.getItemDetails2());
		} catch (SessionEndedException e) {
			// ok
		}
	}

}
