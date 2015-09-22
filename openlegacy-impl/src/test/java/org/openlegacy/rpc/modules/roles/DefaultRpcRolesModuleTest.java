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

package org.openlegacy.rpc.modules.roles;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openlegacy.exceptions.EntityNotAccessibleException;
import org.openlegacy.modules.login.Login;
import org.openlegacy.modules.login.User;
import org.openlegacy.rpc.RpcSession;
import org.openlegacy.rpc.definitions.mock.RpcDummyEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import javax.inject.Inject;

/**
 * @author Ivan Bort
 */
@ContextConfiguration("/org/openlegacy/rpc/test-rpc-mock-roles-context.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class DefaultRpcRolesModuleTest {

	@Inject
	private RpcSession rpcSession;

	@Test
	public void testShowAction() {
		Login loginModule = rpcSession.getModule(Login.class);
		loginModule.login("user1", "password1");
		User loggedInUser = loginModule.getLoggedInUser();
		loggedInUser.getProperties().put(Login.USER_ROLE_PROPERTY, "AGENT");

		RpcDummyEntity dummyEntity = rpcSession.getEntity(RpcDummyEntity.class, "John", "Doe", 40);
		Assert.notNull(dummyEntity);
	}

	@Test(expected = EntityNotAccessibleException.class)
	public void testShowActionFails() {
		Login loginModule = rpcSession.getModule(Login.class);
		loginModule.login("user1", "password1");
		User loggedInUser = loginModule.getLoggedInUser();
		loggedInUser.getProperties().put(Login.USER_ROLE_PROPERTY, "UNKNOWN");

		rpcSession.getEntity(RpcDummyEntity.class, "John", "Doe", 40);
	}

	@Test
	public void testEntityFieldNotEmpty() {
		Login loginModule = rpcSession.getModule(Login.class);
		loginModule.login("user1", "password1");
		User loggedInUser = loginModule.getLoggedInUser();
		loggedInUser.getProperties().put(Login.USER_ROLE_PROPERTY, "AGENT");

		RpcDummyEntity dummyEntity = rpcSession.getEntity(RpcDummyEntity.class, "John", "Doe", 40);
		Assert.notNull(dummyEntity.getMessage());
	}

	@Test
	public void testEntityFieldEmpty() {
		Login loginModule = rpcSession.getModule(Login.class);
		loginModule.login("user1", "password1");
		User loggedInUser = loginModule.getLoggedInUser();
		loggedInUser.getProperties().put(Login.USER_ROLE_PROPERTY, "MANAGER");

		RpcDummyEntity dummyEntity = rpcSession.getEntity(RpcDummyEntity.class, "John", "Doe", 40);
		Assert.isNull(dummyEntity.getMessage());
	}

	@Test
	public void testPartFieldNotEmpty() {
		Login loginModule = rpcSession.getModule(Login.class);
		loginModule.login("user1", "password1");
		User loggedInUser = loginModule.getLoggedInUser();
		loggedInUser.getProperties().put(Login.USER_ROLE_PROPERTY, "AGENT");

		RpcDummyEntity dummyEntity = rpcSession.getEntity(RpcDummyEntity.class, "John", "Doe", 40);
		Assert.notNull(dummyEntity.getDummyPart().getPartName());
	}

	@Test
	public void testPartFieldEmpty() {
		Login loginModule = rpcSession.getModule(Login.class);
		loginModule.login("user1", "password1");
		User loggedInUser = loginModule.getLoggedInUser();
		loggedInUser.getProperties().put(Login.USER_ROLE_PROPERTY, "MANAGER");

		RpcDummyEntity dummyEntity = rpcSession.getEntity(RpcDummyEntity.class, "John", "Doe", 40);
		Assert.isNull(dummyEntity.getDummyPart().getPartName());
	}

}
