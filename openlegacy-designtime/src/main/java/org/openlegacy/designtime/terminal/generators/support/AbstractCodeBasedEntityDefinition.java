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
import org.openlegacy.definitions.PartEntityDefinition;
import org.openlegacy.designtime.generators.PojoCodeModel;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public abstract class AbstractCodeBasedEntityDefinition<F extends FieldDefinition, C extends PojoCodeModel> implements EntityDefinition<F> {

	private C codeModel;
	private Map<String, PartEntityDefinition<F>> partDefinitions = new TreeMap<String, PartEntityDefinition<F>>();
	private File packageDir;
	private List<FieldDefinition> keyFields;

	public AbstractCodeBasedEntityDefinition(C codeModel, File packageDir) {
		this.codeModel = codeModel;
		this.packageDir = packageDir;
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
	public abstract Map<String, F> getFieldsDefinitions();

	private static void throwNotImplemented() throws UnsupportedOperationException {
		throw (new NotImplementedException("Code based entity has not implemented this method"));
	}

	@Override
	public F getFirstFieldDefinition(Class<? extends FieldType> fieldType) {
		throwNotImplemented();
		return null;
	}

	@Override
	public Map<String, PartEntityDefinition<F>> getPartsDefinitions() {
		return partDefinitions;
	}

	@Override
	public abstract List<ActionDefinition> getActions();

	@Override
	public String getTypeName() {
		return getCodeModel().getTypeName();
	}

	@Override
	public abstract List<EntityDefinition<?>> getChildEntitiesDefinitions();

	@Override
	public List<? extends FieldDefinition> getKeys() {
		if (keyFields != null) {
			return keyFields;
		}
		keyFields = new ArrayList<FieldDefinition>();

		Collection<F> fieldsList = getFieldsDefinitions().values();
		keyFields = new ArrayList<FieldDefinition>();
		for (FieldDefinition fieldDefinition : fieldsList) {
			if (fieldDefinition.isKey()) {
				keyFields.add(fieldDefinition);
			}
		}
		return keyFields;
	}

	@Override
	public List<? extends FieldDefinition> getFieldDefinitions(Class<? extends FieldType> fieldType) {
		throwNotImplemented();
		return null;
	}

	@Override
	public ActionDefinition getAction(Class<?> actionClass) {
		throwNotImplemented();
		return null;
	}

	public C getCodeModel() {
		return codeModel;
	}

	public File getPackageDir() {
		return packageDir;
	}

	@Override
	public List<String> getRoles() {
		return Collections.emptyList();
	}

	public boolean isServiceInOut() {
		return getCodeModel().isServiceInOut();
	}

	@Override
	public boolean isEmpty() {
		return false;
	}
}
