package org.openlegacy.terminal.exceptions;

import org.openlegacy.exceptions.OpenLegacyException;

/**
 * Throw when a terminal action is not mapped to a <code>TerminalConnection</code> within <code>TerminalSendAction</code>
 * 
 */
public class TerminalActionNotMappedException extends OpenLegacyException {

	private static final long serialVersionUID = 1L;

	public TerminalActionNotMappedException(Exception e) {
		super(e);
	}

	public TerminalActionNotMappedException(String s) {
		super(s);
	}

}
