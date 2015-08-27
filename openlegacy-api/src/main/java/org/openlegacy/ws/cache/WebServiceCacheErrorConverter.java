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

public class WebServiceCacheErrorConverter {

	public static final int ALL_OK = 0;
	public static final int CACHE_PROCESSOR_INIT_ERROR = 1;
	public static final int NULL_ENGINE = 2;
	public static final int ENGINE_ERROR = 3;

	public static final String[] ERROR_STRINGS = { "All ok", "Cache processor was initialized with errors",
			"Cache engine not assigned to cache processor, check bean definition", "Engine error" };

	public static String convertError(int error) {
		return ERROR_STRINGS[error];
	}
}
