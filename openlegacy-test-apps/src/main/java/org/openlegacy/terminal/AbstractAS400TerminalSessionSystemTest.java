/*******************************************************************************
 * Copyright (c) 2012 OpenLegacy Inc.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     OpenLegacy Inc. - initial API and implementation
 *******************************************************************************/
package org.openlegacy.terminal;

import apps.inventory.screens.InventoryManagement;
import apps.inventory.screens.ItemDetails1;
import apps.inventory.screens.MainMenu;
import apps.inventory.screens.SignOn;

import org.junit.Assert;
import org.openlegacy.AbstractTest;
import org.openlegacy.Snapshot;
import org.openlegacy.exceptions.SessionEndedException;
import org.openlegacy.modules.trail.SessionTrail;
import org.openlegacy.modules.trail.Trail;
import org.openlegacy.modules.trail.TrailWriter;
import org.openlegacy.terminal.actions.TerminalActions;
import org.openlegacy.utils.StringUtil;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.inject.Inject;

public class AbstractAS400TerminalSessionSystemTest extends AbstractTest {

	@Inject
	private TrailWriter trailWriter;

	protected void testAS400InventorySystem() throws IOException {

		TerminalSession terminalSession = newTerminalSession();

		SignOn signOn = terminalSession.getEntity(SignOn.class);
		Assert.assertNotNull(signOn);
		Assert.assertNotNull(signOn.getTerminalSnapshot());
		Assert.assertNotNull(signOn.getUserField());
		Assert.assertFalse(signOn.getUserField().isPassword());

		Assert.assertNotNull(signOn.getPasswordField());
		Assert.assertTrue(signOn.getPasswordField().isPassword());
		Assert.assertEquals("COPYRIGHT IBM CORP", signOn.getMessage());

		signOn.setUser("user");
		signOn.setPassword("pwd");
		signOn.setFocusField("programProcedure");

		// tests doAction with expected class type
		MainMenu mainMenu = terminalSession.doAction(TerminalActions.ENTER(), signOn, MainMenu.class);
		Assert.assertNotNull(mainMenu);
		Assert.assertTrue(101 == mainMenu.getCompany());

		SessionTrail<? extends Snapshot> trail = terminalSession.getModule(Trail.class).getSessionTrail();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		trailWriter.write(trail, baos);
		System.out.println(StringUtil.toString(baos));

		InventoryManagement inventoryManagement = terminalSession.getEntity(InventoryManagement.class);
		Assert.assertNotNull(inventoryManagement);

		ItemDetails1 itemDetails1 = terminalSession.getEntity(ItemDetails1.class);
		Assert.assertNotNull(itemDetails1);
		Assert.assertEquals(2000, itemDetails1.getItemNumber().intValue());

		Assert.assertEquals("2000", itemDetails1.getItemDetails2().getItemNumber());

		// tests @ScreenPart & related screen entity
		Assert.assertEquals("17/01/2005", itemDetails1.getItemDetails2().getAuditDetails().getCreatedDate());
		Assert.assertNotNull(itemDetails1.getItemDetails2().getAuditDetails().getCreatedDateField());
		Assert.assertEquals("STUDENT2", itemDetails1.getItemDetails2().getAuditDetails().getCreatedBy());

		// make sure no extra fetch is made
		Assert.assertEquals("2000", itemDetails1.getItemDetails2().getItemNumber());

		itemDetails1.getItemDetails2().getStockInfo().setListPrice("10");
		itemDetails1.getItemDetails2().getStockInfo().setStandardUnitCost("1");

		try {
			terminalSession.doAction(TerminalActions.ENTER(), itemDetails1.getItemDetails2());
		} catch (SessionEndedException e) {
			// ok
		}

	}

}
