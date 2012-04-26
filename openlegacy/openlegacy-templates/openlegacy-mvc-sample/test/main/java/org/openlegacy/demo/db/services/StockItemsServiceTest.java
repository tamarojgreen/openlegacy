package org.openlegacy.demo.db.services;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openlegacy.demo.db.model.StockItem;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;

import junit.framework.Assert;

@ContextConfiguration("/META-INF/spring/applicationContext-test.xml")
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class StockItemsServiceTest {

	@Inject
	private StockItemsService stockItemsService;

	@Test
	@Rollback(false)
	public void testAddItem() {

		StockItem stockItem = stockItemsService.getOrCreateStockItem(1);

		stockItem = stockItemsService.getStockItem(1);
		Assert.assertNotNull(stockItem);
	}

	@Test
	@Rollback(false)
	public void testUpdateNote() {

		stockItemsService.addOrUpdateNote(1, "note1", "Hello world");

		StockItem stockItem = stockItemsService.getStockItem(1);
		Assert.assertNotNull(stockItem);
		Assert.assertEquals(1, stockItem.getNotes().size());
	}
}
