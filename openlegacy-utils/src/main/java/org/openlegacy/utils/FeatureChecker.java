/*******************************************************************************
 * Copyright (c) 2014 OpenLegacy Inc.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     OpenLegacy Inc. - initial API and implementation
 *******************************************************************************/
package org.openlegacy.utils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class FeatureChecker {

	private final static Log logger = LogFactory.getLog(FeatureChecker.class);

	public static boolean isSupportBidi() {
		try {
			Class.forName("com.ibm.icu.text.Bidi");
			logger.debug("Found com.ibm.icu library in the classpath. activating bidi support");
			return true;
		} catch (Exception e) {
			return false;
		}
	}
}
