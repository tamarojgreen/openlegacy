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

package org.openlegacy.services;

import org.apache.commons.lang.ClassUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openlegacy.services.cache.ServiceCacheError;
import org.openlegacy.services.cache.SimpleServiceCacheProcessor;
import org.openlegacy.services.definitions.ServiceDefinition;
import org.openlegacy.services.definitions.ServiceMethodDefinition;
import org.openlegacy.services.definitions.ServiceParamDetailsDefinition;
import org.openlegacy.support.SimpleServicesRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

@ContextConfiguration("ServiceRegistryTest-context.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class ServiceRegistryTest {

	private final static Log logger = LogFactory.getLog(ServiceRegistryTest.class);

	@Inject
	SimpleServicesRegistry registry;

	@Inject
	ApplicationContext applicationContext;

	@Inject
	WebService service;

	@Inject
	SimpleServiceCacheProcessor cacheProcessor;

	private volatile Throwable error = new Throwable();

	@Before
	public void checkRegistry() {
		Assert.assertEquals(1, registry.getServices().size());
		ServiceDefinition wsDef = registry.getServiceByClass(WebServiceImpl.class);
		Assert.assertEquals(wsDef, registry.getServiceByName("WebService"));

		Assert.assertEquals(1, wsDef.getMethods().size());
		ServiceMethodDefinition wsMDef = wsDef.getMethodByName("getItem");
		Assert.assertNotNull(wsMDef);

		Assert.assertEquals(1, wsMDef.getInputParams().size());
		Assert.assertEquals(1, wsMDef.getOutputParams().size());

		ServiceParamDetailsDefinition wsPDef = wsMDef.getInputParams().get(0);
		Assert.assertNotNull(wsPDef);
		Assert.assertEquals(0, wsPDef.getFields().size());

		wsPDef = wsMDef.getOutputParams().get(0);
		Assert.assertNotNull(wsPDef);
		Assert.assertEquals(3, wsPDef.getFields().size());

		String[] valNames = new String[] { "itemNum", "itemRecord", "shipping" };
		int[] valCount = new int[] { 0, 3, 2 };

		for (int i = 0; i < 3; i++) {
			Assert.assertEquals(valNames[i], wsPDef.getFields().get(i).getFieldName());
			Assert.assertEquals(valCount[i], wsPDef.getFields().get(i).getFields().size());
		}
	}

	@Test
	public void testCache() throws Exception {
		logger.info("Service cache test will run ~35 seconds");
		Assert.assertTrue(ClassUtils.getAllSuperclasses(service.getClass()).contains(Proxy.class));
		final int[] ids = new int[] { 1000, 1001, 1002, 1003, 1004 };
		List<Thread> threads = new ArrayList<Thread>();

		int processors = Runtime.getRuntime().availableProcessors();// one thread - one processor
		if (processors > 5) {
			processors = 5;
		}

		// processors = 1; // uncomment for debug
		for (int i = 0; i < processors; i++) {
			final int j = i;
			Thread thread = new Thread(new Runnable() {

				int id = ids[j];
				long accessTime;
				int times = 3;// thirty seconds = times * WebService.DURATION

				public boolean isNewObjectInCache(long accessTime) {
					return accessTime - this.accessTime >= WebServiceImpl.DURATION;
				}

				@Override
				public void run() {
					accessTime = System.currentTimeMillis();
					while (times > 0) {
						try {
							ItemDetails cacheObject;
							synchronized (service) {
								cacheObject = service.getItem(id);
							}
							Assert.assertTrue(cacheProcessor.getLastError() == ServiceCacheError.ALL_OK);
							long accessTime = System.currentTimeMillis();
							Assert.assertNotNull(cacheObject); // null reference
							Assert.assertEquals((Integer)cacheObject.getItemNum(), (Integer)id); // another instance
							if (isNewObjectInCache(accessTime)) {
								this.accessTime = accessTime;
								times--;
							}
						} catch (Throwable th) {
							synchronized (error) {
								error.initCause(th);// for accessing from this thread
							}
						}
					}
				}
			});
			threads.add(thread);
		}

		for (Thread thread : threads) {
			thread.start();
		}

		boolean exit = false;
		while (!exit) {
			synchronized (error) {
				if (error.getCause() != null) {
					throw new Exception(error.getCause());
				}
			}
			for (Thread thread : threads) {
				exit = !thread.isAlive();
			}
		}
		Long newDuration = 20000L;
		// Assert.assertTrue(cache.getSize() > 0);
		cacheProcessor.updateCacheDuration("WebService", "getItem", newDuration);
		Thread.sleep(4000);
		// Assert.assertTrue(cache.getSize() == 0);// if you`re using updating records in cache instead of removing(on cache
		// duration
		// changed) - comment this assert
		Assert.assertTrue(cacheProcessor.getLastError() == ServiceCacheError.ALL_OK);
		Assert.assertEquals(newDuration, registry.getServiceByName("WebService").getMethodByName("getItem").getCacheDuration());
	}
}