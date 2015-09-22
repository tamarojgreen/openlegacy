/*******************************************************************************
 * Copyright (c) 2015 OpenLegacy Inc.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     OpenLegacy Inc. - initial API and implementation
 *******************************************************************************/

package org.openlegacy.db.modules.roles;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openlegacy.db.DbSession;
import org.openlegacy.db.definitions.mock.DbDummyEntity;
import org.openlegacy.exceptions.EntityNotAccessibleException;
import org.openlegacy.modules.login.Login;
import org.openlegacy.modules.login.User;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import javax.inject.Inject;

/**
 * @author Ivan Bort
 */
@ContextConfiguration("/test-db-basic-context.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class DefaultDbRolesModuleTest {

	@Inject
	private DbSession dbSession;

	@Test
	public void testShowAction() {
		Login loginModule = dbSession.getModule(Login.class);
		loginModule.login("sa", "");
		User loggedInUser = loginModule.getLoggedInUser();
		loggedInUser.getProperties().put(Login.USER_ROLE_PROPERTY, "AGENT");

		DbDummyEntity dummyEntity = dbSession.getEntity(DbDummyEntity.class, 1);
		Assert.notNull(dummyEntity);
	}

	@Test(expected = EntityNotAccessibleException.class)
	public void testShowActionFails() {
		Login loginModule = dbSession.getModule(Login.class);
		loginModule.login("sa", "");
		User loggedInUser = loginModule.getLoggedInUser();
		loggedInUser.getProperties().put(Login.USER_ROLE_PROPERTY, "UNKNOWN");

		dbSession.getEntity(DbDummyEntity.class, 1);
	}

	@Test
	public void testEntityFieldNotEmpty() {
		Login loginModule = dbSession.getModule(Login.class);
		loginModule.login("sa", "");
		User loggedInUser = loginModule.getLoggedInUser();
		loggedInUser.getProperties().put(Login.USER_ROLE_PROPERTY, "AGENT");

		DbDummyEntity dummyEntity = dbSession.getEntity(DbDummyEntity.class, 1);
		Assert.notNull(dummyEntity.getDescription());
	}

	@Test
	public void testEntityFieldEmpty() {
		Login loginModule = dbSession.getModule(Login.class);
		loginModule.login("sa", "");
		User loggedInUser = loginModule.getLoggedInUser();
		loggedInUser.getProperties().put(Login.USER_ROLE_PROPERTY, "MANAGER");

		DbDummyEntity dummyEntity = dbSession.getEntity(DbDummyEntity.class, 1);
		Assert.isNull(dummyEntity.getDescription());
	}

}
