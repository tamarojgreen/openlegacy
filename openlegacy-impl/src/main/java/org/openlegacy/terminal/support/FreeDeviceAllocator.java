package org.openlegacy.terminal.support;

import org.openlegacy.SessionProperties;
import org.openlegacy.SessionsManager;
import org.openlegacy.terminal.DeviceAllocator;
import org.openlegacy.terminal.TerminalSession;
import org.openlegacy.terminal.TerminalSessionPropertiesConsts;

import java.util.Set;

import javax.inject.Inject;

public class FreeDeviceAllocator implements DeviceAllocator {

	@Inject
	private SessionsManager<TerminalSession> sessionsManager;

	public String allocate(String pullName) {
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
		return null;
	}

}
