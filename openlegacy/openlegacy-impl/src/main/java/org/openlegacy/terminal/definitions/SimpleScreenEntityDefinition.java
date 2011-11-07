package org.openlegacy.terminal.definitions;

import org.openlegacy.SimpleHostEntityDefinition;
import org.openlegacy.terminal.spi.ScreenIdentification;
import org.openlegacy.terminal.support.SimpleScreenIdentification;

import java.util.HashMap;
import java.util.Map;

public class SimpleScreenEntityDefinition extends SimpleHostEntityDefinition<FieldMappingDefinition> implements ScreenEntityDefinition {

	private ScreenIdentification screenIdentification = new SimpleScreenIdentification();
	private NavigationDefinition navigationDefinition;
	private Map<String, TableDefinition> tableDefinitions = new HashMap<String, TableDefinition>();
	private Map<String, ScreenPartEntityDefinition> partDefinitions = new HashMap<String, ScreenPartEntityDefinition>();

	public SimpleScreenEntityDefinition(String hostEntityName, Class<?> hostEntityClass) {
		super(hostEntityName, hostEntityClass);
	}

	public ScreenIdentification getScreenIdentification() {
		return screenIdentification;
	}

	public void setScreenIdentification(ScreenIdentification screenIdentification) {
		this.screenIdentification = screenIdentification;
	}

	public NavigationDefinition getNavigationDefinition() {
		return navigationDefinition;
	}

	public void setNavigationDefinition(NavigationDefinition navigationDefinition) {
		this.navigationDefinition = navigationDefinition;
	}

	public Map<String, TableDefinition> getTableDefinitions() {
		return tableDefinitions;
	}

	public Map<String, ScreenPartEntityDefinition> getPartsDefinitions() {
		return partDefinitions;
	}
}
