package org.openlegacy.terminal.modules.table;

import apps.inventory.screens.ItemDetails1;
import apps.inventory.screens.ItemsList;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openlegacy.AbstractTest;
import org.openlegacy.modules.table.Table;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@ContextConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
public class DefaultTableDrilldownPerformerTest extends AbstractTest {

	@Test
	public void testDrilldown() {

		ItemDetails1 itemDetails1 = terminalSession.getModule(Table.class).drillDown(ItemsList.class, ItemDetails1.class,
				TerminalDrilldownActions.enter("2"), 1055);

		Assert.assertNotNull(itemDetails1);
	}

}
