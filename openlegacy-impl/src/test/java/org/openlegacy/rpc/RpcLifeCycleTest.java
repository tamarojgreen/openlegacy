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

package org.openlegacy.rpc;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openlegacy.Session;
import org.openlegacy.rpc.definitions.mock.InvalidRpcDummyAction;
import org.openlegacy.rpc.definitions.mock.PausedRpcDummyAction;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.inject.Inject;

import junit.framework.Assert;

@ContextConfiguration("test-rpc-life-cycle-context.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class RpcLifeCycleTest {

	@Inject
	ApplicationContext applicationContext;
	boolean breaked = false;
	Object entity;

	@Before
	public void reset() {
		entity = null;
	}

	@Test
	public void testPaused() {
		Assert.assertNull(getSessionBean().getEntity(PausedRpcDummyAction.class));
	}

	@Test
	public void testInvalid() {
		Assert.assertNull(getSessionBean().getEntity(InvalidRpcDummyAction.class));
	}

	public Session getSessionBean() {
		return (Session)applicationContext.getBean("rpcSession");
	}
}
