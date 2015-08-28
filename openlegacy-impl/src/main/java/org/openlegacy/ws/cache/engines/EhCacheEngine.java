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

package org.openlegacy.ws.cache.engines;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

import org.openlegacy.ws.cache.WebServiceCacheEngine;
import org.openlegacy.ws.cache.WebServiceCacheError;
import org.springframework.context.ApplicationContext;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

//Small example for 3rd parties engines(frameworks) usage
public class EhCacheEngine implements WebServiceCacheEngine {

	@Inject
	private ApplicationContext applicationContext;

	private CacheManager cacheManager;
	private Cache cache;

	@Override
	public boolean init() {
		cacheManager = CacheManager.getInstance();
		cacheManager.addCache(applicationContext.getDisplayName());
		cache = cacheManager.getCache(applicationContext.getDisplayName());
		return true;
	}

	@Override
	public void put(String key, Object obj) {
		cache.put(new Element(key, obj));
	}

	@Override
	public Object get(String key, long accessTime) {
		Element cached = cache.get(key);
		return cached == null ? null : cached.getObjectValue();
	}

	@Override
	public void remove(String key) {
		cache.remove(key);
	}

	@Override
	public void destroy() {
		cacheManager.shutdown();
	}

	@Override
	public int getSize() {
		return cache.getSize();
	}

	@Override
	public int getLastError() {
		return WebServiceCacheError.ALL_OK;
	}

	@Override
	public void fix() {}

	@Override
	public void update(String key, Object obj) {
		remove(key);
		put(key, obj);
	}

	@Override
	public List<String> getKeys(String keyPart) {
		List<String> keys = new ArrayList<String>();
		for (Object key : cache.getKeys()) {
			String localkey = (String)key;
			if (localkey.contains(keyPart)) {
				keys.add(localkey);
			}
		}
		return keys;
	}
}
