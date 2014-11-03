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
package org.openlegacy.terminal.support;

import org.openlegacy.SessionProperties;
import org.openlegacy.SessionsManager;
import org.openlegacy.terminal.DeviceAllocator;
import org.openlegacy.terminal.DeviceNotAvailableException;
import org.openlegacy.terminal.TerminalSession;
import org.openlegacy.terminal.TerminalSessionPropertiesConsts;

import java.text.MessageFormat;
import java.util.Set;

import javax.inject.Inject;

public class FreeDeviceAllocator implements DeviceAllocator {

	private static final String IP = "IP";
	
	@Inject
	private SessionsManager<TerminalSession> sessionsManager;

	private boolean errorWhenNotAvailable = false;
	private String errorMessageWhenNotAvailable = "No device is available from:{0}";
	private boolean disconnectOtherSessionWhenSameIP = false;

	private long delayAfterDisconnect = 3000;

	@Override
	public String allocate(String pullName,SessionProperties newSessionProperties) {
		if (pullName == null) {
			return null;
		}
		String[] possibleDevices = pullName.split(",");

		Set<SessionProperties> sessionsProperties = sessionsManager.getSessionsProperties();
		for (String possibleDevice : possibleDevices) {
			boolean used = false;
			for (SessionProperties sessionProperties : sessionsProperties) {
				String activeDevice = (String)sessionProperties.getProperty(TerminalSessionPropertiesConsts.DEVICE_NAME);
				if (possibleDevice.equals(activeDevice)) {
					used = !disconnectWhenSameIp(newSessionProperties,
							sessionProperties);
				}
			}
			if (!used) {
				return possibleDevice;
			}
		}
		if (errorWhenNotAvailable) {
			throw (new DeviceNotAvailableException(MessageFormat.format(errorMessageWhenNotAvailable, pullName)));
		}
		return null;
	}

	private boolean disconnectWhenSameIp(SessionProperties newSessionProperties,
			SessionProperties sessionProperties) {
		if (disconnectOtherSessionWhenSameIP){
			String activeDeviceIp = (String) sessionProperties.getProperty(IP);
			String newSessionIp = (String) newSessionProperties.getProperty(IP);
			if (activeDeviceIp != null && newSessionIp != null && activeDeviceIp.equals(newSessionIp)){
				sessionsManager.disconnect(sessionProperties.getId());
				try {
					Thread.sleep(delayAfterDisconnect);
				} catch (InterruptedException e) {
				}
				return true;
			}
		}
		return false;
	}

	public void setErrorWhenNotAvailable(boolean errorWhenNotAvailable) {
		this.errorWhenNotAvailable = errorWhenNotAvailable;
	}

	public void setErrorMessageWhenNotAvailable(String errorMessageWhenNotAvailable) {
		this.errorMessageWhenNotAvailable = errorMessageWhenNotAvailable;
	}

	public void setDisconnectOtherSessionWhenSameIP(boolean disconnectOtherSessionWhenSameIP) {
		this.disconnectOtherSessionWhenSameIP = disconnectOtherSessionWhenSameIP;
	}
	public void setDelayAfterDisconnect(long delayAfterDisconnect) {
		this.delayAfterDisconnect = delayAfterDisconnect;
	}
}
