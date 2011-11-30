package org.openlegacy.exceptions;

/**
 * This exception is thrown when unable to load a persisted snapshot
 * 
 */
public class UnableToLoadSnapshotException extends OpenLegacyException {

	private static final long serialVersionUID = 1L;

	public UnableToLoadSnapshotException(String s) {
		super(s);
	}

	public UnableToLoadSnapshotException(Exception e) {
		super(e);
	}
}
