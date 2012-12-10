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
package org.openlegacy.plugins.management.web;

public class ManagementPlugin {

	private boolean enableDisconnect = true;
	private boolean enableSessionViewer = true;

	public boolean isEnableDisconnect() {
		return enableDisconnect;
	}

	public void setEnableDisconnect(boolean enableDisconnect) {
		this.enableDisconnect = enableDisconnect;
	}

	public boolean isEnableSessionViewer() {
		return enableSessionViewer;
	}

	public void setEnableSessionViewer(boolean enableSessionViewer) {
		this.enableSessionViewer = enableSessionViewer;
	}

}
