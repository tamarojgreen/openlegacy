package org.openlegacy.designtime.terminal.model;

import org.apache.commons.lang.NotImplementedException;
import org.openlegacy.EntityType;
import org.openlegacy.FieldType;
import org.openlegacy.terminal.TerminalSnapshot;
import org.openlegacy.terminal.definitions.FieldMappingDefinition;
import org.openlegacy.terminal.definitions.NavigationDefinition;
import org.openlegacy.terminal.definitions.ScreenEntityDefinition;
import org.openlegacy.terminal.definitions.ScreenPartEntityDefinition;
import org.openlegacy.terminal.definitions.TableDefinition;
import org.openlegacy.terminal.spi.ScreenIdentification;
import org.openlegacy.terminal.support.SimpleScreenIdentification;

import java.io.Serializable;
import java.util.Map;
import java.util.TreeMap;

public class ScreenEntityDesigntimeDefinition implements ScreenEntityDefinition, Serializable {

	private static final long serialVersionUID = 1L;

	private String entityName;
	private String displayName;
	private Map<String, FieldMappingDefinition> fieldDefinitions = new TreeMap<String, FieldMappingDefinition>();
	private Map<String, TableDefinition> tableDefinitions = new TreeMap<String, TableDefinition>();
	private TerminalSnapshot terminalSnapshot;

	private String packageName;

	private ScreenIdentification screenIdentification = new SimpleScreenIdentification();

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
		throw (new NotImplementedException("Design-time entity definition doesn''t support class"));
	}

	public Class<? extends EntityType> getType() {
		throw (new NotImplementedException("Design-time entity definition doesn''t support entity type"));
	}

	public Map<String, FieldMappingDefinition> getFieldsDefinitions() {
		return fieldDefinitions;
	}

	public FieldMappingDefinition getFirstFieldDefinition(Class<? extends FieldType> fieldType) {
		return null;
	}

	public ScreenIdentification getScreenIdentification() {
		return screenIdentification;
	}

	public NavigationDefinition getNavigationDefinition() {
		return null;
	}

	public Map<String, TableDefinition> getTableDefinitions() {
		return tableDefinitions;
	}

	public Map<String, ScreenPartEntityDefinition> getPartsDefinitions() {
		return null;
	}

	public void setSnapshot(TerminalSnapshot terminalSnapshot) {
		this.terminalSnapshot = terminalSnapshot;
	}

	public TerminalSnapshot getSnapshot() {
		return terminalSnapshot;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

}
