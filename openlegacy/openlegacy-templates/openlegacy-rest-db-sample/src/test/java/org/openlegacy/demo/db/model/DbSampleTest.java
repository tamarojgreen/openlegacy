package org.openlegacy.demo.db.model;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import junit.framework.Assert;

@ContextConfiguration("/META-INF/spring/applicationContext.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class DbSampleTest {

	@PersistenceContext
	private EntityManager entityManager;

	@Test
	@Rollback(false)
	@Transactional
	public void testSession() {

		StockItem stockItem = new StockItem();
		stockItem.setItemId(1);
		stockItem.setDescription("Item 1");

		entityManager.merge(stockItem);
		entityManager.flush();

		stockItem = entityManager.find(StockItem.class, 1);
		Assert.assertEquals("Item 1", stockItem.getDescription());

	}
}
