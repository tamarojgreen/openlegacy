package org.openlegacy.terminal.exceptions;

import org.openlegacy.exceptions.EntityNotAccessibleException;

/**
 * An exception indicating a problem with a terminal action execution
 * 
 */
public class TerminalActionException extends EntityNotAccessibleException {

	private static final long serialVersionUID = 1L;

	public TerminalActionException(Exception e) {
		super(e);
	}

	public TerminalActionException(String s) {
		super(s);
	}

}
