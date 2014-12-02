package org.openlegacy.validations;

public class Validation {

	public enum ValidationType {
		ERROR,
		WARNING
	};

	private String message;
	private String field;
	private String category;
	private ValidationType validationType;

	public Validation(String message, String field, String category, ValidationType validationType) {
		this.message = message;
		this.field = field;
		this.category = category;
		this.validationType = validationType;
	}

	public String getMessage() {
		return message;
	}

	public String getField() {
		return field;
	}

	public String getCategory() {
		return category;
	}

	public ValidationType getValidationType() {
		return validationType;
	}
}
