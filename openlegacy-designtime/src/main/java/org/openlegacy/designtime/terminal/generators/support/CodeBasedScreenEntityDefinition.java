/*******************************************************************************
 * Copyright (c) 2012 OpenLegacy Inc.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     OpenLegacy Inc. - initial API and implementation
 *******************************************************************************/
package org.openlegacy.designtime.terminal.generators.support;

import org.apache.commons.lang.NotImplementedException;
import org.openlegacy.EntityDefinition;
import org.openlegacy.EntityType;
import org.openlegacy.FieldType;
import org.openlegacy.definitions.ActionDefinition;
import org.openlegacy.definitions.FieldDefinition;
import org.openlegacy.definitions.PartEntityDefinition;
import org.openlegacy.designtime.terminal.generators.ScreenPojoCodeModel;
import org.openlegacy.terminal.ScreenEntityBinder;
import org.openlegacy.terminal.ScreenSize;
import org.openlegacy.terminal.TerminalSnapshot;
import org.openlegacy.terminal.definitions.NavigationDefinition;
import org.openlegacy.terminal.definitions.ScreenEntityDefinition;
import org.openlegacy.terminal.definitions.ScreenFieldDefinition;
import org.openlegacy.terminal.definitions.ScreenTableDefinition;
import org.openlegacy.terminal.services.ScreenIdentification;
import org.openlegacy.terminal.support.TerminalPositionContainerComparator;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class CodeBasedScreenEntityDefinition implements ScreenEntityDefinition {

	private ScreenPojoCodeModel codeModel;
	private Map<String, PartEntityDefinition<ScreenFieldDefinition>> partDefinitions = new TreeMap<String, PartEntityDefinition<ScreenFieldDefinition>>();
	private Map<String, ScreenFieldDefinition> fields;
	private Map<String, ScreenTableDefinition> tableDefinitions = new TreeMap<String, ScreenTableDefinition>();
	private List<ActionDefinition> actions;
	private List<EntityDefinition<?>> childScreens;
	private Set<EntityDefinition<?>> allChildScreens;
	private File packageDir;
	private List<ScreenFieldDefinition> keyFields;

	public CodeBasedScreenEntityDefinition(ScreenPojoCodeModel codeModel, File packageDir) {
		this.codeModel = codeModel;
		this.packageDir = packageDir;
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
		// @author: Ivan Bort, refs assembla #112
		return codeModel.getScreenIdentification();
	}

	public NavigationDefinition getNavigationDefinition() {
		return codeModel.getNavigationDefinition();
	}

	public Map<String, ScreenTableDefinition> getTableDefinitions() {
		return tableDefinitions;
	}

	public Map<String, PartEntityDefinition<ScreenFieldDefinition>> getPartsDefinitions() {
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
		return codeModel.isWindow();
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
		return codeModel.getScreenSize();
	}

	public String getTypeName() {
		return codeModel.getTypeName();
	}

	public List<EntityDefinition<?>> getChildEntitiesDefinitions() {
		if (childScreens == null) {
			childScreens = CodeBasedDefinitionUtils.getChildScreensDefinitions(codeModel, packageDir);
		}
		return childScreens;
	}

	public boolean isChild() {
		return codeModel.isChildScreen();
	}

	public Set<EntityDefinition<?>> getAllChildEntitiesDefinitions() {
		if (allChildScreens == null) {
			allChildScreens = CodeBasedDefinitionUtils.getAllChildScreensDefinitions(codeModel, packageDir);
		}
		return allChildScreens;
	}

	public List<ScreenFieldDefinition> getSortedFields() {
		Collection<ScreenFieldDefinition> fields = getFieldsDefinitions().values();
		List<ScreenFieldDefinition> sortedFields = new ArrayList<ScreenFieldDefinition>(fields);
		Collections.sort(sortedFields, TerminalPositionContainerComparator.instance());
		return sortedFields;
	}

	public List<? extends FieldDefinition> getKeys() {
		if (keyFields != null) {
			return keyFields;
		}
		keyFields = new ArrayList<ScreenFieldDefinition>();

		Collection<ScreenFieldDefinition> fieldsList = getFieldsDefinitions().values();
		keyFields = new ArrayList<ScreenFieldDefinition>();
		for (ScreenFieldDefinition screenFieldDefinition : fieldsList) {
			if (screenFieldDefinition.isKey()) {
				keyFields.add(screenFieldDefinition);
			}
		}
		return keyFields;
	}

	public List<ScreenEntityBinder> getBinders() {
		throwNotImplemented();
		return null;
	}

	public boolean isPerformDefaultBinding() {
		throwNotImplemented();
		return false;
	}

	public List<? extends FieldDefinition> getFieldDefinitions(Class<? extends FieldType> fieldType) {
		throwNotImplemented();
		return null;
	}

	public boolean isSupportTerminalData() {
		return this.codeModel.isSupportTerminalData();
	}

	public ActionDefinition getAction(Class<?> actionClass) {
		throwNotImplemented();
		return null;
	}
}
