package org.openlegacy.terminal.exceptions;

import org.openlegacy.exceptions.OpenLegacyException;

public class TerminalActionNotMappedException extends OpenLegacyException {

	private static final long serialVersionUID = 1L;

	public TerminalActionNotMappedException(Exception e) {
		super(e);
	}

	public TerminalActionNotMappedException(String s) {
		super(s);
	}

}
