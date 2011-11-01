package org.openlegacy.terminal.definitions;

import org.openlegacy.HostAction;

import java.util.List;

public interface NavigationDefinition {

	Class<?> getAccessedFrom();

	List<FieldAssignDefinition> getAssignedFields();

	HostAction getHostAction();
}
