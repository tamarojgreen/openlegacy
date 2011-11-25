package org.openlegacy.terminal.exceptions;

import org.openlegacy.exceptions.EntityNotFoundException;

public class ScreenPositionNotFoundException extends EntityNotFoundException {

	private static final long serialVersionUID = 1L;

	public ScreenPositionNotFoundException(Exception e) {
		super(e);
	}

	public ScreenPositionNotFoundException(String s) {
		super(s);
	}

}
