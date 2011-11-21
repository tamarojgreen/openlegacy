package org.openlegacy.terminal.exceptions;

import org.openlegacy.exceptions.EntityNotAccessibleException;

public class SendActionException extends EntityNotAccessibleException {

	private static final long serialVersionUID = 1L;

	public SendActionException(Exception e) {
		super(e);
	}

	public SendActionException(String s) {
		super(s);
	}

}
