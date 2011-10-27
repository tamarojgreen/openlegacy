package org.openlegacy.terminal;

import com.someorg.examples.screens.InventoryManagement;
import com.someorg.examples.screens.ItemDetails1;
import com.someorg.examples.screens.ItemDetails2;
import com.someorg.examples.screens.ItemsList;
import com.someorg.examples.screens.MainMenu;
import com.someorg.examples.screens.SignOn;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openlegacy.AbstractTest;
import org.openlegacy.adapter.terminal.SendKeyActions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;

@ContextConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
public class SimpleScreenEntityBinderTest extends AbstractTest {

	@Autowired
	private TerminalSession terminalSession;

	@Test
	public void testScreenBinder() throws IOException {
		SignOn signOn = terminalSession.getEntity(SignOn.class);
		Assert.assertNotNull(signOn);
		Assert.assertNotNull(signOn.getTerminalScreen());
		Assert.assertNotNull(signOn.getUserField());

		terminalSession.doAction(SendKeyActions.ENTER, null);
		MainMenu mainMenu = terminalSession.getEntity(MainMenu.class);
		Assert.assertNotNull(mainMenu);
		Assert.assertEquals("101", mainMenu.getCompany());

		terminalSession.doAction(SendKeyActions.ENTER, null);
		InventoryManagement inventoryManagement = terminalSession.getEntity(InventoryManagement.class);
		Assert.assertNotNull(inventoryManagement);

		terminalSession.doAction(SendKeyActions.ENTER, null);
		ItemsList workwithItemMaster = terminalSession.getEntity(ItemsList.class);
		Assert.assertNotNull(workwithItemMaster);

		terminalSession.doAction(SendKeyActions.PAGEDN, null);
		ItemsList workwithItemMaster2 = terminalSession.getEntity(ItemsList.class);
		Assert.assertNotNull(workwithItemMaster2);

		terminalSession.doAction(SendKeyActions.PAGEDN, null);
		ItemsList workwithItemMaster3 = terminalSession.getEntity(ItemsList.class);
		Assert.assertNotNull(workwithItemMaster3);

		terminalSession.doAction(SendKeyActions.ENTER, null);
		ItemDetails1 itemDetails1 = terminalSession.getEntity(ItemDetails1.class);
		Assert.assertNotNull(itemDetails1);
		Assert.assertEquals("2000", itemDetails1.getItemNumber());

		terminalSession.doAction(SendKeyActions.ENTER, null);
		ItemDetails2 itemDetails2 = terminalSession.getEntity(ItemDetails2.class);
		Assert.assertNotNull(itemDetails2);
		Assert.assertEquals("2000", itemDetails2.getItemNumber());
	}
}
