package org.openlegacy.terminal.definitions;

import java.util.List;

public interface NavigationDefinition {

	Class<?> getAccessedFrom();

	List<FieldAssignDefinition> getAssignedFields();
}
