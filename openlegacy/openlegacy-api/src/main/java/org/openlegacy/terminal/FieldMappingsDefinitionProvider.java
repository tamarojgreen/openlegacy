package org.openlegacy.terminal;

import java.util.Collection;

/**
 * FieldMapping meta-data provider purpose is to return mappings for a given screenEntity
 * 
 */
public interface FieldMappingsDefinitionProvider {

	Collection<FieldMappingDefinition> getFieldsMappingDefinitions(TerminalScreen terminalScreen, Class<?> screenEntity);

}
