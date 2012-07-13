package org.openlegacy.designtime;

public class DesigntimeException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public DesigntimeException(Exception e) {
		super(e);
	}

	public DesigntimeException(String message, Exception e) {
		super(message, e);
	}

	public DesigntimeException(String message) {
		super(message);
	}

}
