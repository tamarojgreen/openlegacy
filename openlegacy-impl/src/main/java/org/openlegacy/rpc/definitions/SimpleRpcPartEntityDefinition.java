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

import org.apache.commons.lang.builder.ToStringBuilder;
import org.openlegacy.definitions.AbstractPartEntityDefinition;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

public class SimpleRpcPartEntityDefinition extends AbstractPartEntityDefinition<RpcFieldDefinition> implements RpcPartEntityDefinition, Serializable {

	private static final long serialVersionUID = 1L;
	private String originalName;
	private int order;
	private int occur;

	private final Map<String, RpcPartEntityDefinition> innerPartsDefinitions = new LinkedHashMap<String, RpcPartEntityDefinition>();

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

	public int getOccur() {
		return occur;
	}

	public void setOccur(int occur) {
		this.occur = occur;
	}

	public Map<String, RpcPartEntityDefinition> getInnerPartsDefinitions() {
		return innerPartsDefinitions;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
