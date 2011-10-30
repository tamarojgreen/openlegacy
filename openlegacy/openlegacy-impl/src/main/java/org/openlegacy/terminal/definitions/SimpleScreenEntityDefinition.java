package org.openlegacy.terminal.definitions;

import org.openlegacy.SimpleHostEntityDefinition;
import org.openlegacy.terminal.spi.ScreenIdentification;

import java.util.HashMap;
import java.util.Map;

public class SimpleScreenEntityDefinition extends SimpleHostEntityDefinition<FieldMappingDefinition> implements ScreenEntityDefinition {

	private ScreenIdentification screenIdentification;
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

	public Map<String, ChildScreenDefinition> getChildScreenDefinitions() {
		return childScreenDefinitions;
	}

}
