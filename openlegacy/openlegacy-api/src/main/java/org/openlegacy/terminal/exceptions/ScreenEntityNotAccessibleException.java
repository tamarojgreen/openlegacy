package org.openlegacy.terminal.exceptions;

import org.openlegacy.exceptions.HostEntityNotAccessibleException;

public class ScreenEntityNotAccessibleException extends HostEntityNotAccessibleException {

	private static final long serialVersionUID = 1L;

	public ScreenEntityNotAccessibleException(Exception e) {
		super(e);
	}

	public ScreenEntityNotAccessibleException(String s) {
		super(s);
	}

}
