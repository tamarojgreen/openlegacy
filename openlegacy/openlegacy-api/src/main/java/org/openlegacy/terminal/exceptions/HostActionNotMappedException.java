package org.openlegacy.terminal.exceptions;

import org.openlegacy.exceptions.OpenLegacyException;

public class HostActionNotMappedException extends OpenLegacyException {

	private static final long serialVersionUID = 1L;

	public HostActionNotMappedException(Exception e) {
		super(e);
	}

	public HostActionNotMappedException(String s) {
		super(s);
	}

}
