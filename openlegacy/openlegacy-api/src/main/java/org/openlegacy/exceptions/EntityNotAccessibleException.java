package org.openlegacy.exceptions;

/**
 * This exception is typically thrown when open legacy is unable to retrieve a entity
 * 
 */
public class EntityNotAccessibleException extends OpenLegacyException {

	private static final long serialVersionUID = 1L;

	public EntityNotAccessibleException(String s) {
		super(s);
	}

	public EntityNotAccessibleException(Exception e) {
		super(e);
	}
}
