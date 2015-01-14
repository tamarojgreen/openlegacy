package org.openlegacy.terminal.modules.table;

import apps.inventory.screens.ItemDetails1;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openlegacy.AbstractTest;
import org.openlegacy.modules.table.Table;
import org.openlegacy.terminal.TerminalSession;
import org.openlegacy.terminal.actions.TerminalActions.ENTER;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@ContextConfiguration("DefaultTableDrilldownPerformerTest-context.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class DefaultTableDrilldownPerformerTest extends AbstractTest {

	@Test
	public void testDrilldown() {

		TerminalSession terminalSession = newTerminalSession();

		ItemDetails1 itemDetails1 = terminalSession.getModule(Table.class).drillDown(ItemDetails1.class,
				TerminalDrilldownActions.newAction(ENTER.class, "2"), 2000);

		Assert.assertNotNull(itemDetails1);
	}

}
