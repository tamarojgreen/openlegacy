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

package org.openlegacy.services.cache.engines;

import org.openlegacy.services.cache.ServiceCacheEngine;
import org.openlegacy.services.cache.ServiceCacheError;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SimpleServiceCacheEngine implements ServiceCacheEngine {

	private volatile Map<String, Object> cache = new LinkedHashMap<String, Object>();

	@Override
	public boolean init() {
		return true;
	}

	@Override
	public void put(String key, Object obj) {
		synchronized (cache) {
			cache.put(key, obj);
		}
	}

	@Override
	public Object get(String key, long accessTime) {
		synchronized (cache) {
			return cache.get(key);
		}
	}

	@Override
	public void remove(String key) {
		synchronized (cache) {
			cache.remove(key);
		}
	}

	@Override
	public void destroy() {
		synchronized (cache) {
			cache.clear();
		}
	}

	@Override
	public int getSize() {
		synchronized (cache) {
			return cache.size();
		}
	}

	@Override
	public int getLastError() {
		return ServiceCacheError.ALL_OK;
	}

	@Override
	public void fix() {}

	@Override
	public void update(String key, Object obj) {
		put(key, obj);
	}

	@Override
	public List<String> getKeys(String keyPart) {
		List<String> keys = new ArrayList<String>();
		for (String key : cache.keySet()) {
			if (key.contains(keyPart)) {
				keys.add(key);
			}
		}
		return keys;
	}
}
