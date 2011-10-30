package org.openlegacy.exceptions;

/**
 * The root exception for all open legacy exception types. Extends RuntimeException for simpler exception handing
 * 
 */
public class RegistryException extends OpenLegacyException {

	private static final long serialVersionUID = 1L;

	public RegistryException(Exception e) {
		super(e);
	}

	public RegistryException(String s) {
		super(s);
	}

	public RegistryException(String s, Exception e) {
		super(s, e);
	}
}
