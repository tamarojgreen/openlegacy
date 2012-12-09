package org.openlegacy.terminal.web;

import net.sf.uadetector.UADetectorServiceFactory;
import net.sf.uadetector.UserAgent;
import net.sf.uadetector.UserAgentStringParser;

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

		// Get an UserAgentStringParser and analyze the requesting client
		UserAgentStringParser parser = UADetectorServiceFactory.getResourceModuleParser();
		UserAgent agent = parser.parse(request.getHeader("User-Agent"));

		sessionProperties.setProperty("OS", agent.getOperatingSystem().getName());
		sessionProperties.setProperty("browser", agent.getName());
		sessionProperties.setProperty("version", agent.getVersionNumber().toString());
		return sessionProperties;
	}

	public void setIpDevice(Map<String, String> ipDeviceName) {
		this.ipDeviceName = ipDeviceName;
	}
}
