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
package org.openlegacy.mvc;

public class SimpleOpenLegacyWebProperties implements OpenLegacyWebProperties {

	private String fallbackUrl;
	private boolean fallbackUrlOnError;
	private boolean treeMenu;

	@Override
	public String getFallbackUrl() {
		return fallbackUrl;
	}

	public void setFallbackUrl(String fallbackUrl) {
		this.fallbackUrl = fallbackUrl;
	}

	@Override
	public boolean isFallbackUrlOnError() {
		return fallbackUrlOnError;
	}

	public void setFallbackUrlOnError(boolean fallbackUrlOnError) {
		this.fallbackUrlOnError = fallbackUrlOnError;
	}

	
	@Override
	public boolean isTreeMenu() {
		return treeMenu;
	}
	
	public void setTreeMenu(boolean treeMenu) {
		this.treeMenu = treeMenu;
	}
}
