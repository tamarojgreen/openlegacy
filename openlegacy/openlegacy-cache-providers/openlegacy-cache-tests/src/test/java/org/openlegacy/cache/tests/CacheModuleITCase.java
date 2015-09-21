package org.openlegacy.cache.tests;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openlegacy.annotations.rpc.Direction;
import org.openlegacy.cache.CacheInfo;
import org.openlegacy.cache.CacheManager;
import org.openlegacy.cache.services.CacheableEntitiesRegistry;
import org.openlegacy.cache.tests.models.ItemDetails;
import org.openlegacy.rpc.RpcSession;
import org.openlegacy.rpc.actions.RpcActions.UPDATE;
import org.openlegacy.rpc.support.SimpleRpcFlatField;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import javax.inject.Inject;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/test-rpc-context.xml")
public class CacheModuleITCase {

	@Inject
	private RpcSession session;

	@Inject
	private CacheManager cacheManager;

	@Inject
	private CacheableEntitiesRegistry cacheableEntitiesRegistry;

	public static SimpleRpcFlatField makeField(String name, Object value, Class<?> type, Direction direction, int order) {
		SimpleRpcFlatField f = new SimpleRpcFlatField();
		f.setName(name);
		f.setValue(value);
		f.setType(type);
		f.setLength(4);
		f.setDirection(direction);
		f.setOrder(order - 1);

		return f;
	}

	public static SimpleRpcFlatField makeField(String name, Object value) {
		return makeField(name, value, Direction.NONE, 0);
	}

	public static SimpleRpcFlatField makeField(String name, Object value, Direction direction, int order) {
		return makeField(name, value, value.getClass(), direction, order);
	}

	@Test
	public void testGetAndRemoveCacheActions() {

		// GET
		ItemDetails entity = session.getEntity(ItemDetails.class, 1);
		Assert.assertNotNull(entity);

		List<CacheInfo> stats = cacheManager.getCacheStats();
		Assert.assertEquals(1, stats.size());
		Assert.assertEquals(1, stats.get(0).getElementsCount());

		ItemDetails updated = entity.clone();
		updated.getItemRecord().setItemName("Updated Item Name");

		// REMOVE
		session.doAction(new UPDATE(), updated);

		entity = session.getEntity(ItemDetails.class, 1);
		Assert.assertNotNull(entity);
		Assert.assertEquals(updated, entity);

		cacheManager.destroyCache(cacheableEntitiesRegistry.getEntityCacheName(entity.getClass().getSimpleName()));
		Assert.assertEquals(0, cacheManager.getCacheStats().size());
	}

}
