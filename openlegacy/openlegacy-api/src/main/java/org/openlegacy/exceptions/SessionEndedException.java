package org.openlegacy.exceptions;

/**
 * This exception is thrown when/after an action is performed and the session ended
 * 
 */
public class SessionEndedException extends OpenLegacyException {

	private static final long serialVersionUID = 1L;

	public SessionEndedException(String s) {
		super(s);
	}

	public SessionEndedException(Exception e) {
		super(e);
	}
}
