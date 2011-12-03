package org.openlegacy.terminal.exceptions;

import org.openlegacy.exceptions.EntityNotFoundException;

/**
 * An exception for request a field from a <code>TerminalSnapshot</code> by position which doesn't exists on the snapshot
 * 
 */
public class PositionNotFoundException extends EntityNotFoundException {

	private static final long serialVersionUID = 1L;

	public PositionNotFoundException(Exception e) {
		super(e);
	}

	public PositionNotFoundException(String s) {
		super(s);
	}

}
