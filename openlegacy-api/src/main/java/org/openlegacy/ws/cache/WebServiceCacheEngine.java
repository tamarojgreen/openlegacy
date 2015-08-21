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

package org.openlegacy.ws.cache;

public interface WebServiceCacheEngine {

	public void init();

	public void put(String key, Object obj);

	public Object get(String key, long accessTime);

	public void remove(String key);

	public void destroy();

	public int getSize();
}
