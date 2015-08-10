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

package org.openlegacy.ws;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openlegacy.support.SimpleWebServicesRegistry;
import org.openlegacy.ws.definitions.WebServiceDefinition;
import org.openlegacy.ws.definitions.WebServiceMethodDefinition;
import org.openlegacy.ws.definitions.WebServiceParamDetailsDefinition;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.inject.Inject;

import junit.framework.Assert;

@ContextConfiguration("WebServiceRegistryTest-context.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class WebServiceRegistryTest {

	@Inject
	SimpleWebServicesRegistry registry;

	@Test
	public void checkRegistry() {
		Assert.assertEquals(1, registry.getWebServices().size());
		WebServiceDefinition wsDef = registry.getWebServiceByClass(ItemDetailsServiceImpl.class);
		Assert.assertEquals(wsDef, registry.getWebServiceByName(ItemDetailsServiceImpl.class.getSimpleName()));

		Assert.assertEquals(1, wsDef.getMethods().size());
		WebServiceMethodDefinition wsMDef = wsDef.getMethodByName("getItemDetails");
		Assert.assertNotNull(wsMDef);

		Assert.assertEquals(1, wsMDef.getInputParams().size());
		Assert.assertEquals(1, wsMDef.getOutputParams().size());

		WebServiceParamDetailsDefinition wsPDef = wsMDef.getInputParams().get(0);
		Assert.assertNotNull(wsPDef);
		Assert.assertEquals(0, wsPDef.getFields().get(0).getFields().size());

		wsPDef = wsMDef.getOutputParams().get(0);
		Assert.assertNotNull(wsPDef);
		Assert.assertEquals(3, wsPDef.getFields().get(0).getFields().size());

		String[] valNames = new String[] { "itemNum", "itemRecord", "shipping" };
		int[] valCount = new int[] { 0, 3, 2 };

		for (int i = 0; i < 3; i++) {
			Assert.assertEquals(valNames[i], wsPDef.getFields().get(0).getFields().get(i).getFieldName());
			Assert.assertEquals(valCount[i], wsPDef.getFields().get(0).getFields().get(i).getFields().size());
		}
	}
}
