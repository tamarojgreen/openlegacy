package org.openlegacy.exceptions;

/**
 * Indicates a problem in the registry definition. e.g: 2 entities/fields with the same name, etc
 * 
 */
public class RegistryException extends OpenLegacyRuntimeException {

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
