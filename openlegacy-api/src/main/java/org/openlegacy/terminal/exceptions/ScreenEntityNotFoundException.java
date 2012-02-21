package org.openlegacy.terminal.exceptions;

import org.openlegacy.exceptions.EntityNotFoundException;

/**
 * This exception is thrown when the request screen entity class is not found in the registry. May happen if component scan is not
 * defined properly or the class is not marked with @ScreenEntity
 * 
 */
public class ScreenEntityNotFoundException extends EntityNotFoundException {

	private static final long serialVersionUID = 1L;

	public ScreenEntityNotFoundException(Exception e) {
		super(e);
	}

	public ScreenEntityNotFoundException(String s) {
		super(s);
	}

}
