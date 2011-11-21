package org.openlegacy.exceptions;

/**
 * This exception is typically thrown when open legacy is unable to retrieve an entity
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
