package org.openlegacy.exceptions;

/**
 * The root exception for all open legacy exception types. Extends RuntimeException for simpler exception handing
 * 
 */
public class OpenLegacyException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public OpenLegacyException(Exception e) {
		super(e);
	}

	public OpenLegacyException(String s) {
		super(s);
	}

	public OpenLegacyException(String s, Exception e) {
		super(s, e);
	}
}
