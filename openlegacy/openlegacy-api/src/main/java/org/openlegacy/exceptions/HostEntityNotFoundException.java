package org.openlegacy.exceptions;

/**
 * This exception is typically thrown when a given host entity is not found in the registry
 * 
 */
public class HostEntityNotFoundException extends OpenLegacyException {

	private static final long serialVersionUID = 1L;

	public HostEntityNotFoundException(String s) {
		super(s);
	}

	public HostEntityNotFoundException(Exception e) {
		super(e);
	}
}
