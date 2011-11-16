package org.openlegacy.terminal;

public interface FieldComparator {

	boolean isFieldModified(Object screenPojo, String fieldName, Object oldValue, Object newValue);
}
