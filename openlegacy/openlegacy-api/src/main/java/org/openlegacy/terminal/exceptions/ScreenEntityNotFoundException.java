package org.openlegacy.terminal.exceptions;

import org.openlegacy.exceptions.HostEntityNotFoundException;

public class ScreenEntityNotFoundException extends HostEntityNotFoundException {

	private static final long serialVersionUID = 1L;

	public ScreenEntityNotFoundException(Exception e) {
		super(e);
	}

	public ScreenEntityNotFoundException(String s) {
		super(s);
	}

}
