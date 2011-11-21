package org.openlegacy.exceptions;

/**
 * This exception is typically thrown when open legacy is unable to retrieve an entity
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
