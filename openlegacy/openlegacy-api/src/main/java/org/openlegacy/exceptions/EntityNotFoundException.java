package org.openlegacy.exceptions;

/**
 * This exception is typically thrown when a given entity is not found in the registry
 * 
 */
public class EntityNotFoundException extends OpenLegacyRuntimeException {

	private static final long serialVersionUID = 1L;

	public EntityNotFoundException(String s) {
		super(s);
	}

	public EntityNotFoundException(Exception e) {
		super(e);
	}
}
