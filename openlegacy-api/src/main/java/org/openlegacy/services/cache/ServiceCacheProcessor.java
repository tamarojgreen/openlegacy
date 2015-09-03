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

package org.openlegacy.services.cache;

import org.openlegacy.services.definitions.ServiceMethodDefinition;

public interface ServiceCacheProcessor {

	public void put(String key, Object obj, ServiceMethodDefinition methodDefinition);

	public Object get(String key, long accessTime);

	public void remove(String key);

	public void update(String key, Object obj);

	public String generateKey(Object... args);

	public void setEngine(ServiceCacheEngine cacheEngine);

	public void destroy();

	public int getLastError();

	public void tryToFixEngine();

	public void updateCacheDuration(String serviceName, String methodName, long newDuration);
}