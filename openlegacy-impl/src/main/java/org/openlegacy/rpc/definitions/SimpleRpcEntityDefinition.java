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
package org.openlegacy.rpc.definitions;

import org.openlegacy.annotations.rpc.Languages;
import org.openlegacy.definitions.FieldDefinition;
import org.openlegacy.definitions.PartEntityDefinition;
import org.openlegacy.definitions.RpcActionDefinition;
import org.openlegacy.definitions.support.AbstractEntityDefinition;
import org.openlegacy.rpc.actions.RpcActions;
import org.openlegacy.rpc.support.RpcOrderFieldComparator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SimpleRpcEntityDefinition extends AbstractEntityDefinition<RpcFieldDefinition> implements RpcEntityDefinition {

	private static final long serialVersionUID = 1L;
	private Languages language;
	private RpcNavigationDefinition navigationDefinition = new SimpleRpcNavigationDefinition();
	private String sourceCode;
	private List<RpcFieldDefinition> keyFields;

	public SimpleRpcEntityDefinition() {
		super(null, null);
	}

	public SimpleRpcEntityDefinition(String entityName, Class<?> entityClass) {
		super(entityName, entityClass);
	}

	public Languages getLanguage() {
		return language;
	}

	public void setLanguage(Languages language) {
		this.language = language;
	}

	public boolean isWindow() {
		return false;
	}

	public RpcNavigationDefinition getNavigationDefinition() {
		return navigationDefinition;
	}

	public List<OrderedField> getSortedFields() {

		List<OrderedField> result = new ArrayList<OrderedField>();

		result.addAll(getFieldsDefinitions().values());

		Collections.sort(result, new RpcOrderFieldComparator());
		return result;
	}

	public List<OrderedField> getAllFieldsDefinitionsSorted() {

		List<OrderedField> result = new ArrayList<OrderedField>();

		result.addAll(getFieldsDefinitions().values());

		Collection<PartEntityDefinition<RpcFieldDefinition>> parts = getPartsDefinitions().values();
		for (PartEntityDefinition<RpcFieldDefinition> p : parts) {
			result.add((SimpleRpcPartEntityDefinition)p);
		}

		Collections.sort(result, new RpcOrderFieldComparator());
		return result;
	}

	public String getSourceCode() {
		return sourceCode;
	}

	public void setSourceCode(String sourceCode) {
		this.sourceCode = sourceCode;
	}

	public String getIdentification() {

		RpcActionDefinition actionDefinition = (RpcActionDefinition)getAction(RpcActions.READ.class);
		if (actionDefinition != null) {
			return actionDefinition.getProgramPath();
		}
		return null;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<? extends FieldDefinition> getKeys() {

		if (keyFields == null) {
			keyFields = (List<RpcFieldDefinition>)super.getKeys();
			Collection<PartEntityDefinition<RpcFieldDefinition>> parts = getPartsDefinitions().values();
			for (PartEntityDefinition<RpcFieldDefinition> partDefinition : parts) {
				RpcPartEntityDefinition rpcPartEntityDefinition = (RpcPartEntityDefinition)partDefinition;
				if (rpcPartEntityDefinition.getCount() == 1) {
					keyFields.addAll(rpcPartEntityDefinition.getKeys());
				}
			}
			Collections.sort(keyFields, new Comparator<RpcFieldDefinition>() {

				public int compare(RpcFieldDefinition o1, RpcFieldDefinition o2) {
					return o1.getKeyIndex() - o2.getKeyIndex();
				}
			});
		}

		return keyFields;
	}
}
