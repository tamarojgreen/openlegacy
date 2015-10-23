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

package org.openlegacy.providers.wsrpc;

import org.apache.commons.lang.StringUtils;

public class UrlProps {

	private String baseUrl, userName, password;

	// , authType, wssPassType;

	public String getBaseUrl() {
		return baseUrl;
	}

	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl.endsWith("/") ? StringUtils.removeEnd(baseUrl, "/") : baseUrl;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	// public String getAuthType() {
	// return authType;
	// }
	//
	// public void setAuthType(String authType) {
	// this.authType = authType;
	// }
	//
	// public String getWssPassType() {
	// return wssPassType;
	// }
	//
	// public void setWssPassType(String wssPassType) {
	// this.wssPassType = wssPassType;
	// }

}