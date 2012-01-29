package org.openlegacy.exceptions;

/**
 * This exception is thrown when unable to load a persisted snapshot
 * 
 */
public class UnableToGenerateSnapshotException extends OpenLegacyRuntimeException {

	private static final long serialVersionUID = 1L;

	public UnableToGenerateSnapshotException(String s) {
		super(s);
	}

	public UnableToGenerateSnapshotException(Exception e) {
		super(e);
	}
}
