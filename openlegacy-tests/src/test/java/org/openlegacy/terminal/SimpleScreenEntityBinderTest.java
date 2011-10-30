package org.openlegacy.terminal;

import com.someorg.examples.screens.InventoryManagement;
import com.someorg.examples.screens.ItemDetails1;
import com.someorg.examples.screens.ItemsList;
import com.someorg.examples.screens.MainMenu;
import com.someorg.examples.screens.SignOn;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openlegacy.AbstractTest;
import org.openlegacy.terminal.support.actions.SendKeyActions;
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
		getToItemDetails();

		terminalSession.doAction(SendKeyActions.ENTER, null);
		ItemDetails1 itemDetails1 = terminalSession.getEntity(ItemDetails1.class);
		Assert.assertNotNull(itemDetails1);
		Assert.assertEquals("2000", itemDetails1.getItemNumber());

		// TODO ChildScreenEntityAspect has some session scope problems in tests mode - need to test on real session
		Assert.assertEquals("2000", itemDetails1.getItemDetails2().getItemNumber());
		// make sure no extra fetch is made
		Assert.assertEquals("2000", itemDetails1.getItemDetails2().getItemNumber());

	}

	private void getToItemDetails() {
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
	}
}
