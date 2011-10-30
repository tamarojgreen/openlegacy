package org.openlegacy.exceptions;

/**
 * This exception is typically thrown when open legacy is unable to retrieve a host entity
 * 
 */
public class HostEntityNotAccessibleException extends OpenLegacyException {

	private static final long serialVersionUID = 1L;

	public HostEntityNotAccessibleException(String s) {
		super(s);
	}

	public HostEntityNotAccessibleException(Exception e) {
		super(e);
	}
}
