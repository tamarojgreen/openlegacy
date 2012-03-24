package org.openlegacy.designtime.terminal.generators.support;

import org.apache.commons.lang.NotImplementedException;
import org.openlegacy.EntityType;
import org.openlegacy.FieldType;
import org.openlegacy.definitions.ActionDefinition;
import org.openlegacy.designtime.terminal.generators.ScreenPojoCodeModel;
import org.openlegacy.terminal.ScreenSize;
import org.openlegacy.terminal.TerminalSnapshot;
import org.openlegacy.terminal.definitions.NavigationDefinition;
import org.openlegacy.terminal.definitions.ScreenEntityDefinition;
import org.openlegacy.terminal.definitions.ScreenFieldDefinition;
import org.openlegacy.terminal.definitions.ScreenPartEntityDefinition;
import org.openlegacy.terminal.definitions.ScreenTableDefinition;
import org.openlegacy.terminal.spi.ScreenIdentification;
import org.openlegacy.terminal.support.SimpleScreenSize;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class CodeBasedScreenEntityDefinition implements ScreenEntityDefinition {

	private ScreenPojoCodeModel codeModel;
	private Map<String, ScreenPartEntityDefinition> partDefinitions = new TreeMap<String, ScreenPartEntityDefinition>();
	private Map<String, ScreenFieldDefinition> fields;
	private SimpleScreenSize screenSize;
	private Map<String, ScreenTableDefinition> tableDefinitions = new TreeMap<String, ScreenTableDefinition>();
	private List<ActionDefinition> actions;

	public CodeBasedScreenEntityDefinition(ScreenPojoCodeModel codeModel) {
		this.codeModel = codeModel;
	}

	public String getEntityName() {
		return codeModel.getEntityName();
	}

	public String getPackageName() {
		return codeModel.getPackageName();
	}

	public String getDisplayName() {
		return codeModel.getDisplayName();
	}

	public Class<?> getEntityClass() {
		throwNotImplemented();
		return null;
	}

	public String getEntityClassName() {
		return codeModel.getClassName();
	}

	public Class<? extends EntityType> getType() {
		throwNotImplemented();
		return null;
	}

	public Map<String, ScreenFieldDefinition> getFieldsDefinitions() {
		if (fields == null) {
			fields = CodeBasedDefinitionUtils.getFieldsFromCodeModel(codeModel, null);
		}
		return fields;
	}

	private static void throwNotImplemented() throws UnsupportedOperationException {
		throw (new NotImplementedException("Code based screen entity has not implemented this method"));
	}

	public ScreenFieldDefinition getFirstFieldDefinition(Class<? extends FieldType> fieldType) {
		throwNotImplemented();
		return null;
	}

	public ScreenIdentification getScreenIdentification() {
		throwNotImplemented();
		return null;
	}

	public NavigationDefinition getNavigationDefinition() {
		throwNotImplemented();
		return null;
	}

	public Map<String, ScreenTableDefinition> getTableDefinitions() {
		return tableDefinitions;
	}

	public Map<String, ScreenPartEntityDefinition> getPartsDefinitions() {
		return partDefinitions;
	}

	public List<ActionDefinition> getActions() {
		if (actions == null) {
			actions = CodeBasedDefinitionUtils.getActionsFromCodeModel(codeModel);
		}
		return actions;
	}

	public TerminalSnapshot getSnapshot() {
		throwNotImplemented();
		return null;
	}

	public TerminalSnapshot getOriginalSnapshot() {
		throwNotImplemented();
		return null;
	}

	public boolean isWindow() {
		throwNotImplemented();
		return false;
	}

	public ScreenEntityDefinition getAccessedFromScreenDefinition() {
		throwNotImplemented();
		return null;
	}

	public TerminalSnapshot getAccessedFromSnapshot() {
		throwNotImplemented();
		return null;
	}

	public ScreenSize getScreenSize() {
		if (screenSize == null) {
			screenSize = new SimpleScreenSize();
		}
		return screenSize;
	}

	public String getTypeName() {
		return codeModel.getTypeName();
	}

	public List<ScreenEntityDefinition> getChildScreensDefinitions() {
		throwNotImplemented();
		return null;
	}

}
