package org.openlegacy.terminal;

import org.openlegacy.exceptions.OpenLegacyRuntimeException;

public class DeviceNotAvailableException extends OpenLegacyRuntimeException {

	private static final long serialVersionUID = 1L;

	public DeviceNotAvailableException(String s) {
		super(s);
	}

	public DeviceNotAvailableException(Exception e) {
		super(e);
	}

}
