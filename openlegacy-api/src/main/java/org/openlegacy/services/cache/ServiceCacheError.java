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

public class ServiceCacheError {

	public static final int NULL = 0;
	public static final int ALL_OK = 1;
	public static final int CACHE_INIT_ERROR = 2;
	public static final int ENGINE_INIT_ERROR = 3;
	public static final int ENGINE_ERROR = 4;
	public static final int CACHE_ERROR = 5;

	public static final String[] ERROR_STRINGS = { "Null", "All ok", "Cache was initialized with errors",
			"Cache engine was initialized with errors", "Engine error, see log for details", "Cache error, see log for details" };

	public static String convertError(int error) {
		return ERROR_STRINGS[error];
	}
}
