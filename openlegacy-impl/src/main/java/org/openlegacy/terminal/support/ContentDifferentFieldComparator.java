package org.openlegacy.terminal.support;

import org.openlegacy.FieldFormatter;
import org.openlegacy.terminal.FieldComparator;

import javax.inject.Inject;

public class ContentDifferentFieldComparator implements FieldComparator {

	@Inject
	private FieldFormatter fieldFormatter;

	public boolean isFieldModified(Object screenPojo, String fieldName, Object oldValue, Object newValue) {
		if (newValue == null) {
			return false;
		}
		if (oldValue == null && newValue != null) {
			return true;
		}
		oldValue = fieldFormatter.format(String.valueOf(oldValue));
		return !oldValue.equals(String.valueOf(newValue));
	}
}
