package org.openlegacy.terminal.web;

import org.openlegacy.SessionProperties;
import org.openlegacy.SessionPropertiesProvider;
import org.openlegacy.support.SimpleSessionProperties;
import org.openlegacy.terminal.TerminalSessionPropertiesConsts;

import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

public class RequestBasedSessionPropertiesProvider implements SessionPropertiesProvider {

	@Inject
	private HttpServletRequest request;

	private Map<String, String> ipDeviceName;

	public SessionProperties getSessionProperties() {
		String ip = request.getRemoteAddr();
		String deviceName = ipDeviceName.get(ip);

		SimpleSessionProperties sessionProperties = new SimpleSessionProperties();
		sessionProperties.setProperty(TerminalSessionPropertiesConsts.DEVICE_NAME, deviceName);

		return sessionProperties;
	}

	public void setIpDevice(Map<String, String> ipDeviceName) {
		this.ipDeviceName = ipDeviceName;
	}
}
