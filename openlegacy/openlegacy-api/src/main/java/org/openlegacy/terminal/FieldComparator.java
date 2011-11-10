package org.openlegacy.terminal;

public interface FieldComparator {

	boolean isFieldModified(ScreenEntity screenEntity, String fieldName, Object oldValue, Object newValue);
}
