package org.openlegacy.terminal.definitions;

import org.openlegacy.SimpleHostEntityDefinition;
import org.openlegacy.terminal.spi.ScreenIdentification;
import org.openlegacy.terminal.support.SimpleScreenIdentification;

import java.util.HashMap;
import java.util.Map;

public class SimpleScreenEntityDefinition extends SimpleHostEntityDefinition<FieldMappingDefinition> implements ScreenEntityDefinition {

	private ScreenIdentification screenIdentification = new SimpleScreenIdentification();
	private final Map<String, ChildScreenDefinition> childScreenDefinitions = new HashMap<String, ChildScreenDefinition>();
	private NavigationDefinition navigationDefinition;
	private Map<Class<?>, TableDefinition> tableDefinitions = new HashMap<Class<?>, TableDefinition>();

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

	public NavigationDefinition getNavigationDefinition() {
		return navigationDefinition;
	}

	public void setNavigationDefinition(NavigationDefinition navigationDefinition) {
		this.navigationDefinition = navigationDefinition;
	}

	public Map<Class<?>, TableDefinition> getTableDefinitions() {
		return tableDefinitions;
	}
}
