package org.openlegacy.testing.support;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Assert;
import org.openlegacy.Session;
import org.openlegacy.modules.menu.Menu;
import org.openlegacy.modules.menu.MenuItem;
import org.openlegacy.testing.ApiTester;

import java.text.MessageFormat;
import java.util.List;

public class DefaultApiTester implements ApiTester {

	private final static Log logger = LogFactory.getLog(DefaultApiTester.class);

	@Override
	public void test(Session session) {
		List<MenuItem> menus = session.getModule(Menu.class).getFlatMenuEntries();
		for (MenuItem menuItem : menus) {
			List<MenuItem> leafs = menuItem.getMenuItems();
			for (MenuItem leaf : leafs) {
				Object entity = session.getEntity(leaf.getTargetEntity());
				Assert.assertNotNull(MessageFormat.format("Entity {0} is null", leaf.getTargetEntityName()), entity);
				Assert.assertTrue(MessageFormat.format(
						"Retured entity from session {0} doesnt match requested entity in leaf {1}",
						entity.getClass().getSimpleName(), leaf.getTargetEntityName()),
						leaf.getTargetEntity().isAssignableFrom(entity.getClass()));

				logger.info(MessageFormat.format("Successfully reached entity {0}", leaf.getTargetEntityName()));
			}
		}
	}

}
