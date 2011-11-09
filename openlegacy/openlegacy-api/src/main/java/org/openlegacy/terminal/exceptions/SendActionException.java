package org.openlegacy.terminal.exceptions;

import org.openlegacy.exceptions.HostEntityNotAccessibleException;

public class SendActionException extends HostEntityNotAccessibleException {

	private static final long serialVersionUID = 1L;

	public SendActionException(Exception e) {
		super(e);
	}

	public SendActionException(String s) {
		super(s);
	}

}
