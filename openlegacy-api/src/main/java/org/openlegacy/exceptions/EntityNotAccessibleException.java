package org.openlegacy.exceptions;

/**
 * This exception is typically thrown when open legacy is unable to retrieve/access an entity
 * 
 */
public class EntityNotAccessibleException extends OpenLegacyRuntimeException {

	private static final long serialVersionUID = 1L;

	public EntityNotAccessibleException(String s) {
		super(s);
	}

	public EntityNotAccessibleException(String s, Exception e) {
		super(s, e);
	}

	public EntityNotAccessibleException(Exception e) {
		super(e);
	}
}
