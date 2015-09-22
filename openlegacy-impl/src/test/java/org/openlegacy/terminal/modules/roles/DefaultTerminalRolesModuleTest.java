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

package org.openlegacy.terminal.modules.roles;

import apps.inventory.screens.ItemDetails1;
import apps.inventory.screens.ItemDetails2;
import apps.inventory.screens.ItemsList;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openlegacy.exceptions.EntityNotAccessibleException;
import org.openlegacy.modules.login.Login;
import org.openlegacy.modules.login.User;
import org.openlegacy.terminal.TerminalSession;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import javax.inject.Inject;

/**
 * @author Ivan Bort
 */
@ContextConfiguration("/test-mock-context.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class DefaultTerminalRolesModuleTest {

	@Inject
	private TerminalSession terminalSession;

	@Test
	public void testShowAction() {
		Login loginModule = terminalSession.getModule(Login.class);
		loginModule.login("user1", "password1");
		User loggedInUser = loginModule.getLoggedInUser();
		loggedInUser.getProperties().put(Login.USER_ROLE_PROPERTY, "AGENT");

		ItemsList itemsList = terminalSession.getEntity(ItemsList.class);
		Assert.notNull(itemsList);
	}

	@Test(expected = EntityNotAccessibleException.class)
	public void testShowActionFails() {
		Login loginModule = terminalSession.getModule(Login.class);
		loginModule.login("user1", "password1");
		User loggedInUser = loginModule.getLoggedInUser();
		loggedInUser.getProperties().put(Login.USER_ROLE_PROPERTY, "MANAGER");

		terminalSession.getEntity(ItemsList.class);
	}

	@Test
	public void testEntityFieldNotEmpty() {
		Login loginModule = terminalSession.getModule(Login.class);
		loginModule.login("user1", "password1");
		User loggedInUser = loginModule.getLoggedInUser();
		loggedInUser.getProperties().put(Login.USER_ROLE_PROPERTY, "AGENT");

		ItemDetails1 itemDetails1 = terminalSession.getEntity(ItemDetails1.class);
		Assert.notNull(itemDetails1.getItemDescription());
	}

	@Test
	public void testEntityFieldEmpty() {
		Login loginModule = terminalSession.getModule(Login.class);
		loginModule.login("user1", "password1");
		User loggedInUser = loginModule.getLoggedInUser();
		loggedInUser.getProperties().put(Login.USER_ROLE_PROPERTY, "MANAGER");

		ItemDetails1 itemDetails1 = terminalSession.getEntity(ItemDetails1.class);
		Assert.isNull(itemDetails1.getItemDescription());
	}

	@Test
	public void testPartFieldNotEmpty() {
		Login loginModule = terminalSession.getModule(Login.class);
		loginModule.login("user1", "password1");
		User loggedInUser = loginModule.getLoggedInUser();
		loggedInUser.getProperties().put(Login.USER_ROLE_PROPERTY, "AGENT");

		ItemDetails2 itemDetails2 = terminalSession.getEntity(ItemDetails2.class);
		Assert.notNull(itemDetails2.getAuditDetails().getCreatedDate());
	}

	@Test
	public void testPartFieldEmpty() {
		Login loginModule = terminalSession.getModule(Login.class);
		loginModule.login("user1", "password1");
		User loggedInUser = loginModule.getLoggedInUser();
		loggedInUser.getProperties().put(Login.USER_ROLE_PROPERTY, "MANAGER");

		ItemDetails2 itemDetails2 = terminalSession.getEntity(ItemDetails2.class);
		Assert.isNull(itemDetails2.getAuditDetails().getCreatedDate());
	}
}
