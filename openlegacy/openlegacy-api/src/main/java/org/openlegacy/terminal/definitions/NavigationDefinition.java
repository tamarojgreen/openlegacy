package org.openlegacy.terminal.definitions;

import org.openlegacy.terminal.actions.TerminalAction;

import java.util.List;

/**
 * A screen navigation definitions. Define how to step into and out from the given screen entity
 * 
 */
public interface NavigationDefinition {

	Class<?> getAccessedFrom();

	List<FieldAssignDefinition> getAssignedFields();

	TerminalAction getHostAction();

	TerminalAction getExitAction();

}
