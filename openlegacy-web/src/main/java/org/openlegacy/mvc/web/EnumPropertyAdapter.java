package org.openlegacy.mvc.web;

import org.openlegacy.definitions.EnumFieldTypeDefinition;
import org.openlegacy.definitions.EnumGetValue;
import org.openlegacy.definitions.FieldDefinition;

import java.beans.PropertyEditorSupport;

public class EnumPropertyAdapter extends PropertyEditorSupport {

	private FieldDefinition fieldDefinition;

	public EnumPropertyAdapter(FieldDefinition fieldDefinition) {
		this.fieldDefinition = fieldDefinition;
	}

	@Override
	public void setAsText(String text) throws IllegalArgumentException {
		Object[] enums = ((EnumFieldTypeDefinition)fieldDefinition.getFieldTypeDefinition()).getEnumClass().getEnumConstants();
		for (Object object : enums) {
			if (object instanceof EnumGetValue) {
				if (object.toString().equals(text)) {
					setValue(object);
					break;
				}
			}
		}
	}
}
