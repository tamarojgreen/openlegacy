package org.openlegacy.terminal.web;

import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.openlegacy.terminal.ConnectionProperties;
import org.openlegacy.terminal.ConnectionPropertiesProvider;
import org.openlegacy.terminal.support.SimpleConnectionProperties;

public class RequestBasedConnectionPropertiesProvider implements ConnectionPropertiesProvider {

	@Inject
	private HttpServletRequest request;
	
	private Map<String,String> ipDeviceName;
	
	public ConnectionProperties getConnectionProperties() {
		String ip = request.getRemoteAddr();
		String deviceName = ipDeviceName.get(ip);
		
		SimpleConnectionProperties connectionProperties = new SimpleConnectionProperties();
		connectionProperties.setDeviceName(deviceName);
		
		return connectionProperties;
	}

	public void setIpDevice(Map<String, String> ipDeviceName) {
		this.ipDeviceName = ipDeviceName;
	}
}
