package org.openlegacy.terminal;

import apps.inventory.screens.InventoryManagement;
import apps.inventory.screens.ItemDetails1;
import apps.inventory.screens.ItemsList;
import apps.inventory.screens.MainMenu;
import apps.inventory.screens.SignOn;

import org.junit.Assert;
import org.openlegacy.AbstractTest;
import org.openlegacy.exceptions.SessionEndedException;
import org.openlegacy.terminal.actions.TerminalActions;

import java.io.IOException;

public class AbstractTerminalSessionSystemTest extends AbstractTest {

	protected void testSystem() throws IOException {

		TerminalSession terminalSession = newTerminalSession();

		SignOn signOn = terminalSession.getEntity(SignOn.class);
		Assert.assertNotNull(signOn);
		Assert.assertNotNull(signOn.getTerminalSnapshot());
		Assert.assertNotNull(signOn.getUserField());
		Assert.assertEquals("COPYRIGHT IBM CORP", signOn.getMessage());

		signOn.setUser("user");
		signOn.setPassword("pwd");
		signOn.setFocusField("programProcedure");

		// tests doAction with expected class type
		MainMenu mainMenu = terminalSession.doAction(TerminalActions.ENTER(), signOn, MainMenu.class);
		Assert.assertNotNull(mainMenu);
		Assert.assertEquals("101", mainMenu.getCompany());

		InventoryManagement inventoryManagement = terminalSession.getEntity(InventoryManagement.class);
		Assert.assertNotNull(inventoryManagement);

		ItemsList itemList = terminalSession.getEntity(ItemsList.class);
		Assert.assertNotNull(itemList);

		ItemDetails1 itemDetails1 = terminalSession.doAction(TerminalActions.ENTER(), null, ItemDetails1.class);
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
			terminalSession.doAction(TerminalActions.ENTER(), itemDetails1.getItemDetails2());
		} catch (SessionEndedException e) {
			// ok
		}
	}

}
