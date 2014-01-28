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

	@Inject
	private SessionsManager<TerminalSession> sessionsManager;

	private boolean errorWhenNotAvailable = false;
	private String errorMessageWhenNotAvailable = "No device is available from:{0}";

	public String allocate(String pullName) {
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
					used = true;
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

	public void setErrorWhenNotAvailable(boolean errorWhenNotAvailable) {
		this.errorWhenNotAvailable = errorWhenNotAvailable;
	}

	public void setErrorMessageWhenNotAvailable(String errorMessageWhenNotAvailable) {
		this.errorMessageWhenNotAvailable = errorMessageWhenNotAvailable;
	}

}
