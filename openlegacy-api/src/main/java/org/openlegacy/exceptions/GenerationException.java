package org.openlegacy.exceptions;

/**
 * This exception is thrown when unable to load a persisted snapshot
 * 
 */
public class GenerationException extends OpenLegacyRuntimeException {

	private static final long serialVersionUID = 1L;

	public GenerationException(String s) {
		super(s);
	}

	public GenerationException(String s, Exception e) {
		super(s, e);
	}

	public GenerationException(Exception e) {
		super(e);
	}
}
