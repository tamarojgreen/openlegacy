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

package org.openlegacy.terminal.support.binders;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openlegacy.AbstractTest;
import org.openlegacy.terminal.TerminalSession;
import org.openlegacy.terminal.actions.TerminalActions;
import org.openlegacy.terminal.support.mock.EnumBindScreen;
import org.openlegacy.terminal.support.mock.EnumBindScreen.StockGroup;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import junit.framework.Assert;

/**
 * @author Ivan Bort
 */
@ContextConfiguration("EnumBinderTest-context.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class EnumFieldsBinderTest extends AbstractTest {

	@Test
	public void testEnumBinder() {
		TerminalSession terminalSession = newTerminalSession();

		EnumBindScreen entity = terminalSession.getEntity(EnumBindScreen.class);
		StockGroup stockGroup = entity.getStockGroup();
		Assert.assertEquals(StockGroup.Standard, stockGroup);
		entity.setStockGroup(StockGroup.Custom);

		terminalSession.doAction(TerminalActions.ENTER(), entity);

		entity = terminalSession.getEntity(EnumBindScreen.class);
		stockGroup = entity.getStockGroup();
		Assert.assertEquals(StockGroup.Custom, stockGroup);
	}

}
