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

import org.openlegacy.OpenLegacyProperties;

import java.io.Serializable;

public class SimpleOpenLegacyProperties implements OpenLegacyProperties, Serializable {

	private static final long serialVersionUID = 1L;
	private boolean rightToLeft;
	private boolean uppercaseInput;
	private boolean liveSession = false;
	private String trailFilePath;

	public SimpleOpenLegacyProperties() {}

	@Override
	public String getProperty(String propertyName) {
		return System.getProperty(propertyName);
	}

	@Override
	public boolean isRightToLeft() {
		return rightToLeft;
	}

	public void setRightToLeft(boolean rightToLeft) {
		this.rightToLeft = rightToLeft;
	}

	@Override
	public boolean isDesigntime() {
		return StringConstants.TRUE.equals(getProperty(OpenLegacyProperties.DESIGN_TIME));
	}

	@Override
	public String getTrailPath() {
		return getProperty(OpenLegacyProperties.TRAIL_FOLDER_PATH);
	}

	@Override
	public String getVersion() {
		return getClass().getPackage().getImplementationVersion();
	}

	@Override
	public boolean isUppercaseInput() {
		return uppercaseInput;
	}

	public void setUppercaseInput(boolean uppercaseInput) {
		this.uppercaseInput = uppercaseInput;
	}

	@Override
	public boolean isLiveSession() {
		return this.liveSession;
	}

	/**
	 * @param isLiveSession
	 *            the isLiveSession to set
	 */
	public void setLiveSession(boolean isLiveSession) {
		this.liveSession = isLiveSession;
	}

	public void setTrailFilePath(String trailFilePath) {
		this.trailFilePath = trailFilePath;
	}

	@Override
	public String getTrailFilePath() {
		return this.trailFilePath;
	}
}
