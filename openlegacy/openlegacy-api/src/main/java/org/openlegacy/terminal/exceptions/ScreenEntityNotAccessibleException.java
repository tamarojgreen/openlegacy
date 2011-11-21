package org.openlegacy.terminal.exceptions;

import org.openlegacy.exceptions.EntityNotAccessibleException;

public class ScreenEntityNotAccessibleException extends EntityNotAccessibleException {

	private static final long serialVersionUID = 1L;

	public ScreenEntityNotAccessibleException(Exception e) {
		super(e);
	}

	public ScreenEntityNotAccessibleException(String s) {
		super(s);
	}

}
