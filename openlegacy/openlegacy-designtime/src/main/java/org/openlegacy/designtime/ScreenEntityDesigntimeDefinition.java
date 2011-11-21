package org.openlegacy.designtime;

import org.openlegacy.FieldType;
import org.openlegacy.HostEntityType;
import org.openlegacy.terminal.definitions.FieldMappingDefinition;
import org.openlegacy.terminal.definitions.NavigationDefinition;
import org.openlegacy.terminal.definitions.ScreenEntityDefinition;
import org.openlegacy.terminal.definitions.ScreenPartEntityDefinition;
import org.openlegacy.terminal.definitions.TableDefinition;
import org.openlegacy.terminal.spi.ScreenIdentification;

import java.util.Map;
import java.util.TreeMap;

public class ScreenEntityDesigntimeDefinition implements ScreenEntityDefinition {

	private String entityName;
	private String displayName;
	private Map<String, FieldMappingDefinition> fieldDefinitions = new TreeMap<String, FieldMappingDefinition>();

	public String getEntityName() {
		return entityName;
	}

	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public Class<?> getEntityClass() {
		return null;
	}

	public Class<? extends HostEntityType> getType() {
		return null;
	}

	public Map<String, FieldMappingDefinition> getFieldsDefinitions() {
		return fieldDefinitions;
	}

	public FieldMappingDefinition getFirstFieldDefinition(Class<? extends FieldType> fieldType) {
		return null;
	}

	public ScreenIdentification getScreenIdentification() {
		return null;
	}

	public NavigationDefinition getNavigationDefinition() {
		return null;
	}

	public Map<String, TableDefinition> getTableDefinitions() {
		return null;
	}

	public Map<String, ScreenPartEntityDefinition> getPartsDefinitions() {
		return null;
	}

}
