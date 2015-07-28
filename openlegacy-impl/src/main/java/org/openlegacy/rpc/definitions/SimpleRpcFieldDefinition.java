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
import org.openlegacy.FieldType;
import org.openlegacy.annotations.rpc.Direction;
import org.openlegacy.definitions.FieldDefinition;
import org.openlegacy.definitions.FieldTypeDefinition;
import org.openlegacy.definitions.RpcNumericFieldTypeDefinition;
import org.openlegacy.definitions.support.AbstractFieldDefinition;

public class SimpleRpcFieldDefinition extends AbstractFieldDefinition<RpcFieldDefinition> implements RpcFieldDefinition {

	private static final long serialVersionUID = 1L;
	private Integer length;
	private Direction direction;
	private String originalName;
	private int order;
	private String defaultValue;
	private String runtimeName;
	private String nullValue;
	private String shortName;
	private String listElementName = "";

	private int count = 1;

	public SimpleRpcFieldDefinition(String name, Class<? extends FieldType> type) {
		super(name, type);
		if (name.indexOf('.') > 0) {
			shortName = name.substring(name.indexOf('.') + 1);
		} else {
			shortName = name;

		}
	}

	@Override
	public Integer getLength() {
		return length;
	}

	public void setLength(Integer length) {
		this.length = length;
	}

	@Override
	public Direction getDirection() {
		return direction;
	}

	public void setDirection(Direction direction) {
		this.direction = direction;
	}

	@Override
	public String getOriginalName() {
		return originalName;
	}

	public void setOriginalName(String originalName) {
		this.originalName = originalName;
	}

	@Override
	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	@Override
	public String getDefaultValue() {
		return defaultValue;
	}

	@Override
	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	@Override
	public Integer getDecimalPlaces() {

		Integer result = new Integer(0);
		FieldTypeDefinition fieldTypeDefinition = getFieldTypeDefinition();
		if (fieldTypeDefinition instanceof RpcNumericFieldTypeDefinition) {
			result = ((RpcNumericFieldTypeDefinition)fieldTypeDefinition).getDecimalPlaces();
		}
		return result;
	}

	@Override
	public int compareTo(FieldDefinition o) {
		return 0;
	}

	public String getRuntimeName() {
		return runtimeName;
	}

	public void setRuntimeName(String runtimeName) {
		this.runtimeName = runtimeName;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	@Override
	public String getNullValue() {
		return nullValue;
	}

	@Override
	public void setNullValue(String nullValue) {
		this.nullValue = nullValue;
	}

	@Override
	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	@Override
	public boolean isEditable() {
		return super.isEditable();
	}

	@Override
	public void setEditable(boolean editable) {
		super.setEditable(editable);
	}

	public String getListElementName() {
		return listElementName;
	}

	public void setListElementName(String listElementName) {
		this.listElementName = listElementName;
	}
}
