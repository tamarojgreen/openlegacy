package org.openlegacy.terminal.support.injectors;

import apps.inventory.screens.ItemsList;
import apps.inventory.screens.ItemsList.ItemsListRow;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openlegacy.AbstractTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

@ContextConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
public class ScreenEntityTablesInjectorTest extends AbstractTest {

	@Test
	public void testTableCreation() {
		ItemsList itemsList = terminalSession.getEntity(ItemsList.class);
		List<ItemsListRow> rows = itemsList.getItemListRows();
		Assert.assertNotNull(rows);
		Assert.assertEquals(12, rows.size());
	}
}
