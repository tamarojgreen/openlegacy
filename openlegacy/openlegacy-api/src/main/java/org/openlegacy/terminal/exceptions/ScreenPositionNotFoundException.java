package org.openlegacy.terminal.exceptions;

import org.openlegacy.exceptions.EntityNotFoundException;

/**
 * An exception for request a field from a <code>TerminalSnapshot</code> by position which doesn't exists on the snapshot
 * 
 */
public class ScreenPositionNotFoundException extends EntityNotFoundException {

	private static final long serialVersionUID = 1L;

	public ScreenPositionNotFoundException(Exception e) {
		super(e);
	}

	public ScreenPositionNotFoundException(String s) {
		super(s);
	}

}
