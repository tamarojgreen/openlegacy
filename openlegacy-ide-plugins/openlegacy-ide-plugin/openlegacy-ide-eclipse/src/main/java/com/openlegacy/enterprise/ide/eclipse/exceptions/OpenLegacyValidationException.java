package com.openlegacy.enterprise.ide.eclipse.exceptions;

import org.openlegacy.exceptions.OpenLegacyException;

/**
 * @author Ivan Bort
 * 
 */
public class OpenLegacyValidationException extends OpenLegacyException {

	private static final long serialVersionUID = -2211696944899325464L;

	public OpenLegacyValidationException(Exception e) {
		super(e);
	}

	public OpenLegacyValidationException(String s) {
		super(s);
	}

	public OpenLegacyValidationException(String s, Exception e) {
		super(s, e);
	}

}
