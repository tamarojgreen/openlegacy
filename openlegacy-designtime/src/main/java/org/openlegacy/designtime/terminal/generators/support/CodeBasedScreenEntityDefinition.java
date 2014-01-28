/*******************************************************************************
 * Copyright (c) 2014 OpenLegacy Inc.
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

public class CodeBasedScreenEntityDefinition extends AbstractCodeBasedEntityDefinition<ScreenFieldDefinition, ScreenPojoCodeModel> implements ScreenEntityDefinition {

	private Map<String, ScreenTableDefinition> tableDefinitions = new TreeMap<String, ScreenTableDefinition>();
	private List<EntityDefinition<?>> childScreens;
	private Set<EntityDefinition<?>> allChildScreens;
	private Map<String, ScreenFieldDefinition> fields;
	private List<ActionDefinition> actions;

	public CodeBasedScreenEntityDefinition(ScreenPojoCodeModel codeModel, File packageDir) {
		super(codeModel, packageDir);
	}

	@Override
	public String getEntityName() {
		return getCodeModel().getEntityName();
	}

	@Override
	public String getPackageName() {
		return getCodeModel().getPackageName();
	}

	@Override
	public String getDisplayName() {
		return getCodeModel().getDisplayName();
	}

	@Override
	public Class<?> getEntityClass() {
		throwNotImplemented();
		return null;
	}

	@Override
	public String getEntityClassName() {
		return getCodeModel().getClassName();
	}

	@Override
	public Class<? extends EntityType> getType() {
		throwNotImplemented();
		return null;
	}

	@Override
	public Map<String, ScreenFieldDefinition> getFieldsDefinitions() {
		if (fields == null) {
			fields = ScreenCodeBasedDefinitionUtils.getFieldsFromCodeModel(getCodeModel(), null);
		}
		return fields;
	}

	public Map<String, ScreenFieldDefinition> getAllFieldsDefinitions() {
		// TODO include parts
		return getFieldsDefinitions();
	}

	private static void throwNotImplemented() throws UnsupportedOperationException {
		throw (new NotImplementedException("Code based screen entity has not implemented this method"));
	}

	public ScreenIdentification getScreenIdentification() {
		// @author: Ivan Bort, refs assembla #112
		return (getCodeModel()).getScreenIdentification();
	}

	public NavigationDefinition getNavigationDefinition() {
		return (getCodeModel()).getNavigationDefinition();
	}

	public Map<String, ScreenTableDefinition> getTableDefinitions() {
		return tableDefinitions;
	}

	@Override
	public List<ActionDefinition> getActions() {
		if (actions == null) {
			actions = ScreenCodeBasedDefinitionUtils.getActionsFromCodeModel(getCodeModel());
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
		return (getCodeModel()).isWindow();
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
		return (getCodeModel()).getScreenSize();
	}

	@Override
	public String getTypeName() {
		return getCodeModel().getTypeName();
	}

	@Override
	public List<EntityDefinition<?>> getChildEntitiesDefinitions() {
		if (childScreens == null) {
			childScreens = ScreenCodeBasedDefinitionUtils.getChildScreensDefinitions(getCodeModel(), getPackageDir());
		}
		return childScreens;
	}

	public boolean isChild() {
		return getCodeModel().isChildScreen();
	}

	public void setChild(boolean child) {
		((DefaultScreenPojoCodeModel)getCodeModel()).setChildScreen(child);
	}

	public Set<EntityDefinition<?>> getAllChildEntitiesDefinitions() {
		if (allChildScreens == null) {
			allChildScreens = ScreenCodeBasedDefinitionUtils.getAllChildScreensDefinitions(getCodeModel(), getPackageDir());
		}
		return allChildScreens;
	}

	public List<ScreenFieldDefinition> getSortedFields() {
		Collection<ScreenFieldDefinition> fields = getFieldsDefinitions().values();
		List<ScreenFieldDefinition> sortedFields = new ArrayList<ScreenFieldDefinition>(fields);
		Collections.sort(sortedFields, TerminalPositionContainerComparator.instance());
		return sortedFields;
	}

	public List<ScreenEntityBinder> getBinders() {
		throwNotImplemented();
		return null;
	}

	public boolean isPerformDefaultBinding() {
		throwNotImplemented();
		return false;
	}

	@Override
	public List<? extends FieldDefinition> getFieldDefinitions(Class<? extends FieldType> fieldType) {
		throwNotImplemented();
		return null;
	}

	public boolean isSupportTerminalData() {
		return this.getCodeModel().isSupportTerminalData();
	}

	public boolean isValidateKeys() {
		return true;
	}

	public boolean isRightToLeft() {
		return false;
	}

}
