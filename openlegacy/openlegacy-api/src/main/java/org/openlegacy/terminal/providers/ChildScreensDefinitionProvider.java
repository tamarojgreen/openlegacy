package org.openlegacy.terminal.providers;

import org.openlegacy.terminal.definitions.ChildScreenDefinition;

import java.util.Collection;

/**
 * Child screen meta-data provider purpose is to return child screen definitions for a given screenEntity or a single field
 * 
 */
public interface ChildScreensDefinitionProvider {

	Collection<ChildScreenDefinition> getChildScreenDefinitions(Class<?> screenEntity);

	ChildScreenDefinition getChildScreenDefinitions(Class<?> screenEntity, String fieldName);

}
