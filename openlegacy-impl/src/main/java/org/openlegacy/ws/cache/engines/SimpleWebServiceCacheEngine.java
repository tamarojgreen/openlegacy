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

import org.openlegacy.ws.cache.WebServiceCacheEngine;

import java.util.LinkedHashMap;
import java.util.Map;

public class SimpleWebServiceCacheEngine implements WebServiceCacheEngine {

	private volatile Map<String, Object> cache = new LinkedHashMap<String, Object>();

	@Override
	public void init() {}

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
		return cache.size();
	}

}
