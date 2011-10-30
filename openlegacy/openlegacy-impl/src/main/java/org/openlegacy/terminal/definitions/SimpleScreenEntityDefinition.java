package org.openlegacy.terminal.definitions;

import org.openlegacy.SimpleHostEntityDefinition;
import org.openlegacy.terminal.ChildScreenDefinition;
import org.openlegacy.terminal.FieldMappingDefinition;
import org.openlegacy.terminal.ScreenEntityDefinition;
import org.openlegacy.terminal.spi.ScreenIdentification;

import java.util.HashMap;
import java.util.Map;

public class SimpleScreenEntityDefinition extends SimpleHostEntityDefinition implements ScreenEntityDefinition {

	private ScreenIdentification screenIdentification;
	private final Map<String, FieldMappingDefinition> fieldMappingDefinitions = new HashMap<String, FieldMappingDefinition>();
	private final Map<String, ChildScreenDefinition> childScreenDefinitions = new HashMap<String, ChildScreenDefinition>();

	public SimpleScreenEntityDefinition(String hostEntityName, Class<?> hostEntityClass) {
		super(hostEntityName, hostEntityClass);
	}

	public ScreenIdentification getScreenIdentification() {
		return screenIdentification;
	}

	public void setScreenIdentification(ScreenIdentification screenIdentification) {
		this.screenIdentification = screenIdentification;
	}

	public Map<String, FieldMappingDefinition> getFieldMappingDefinitions() {
		return fieldMappingDefinitions;
	}

	public Map<String, ChildScreenDefinition> getChildScreenDefinitions() {
		return childScreenDefinitions;
	}

}
