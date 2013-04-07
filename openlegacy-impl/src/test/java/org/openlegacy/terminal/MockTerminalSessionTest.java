package org.openlegacy.terminal;

import apps.inventory.screens.ItemDetails1;
import apps.inventory.screens.ItemDetails2;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openlegacy.AbstractTest;
import org.openlegacy.terminal.actions.TerminalActions;
import org.openlegacy.terminal.support.MockupTerminalSession;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;

import junit.framework.Assert;

@ContextConfiguration("MockTerminalSessionTest-context.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class MockTerminalSessionTest extends AbstractTest {

	@Test
	public void testMockConnection() throws IOException {

		TerminalSession terminalSession = newTerminalSession();
		Assert.assertNotNull(terminalSession);
		Assert.assertEquals(MockupTerminalSession.class, terminalSession.getClass());

		ItemDetails1 itemDetails1 = terminalSession.getEntity(ItemDetails1.class, 2000);
		Assert.assertNotNull(itemDetails1);

		Assert.assertEquals("SG", itemDetails1.getStockGroup());
		Assert.assertEquals("Generic stock group", itemDetails1.getStockGroupDescription());

		itemDetails1.setItemDescription("test");
		ItemDetails2 itemDetails2 = terminalSession.doAction(TerminalActions.ENTER(), itemDetails1, ItemDetails2.class);

		Assert.assertEquals("01", itemDetails2.getStockInfo().getStandardUnitCost());
		Assert.assertEquals("Dollar", itemDetails2.getStockInfo().getStandardUnitCostDescription());

		itemDetails1 = terminalSession.doAction(TerminalActions.ENTER(), itemDetails2, ItemDetails1.class);

	}
}
