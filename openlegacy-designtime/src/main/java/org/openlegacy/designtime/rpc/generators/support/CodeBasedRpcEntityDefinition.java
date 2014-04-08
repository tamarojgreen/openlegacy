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
package org.openlegacy.designtime.rpc.generators.support;

import org.openlegacy.EntityDefinition;
import org.openlegacy.annotations.rpc.Languages;
import org.openlegacy.definitions.ActionDefinition;
import org.openlegacy.definitions.FieldDefinition;
import org.openlegacy.definitions.PartEntityDefinition;
import org.openlegacy.definitions.RpcActionDefinition;
import org.openlegacy.designtime.rpc.generators.RpcPojoCodeModel;
import org.openlegacy.designtime.terminal.generators.support.AbstractCodeBasedEntityDefinition;
import org.openlegacy.rpc.RpcActions;
import org.openlegacy.rpc.definitions.RpcEntityDefinition;
import org.openlegacy.rpc.definitions.RpcFieldDefinition;
import org.openlegacy.rpc.definitions.RpcNavigationDefinition;
import org.openlegacy.rpc.definitions.RpcPartEntityDefinition;
import org.openlegacy.rpc.support.RpcOrderFieldComparator;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CodeBasedRpcEntityDefinition extends AbstractCodeBasedEntityDefinition<RpcFieldDefinition, RpcPojoCodeModel> implements RpcEntityDefinition {

	private List<ActionDefinition> actions;
	private Map<String, RpcFieldDefinition> fields;

	private List<RpcFieldDefinition> keyFields;

	public CodeBasedRpcEntityDefinition(RpcPojoCodeModel codeModel, File packageDir) {
		super(codeModel, packageDir);
	}

	public Set<EntityDefinition<?>> getAllChildEntitiesDefinitions() {
		return new HashSet<EntityDefinition<?>>();
	}

	@Override
	public List<ActionDefinition> getActions() {
		if (actions == null) {
			actions = RpcCodeBasedDefinitionUtils.getActionsFromCodeModel(getCodeModel(), getPackageDir());
		}
		return actions;
	}

	@Override
	public List<EntityDefinition<?>> getChildEntitiesDefinitions() {
		return new ArrayList<EntityDefinition<?>>();
	}

	@Override
	public Map<String, RpcFieldDefinition> getFieldsDefinitions() {
		if (fields == null) {
			fields = RpcCodeBasedDefinitionUtils.getFieldsFromCodeModel(getCodeModel(), null);
		}
		return fields;
	}

	public Map<String, RpcFieldDefinition> getAllFieldsDefinitions() {
		// TODO include parts
		return getFieldsDefinitions();
	}

	public List<RpcFieldDefinition> getSortedFields() {
		Collection<RpcFieldDefinition> fields = getFieldsDefinitions().values();
		List<RpcFieldDefinition> sortedFields = new ArrayList<RpcFieldDefinition>(fields);
		Collections.sort(sortedFields, new RpcOrderFieldComparator());
		return sortedFields;
	}

	public boolean isWindow() {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openlegacy.rpc.definitions.RpcEntityDefinition#getLanguage()
	 */
	public Languages getLanguage() {
		return getCodeModel().getLanguage();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<? extends FieldDefinition> getKeys() {
		if (keyFields != null) {
			return keyFields;
		}

		keyFields = new ArrayList<RpcFieldDefinition>();

		keyFields.addAll((Collection<? extends RpcFieldDefinition>)super.getKeys());

		Collection<PartEntityDefinition<RpcFieldDefinition>> parts = getPartsDefinitions().values();
		for (PartEntityDefinition<RpcFieldDefinition> part : parts) {
			RpcPartEntityDefinition rpcPart = (RpcPartEntityDefinition)part;
			addKeysFromParts(rpcPart.getInnerPartsDefinitions().values(), keyFields);
		}
		return keyFields;
	}

	private static void addKeysFromParts(Collection<RpcPartEntityDefinition> parts, List<RpcFieldDefinition> keyFields) {
		for (PartEntityDefinition<RpcFieldDefinition> part : parts) {
			RpcPartEntityDefinition rpcPart = (RpcPartEntityDefinition)part;
			for (RpcFieldDefinition rpcField : rpcPart.getFieldsDefinitions().values()) {
				if (rpcField.isKey()) {
					keyFields.add(rpcField);
				}
			}
			for (PartEntityDefinition<RpcFieldDefinition> innerPart : rpcPart.getInnerPartsDefinitions().values()) {
				RpcPartEntityDefinition rpcInnerPart = (RpcPartEntityDefinition)innerPart;
				addKeysFromParts(rpcInnerPart.getInnerPartsDefinitions().values(), keyFields);
			}
		}

	}

	public RpcNavigationDefinition getNavigationDefinition() {
		return null;
	}

	public boolean isValidateKeys() {
		return true;
	}

	public String getSourceCode() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getIdentification() {
		RpcActionDefinition actionDefinition = (RpcActionDefinition)getAction(RpcActions.READ.class);
		if (actionDefinition != null) {
			return actionDefinition.getProgramPath();
		}
		return null;
	}
}
