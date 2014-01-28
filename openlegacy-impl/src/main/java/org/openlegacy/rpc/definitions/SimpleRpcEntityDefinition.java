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
package org.openlegacy.rpc.definitions;

import org.openlegacy.annotations.rpc.Languages;
import org.openlegacy.definitions.PartEntityDefinition;
import org.openlegacy.definitions.support.AbstractEntityDefinition;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SimpleRpcEntityDefinition extends AbstractEntityDefinition<RpcFieldDefinition> implements RpcEntityDefinition {

	private static final long serialVersionUID = 1L;
	private Languages language;
	private RpcNavigationDefinition navigationDefinition = new SimpleRpcNavigationDefinition();

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

		Collection<PartEntityDefinition<RpcFieldDefinition>> parts = getPartsDefinitions().values();
		for (PartEntityDefinition<RpcFieldDefinition> p : parts) {
			result.add((SimpleRpcPartEntityDefinition)p);
		}

		Collections.sort(result, new Comparator<OrderedField>() {

			public int compare(OrderedField o1, OrderedField o2) {
				return o1.getOrder() - o2.getOrder();
			}
		});
		return result;
	}

}
