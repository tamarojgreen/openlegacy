/*******************************************************************************
 * Copyright (c) 2014 OpenLegacy Inc.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     OpenLegacy Inc. - initial API and implementation
 *******************************************************************************/

package org.openlegacy.db.modules.menu;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openlegacy.db.DbSession;
import org.openlegacy.db.definitions.mock.DbDummyEntity;
import org.openlegacy.db.modules.menu.DefaultDbMenuModule.DbMenuItem;
import org.openlegacy.modules.menu.Menu;
import org.openlegacy.modules.menu.MenuItem;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import javax.inject.Inject;

import junit.framework.Assert;

@ContextConfiguration("/test-db-basic-context.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class DefaultDbMenuModelTest {

	@Inject
	private ApplicationContext applicationContext;

	@Test
	public void testRootMenu() {

		DbSession dbSession = applicationContext.getBean(DbSession.class);
		MenuItem menuItem = dbSession.getModule(Menu.class).getMenuTree();

		Assert.assertEquals(DbMenuItem.class, menuItem.getTargetEntity());
		Assert.assertEquals(1, menuItem.getMenuItems().size());
		Assert.assertEquals(1, menuItem.getDepth());
		MenuItem subMenuItem = menuItem.getMenuItems().get(0);
		Assert.assertEquals(1, subMenuItem.getDepth());
	}

	@Test
	public void testFlatMenu() {

		DbSession dbSession = applicationContext.getBean(DbSession.class);
		List<MenuItem> menuEntries = dbSession.getModule(Menu.class).getFlatMenuEntries();
		Assert.assertEquals(1, menuEntries.size());
		MenuItem menuItem = menuEntries.get(0);
		List<MenuItem> menuItems = menuItem.getMenuItems();
		Assert.assertEquals(1, menuItems.size());
		Assert.assertEquals(DbDummyEntity.class, menuItems.get(0).getTargetEntity());
		Assert.assertEquals(1, menuItem.getDepth());
	}
}
