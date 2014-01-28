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
package org.openlegacy.terminal.web;

import org.openlegacy.SessionProperties;
import org.openlegacy.support.SimpleSessionProperties;
import org.openlegacy.terminal.TerminalSessionPropertiesConsts;
import org.openlegacy.web.RequestBasedSessionPropertiesProvider;

import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

public class RequestBasedTerminalSessionPropertiesProvider extends RequestBasedSessionPropertiesProvider {

	@Inject
	private HttpServletRequest request;

	private Map<String, String> ipDeviceName;

	@Override
	public SessionProperties getSessionProperties() {

		SimpleSessionProperties sessionProperties = (SimpleSessionProperties)super.getSessionProperties();
		String ip = request.getRemoteAddr();

		if (ipDeviceName != null) {
			String deviceName = ipDeviceName.get(ip);
			if (deviceName != null) {
				sessionProperties.setProperty(TerminalSessionPropertiesConsts.DEVICE_NAME, deviceName);
			}
		}
		return sessionProperties;
	}

	public void setIpDevice(Map<String, String> ipDeviceName) {
		this.ipDeviceName = ipDeviceName;
	}
}
