package org.openlegacy.validations;

public interface Validation {

	public enum ValidationType {
		ERROR,
		WARNING
	};

	public String getMessage();

	public String getField();

	public String getCategory();

	public ValidationType getValidationType();
}
