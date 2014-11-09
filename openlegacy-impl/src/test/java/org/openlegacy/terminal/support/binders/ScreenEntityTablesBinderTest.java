package org.openlegacy.terminal.support.binders;

import apps.inventory.screens.ItemsList;
import apps.inventory.screens.ItemsList.ItemsListRow;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openlegacy.AbstractTest;
import org.openlegacy.modules.table.Table;
import org.openlegacy.terminal.TerminalSession;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

@ContextConfiguration("ScreenEntityTablesBinderTest-context.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class ScreenEntityTablesBinderTest extends AbstractTest {

	@Test
	public void testTableCreation() {
		TerminalSession terminalSession = newTerminalSession();

		ItemsList itemsList = terminalSession.getEntity(ItemsList.class);

		// only 1 of 2 table action should return - since 1 is conditioned wrongly
		Assert.assertEquals(1, itemsList.getItemsListRowActions().size());

		List<ItemsListRow> rows = itemsList.getItemListRows();
		Assert.assertNotNull(rows);
		Assert.assertEquals(12, rows.size());
		Assert.assertEquals("APPLE", rows.get(0).getAlphaSearch());
		Assert.assertEquals("Red apple - FRT", rows.get(0).getItemDescription());
		Assert.assertEquals("FRT", rows.get(0).getItemCategory());
		// Assert.assertNotNull(rows.get(0).getItemDescriptionField());
		Assert.assertTrue(1007 == rows.get(0).getItemNumber());

		Assert.assertEquals("OLIVE", rows.get(rows.size() - 1).getAlphaSearch());

	}

	/**
	 * Includes test of filterExpression, row containing ------ is filtered out.
	 */
	@Test
	public void testMultiplePagesTable() {
		TerminalSession terminalSession = newTerminalSession();

		List<ItemsListRow> rows = terminalSession.getModule(Table.class).collectAll(ItemsList.class, ItemsListRow.class);

		Assert.assertNotNull(rows);
		Assert.assertEquals(14, rows.size());
		Assert.assertEquals("APPLE", rows.get(0).getAlphaSearch());
		Assert.assertEquals("Red apple - FRT", rows.get(0).getItemDescription());
		Assert.assertTrue(1007 == rows.get(0).getItemNumber());

		Assert.assertEquals("TOMATO", rows.get(12).getAlphaSearch());

		Assert.assertEquals("WATERMELON", rows.get(rows.size() - 1).getAlphaSearch());
		for (ItemsListRow row : rows) {
			System.out.println(row.getItemNumber() + " " + row.getItemDescription());
		}
	}
}
