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

import org.apache.commons.lang.builder.ToStringBuilder;
import org.openlegacy.definitions.AbstractPartEntityDefinition;
import org.openlegacy.definitions.ActionDefinition;
import org.openlegacy.definitions.PartEntityDefinition;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SimpleRpcPartEntityDefinition extends AbstractPartEntityDefinition<RpcFieldDefinition> implements RpcPartEntityDefinition, Serializable, OrderedField {

	private static final long serialVersionUID = 1L;
	private String originalName;
	private int order;
	private int count = 1;

	private String runtimeName;

	private final Map<String, RpcPartEntityDefinition> innerPartsDefinitions = new LinkedHashMap<String, RpcPartEntityDefinition>();
	private List<ActionDefinition> actions = new ArrayList<ActionDefinition>();
	private List<RpcFieldDefinition> keys = new ArrayList<RpcFieldDefinition>();

	public SimpleRpcPartEntityDefinition(Class<?> partClass) {
		super(partClass);
	}

	public String getOriginalName() {
		return originalName;
	}

	public void setOriginalName(String originalName) {
		this.originalName = originalName;
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	public int getCount() {
		return count;
	}

	public void getCount(int count) {
		this.count = count;
	}

	public Map<String, RpcPartEntityDefinition> getInnerPartsDefinitions() {
		return innerPartsDefinitions;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	public String getRuntimeName() {
		return runtimeName;
	}

	public void setRuntimeName(String runtimeName) {
		this.runtimeName = runtimeName;
	}

	public List<ActionDefinition> getActions() {
		return actions;
	}

	public List<RpcFieldDefinition> getKeys() {
		return keys;
	}

	public List<OrderedField> getSortedFields() {

		List<OrderedField> result = new ArrayList<OrderedField>();

		result.addAll(getFieldsDefinitions().values());
		Collection<RpcPartEntityDefinition> parts = innerPartsDefinitions.values();

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
