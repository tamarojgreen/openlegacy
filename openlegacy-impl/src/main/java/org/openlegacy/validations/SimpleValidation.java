package org.openlegacy.validations;

import org.apache.commons.lang.builder.ToStringBuilder;

public class SimpleValidation implements Validation {

	private String message;
	private String field;
	private String category;
	private ValidationType validationType;

	public SimpleValidation(String message, String field, String category, ValidationType validationType) {
		this.message = message;
		this.field = field;
		this.category = category;
		this.validationType = validationType;
	}

	@Override
	public String getMessage() {
		return message;
	}

	@Override
	public String getField() {
		return field;
	}

	@Override
	public String getCategory() {
		return category;
	}

	@Override
	public ValidationType getValidationType() {
		return validationType;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
