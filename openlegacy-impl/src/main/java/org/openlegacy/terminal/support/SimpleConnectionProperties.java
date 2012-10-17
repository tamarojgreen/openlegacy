package org.openlegacy.terminal.support;

import org.openlegacy.terminal.ConnectionProperties;

public class SimpleConnectionProperties implements ConnectionProperties{

	
	private String deviceName;

	public String getDeviceName() {
		return deviceName;
	}
	
	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}

}
