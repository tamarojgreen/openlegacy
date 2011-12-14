package org.openlegacy.exceptions;

/**
 * Wrapper exception for providers exception
 * 
 */
public class OpenLegacyProviderException extends OpenLegacyRuntimeException {

	private static final long serialVersionUID = 1L;

	public OpenLegacyProviderException(Exception e) {
		super(e);
	}

	public OpenLegacyProviderException(String s) {
		super(s);
	}

	public OpenLegacyProviderException(String s, Exception e) {
		super(s, e);
	}
}
