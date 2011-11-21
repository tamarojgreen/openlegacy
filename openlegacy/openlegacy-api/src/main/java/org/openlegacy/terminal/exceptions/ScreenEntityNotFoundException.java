package org.openlegacy.terminal.exceptions;

import org.openlegacy.exceptions.EntityNotFoundException;

public class ScreenEntityNotFoundException extends EntityNotFoundException {

	private static final long serialVersionUID = 1L;

	public ScreenEntityNotFoundException(Exception e) {
		super(e);
	}

	public ScreenEntityNotFoundException(String s) {
		super(s);
	}

}
