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
import org.openlegacy.FieldType;
import org.openlegacy.annotations.rpc.Direction;
import org.openlegacy.definitions.FieldTypeDefinition;
import org.openlegacy.definitions.RpcNumericFieldTypeDefinition;
import org.openlegacy.definitions.support.AbstractFieldDefinition;

public class SimpleRpcFieldDefinition extends AbstractFieldDefinition<RpcFieldDefinition> implements RpcFieldDefinition {

	private static final long serialVersionUID = 1L;
	private Integer length;
	private Direction direction;
	private String originalName;
	private int keyIndex;
	private int order;
	private String defaultValue;

	public SimpleRpcFieldDefinition(String name, Class<? extends FieldType> type) {
		super(name, type);
	}

	public Integer getLength() {
		return length;
	}

	public void setLength(Integer length) {
		this.length = length;
	}

	public Direction getDirection() {
		return direction;
	}

	public void setDirection(Direction direction) {
		this.direction = direction;
	}

	public String getOriginalName() {
		return originalName;
	}

	public void setOriginalName(String originalName) {
		this.originalName = originalName;
	}

	public int getKeyIndex() {
		return keyIndex;
	}

	public void setKeyIndex(int keyIndex) {
		this.keyIndex = keyIndex;
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	public Integer getDecimalPlaces() {

		Integer result = new Integer(0);
		FieldTypeDefinition fieldTypeDefinition = getFieldTypeDefinition();
		if (fieldTypeDefinition instanceof RpcNumericFieldTypeDefinition) {
			result = ((RpcNumericFieldTypeDefinition)fieldTypeDefinition).getDecimalPlaces();
		}
		return result;
	}
}
