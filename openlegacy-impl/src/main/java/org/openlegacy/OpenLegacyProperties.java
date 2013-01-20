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

public interface OpenLegacyProperties {

	public static final String TRAIL_FOLDER_PATH = "org.openlegacy.trail.path";
	public static final String DESIGN_TIME = "org.openlegacy.designtime";

	boolean isRightToLeft();

	boolean isDesigntime();

	String getTrailPath();

	String getProperty(String propertyName);

	String getVersion();

	boolean isUppercaseInput();

	String getFallbackUrl();
}
