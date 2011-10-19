package org.openlegacy.terminal;

import java.util.Collection;

/**
 * FieldMapping meta-data provider purpose is to return mappings for a given screenEntity 
 *
 */
public interface FieldMappingsProvider {

	Collection<FieldMapping> getFieldsMappings(TerminalScreen terminalScreen, Class<?> screenEntity);

}
