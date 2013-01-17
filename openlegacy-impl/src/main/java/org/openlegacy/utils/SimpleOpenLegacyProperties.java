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
package org.openlegacy.utils;

import org.openlegacy.OpenLegacyProperties;

public class SimpleOpenLegacyProperties implements OpenLegacyProperties {

	private boolean rightToLeft;
	private boolean uppercaseInput;

	public String getProperty(String propertyName) {
		return System.getProperty(propertyName);
	}

	public boolean isRightToLeft() {
		return rightToLeft;
	}

	public void setRightToLeft(boolean rightToLeft) {
		this.rightToLeft = rightToLeft;
	}

	public boolean isDesigntime() {
		return StringConstants.TRUE.equals(getProperty(OpenLegacyProperties.DESIGN_TIME));
	}

	public String getTrailPath() {
		return getProperty(OpenLegacyProperties.TRAIL_FOLDER_PATH);
	}

	public String getVersion() {
		return getClass().getPackage().getImplementationVersion();
	}

	public boolean isUppercaseInput() {
		return uppercaseInput;
	}

	public void setUppercaseInput(boolean uppercaseInput) {
		this.uppercaseInput = uppercaseInput;
	}

}
