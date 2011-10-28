package org.openlegacy.terminal;

import org.openlegacy.HostEntityDefinition;
import org.openlegacy.terminal.spi.ScreenIdentification;

import java.util.HashMap;
import java.util.Map;

public class ScreenEntityDefinition extends HostEntityDefinition {

	private ScreenIdentification screenIdentification;
	private final Map<String, FieldMappingDefinition> FieldMappings = new HashMap<String, FieldMappingDefinition>();

	public ScreenEntityDefinition(String hostEntityName, Class<?> hostEntityClass) {
		super(hostEntityName, hostEntityClass);
	}

	public ScreenIdentification getScreenIdentification() {
		return screenIdentification;
	}

	public void setScreenIdentification(ScreenIdentification screenIdentification) {
		this.screenIdentification = screenIdentification;
	}

	public Map<String, FieldMappingDefinition> getFieldMappings() {
		return FieldMappings;
	}

}
