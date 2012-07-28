/*******************************************************************************
 * Copyright (c) 2012 OpenLegacy Inc.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     OpenLegacy Inc. - initial API and implementation
 *******************************************************************************/
package org.openlegacy;

import org.springframework.stereotype.Component;

@Component
public class SimpleOpenLegacyProperties implements OpenLegacyProperties {

	public static final String TRAIL_FOLDER_PATH = "org.openlegacy.trail.path";

	public String getProperty(String propertyName) {
		return System.getProperty(propertyName);
	}

}
