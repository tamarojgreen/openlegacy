package org.openlegacy.exceptions;

/**
 * The root exception for all open legacy exception types. Extends RuntimeException for simpler exception handing
 * 
 */
public class OpenLegacyRuntimeException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public OpenLegacyRuntimeException(Exception e) {
		super(e);
	}

	public OpenLegacyRuntimeException(String s) {
		super(s);
	}

	public OpenLegacyRuntimeException(String s, Exception e) {
		super(s, e);
	}
}
