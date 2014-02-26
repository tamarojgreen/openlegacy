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
package org.openlegacy.rpc.support;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.openlegacy.annotations.rpc.Direction;
import org.openlegacy.exceptions.OpenLegacyRuntimeException;
import org.openlegacy.rpc.RpcField;
import org.openlegacy.rpc.RpcStructureField;
import org.openlegacy.rpc.RpcStructureNotMappedException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlType;

@XmlType
@XmlAccessorType(XmlAccessType.FIELD)
public class SimpleRpcStructureField extends AbstractRpcStructure implements RpcStructureField {

	private static final long serialVersionUID = 1L;

	@XmlElementWrapper
	@XmlElements({ @XmlElement(name = "field", type = SimpleRpcFlatField.class),
			@XmlElement(name = "structure", type = SimpleRpcStructureField.class) })
	private List<RpcField> children = new ArrayList<RpcField>();

	private Integer length;

	@XmlAttribute
	private Direction direction;

	private boolean isContainer;

	private boolean virtual;

	private Map<Integer, Integer> orderCorelator;

	public Direction getDirection() {
		if (direction != null) {
			return direction;
		}
		if (direction == null && children.size() > 0) {
			Boolean input = false;
			Boolean output = false;

			for (RpcField rpcField : getChildren()) {

				Direction fieldDirection = rpcField.getDirection();
				if (fieldDirection == Direction.INPUT || fieldDirection == Direction.INPUT_OUTPUT) {
					input = true;
				}
				if (fieldDirection == Direction.OUTPUT || fieldDirection == Direction.INPUT_OUTPUT) {
					output = true;
				}
			}
			if (input && output) {
				direction = Direction.INPUT_OUTPUT;
			} else if (input) {
				direction = Direction.INPUT;

			} else if (output) {
				direction = Direction.OUTPUT;
			}
		}

		return direction;
	}

	public List<RpcField> getChildren() {
		return children;
	}

	@Override
	public Object getDelegate() {
		return null;
	}

	@Override
	public RpcField clone() {
		try {
			return (RpcField)super.clone();
		} catch (CloneNotSupportedException e) {
			throw (new OpenLegacyRuntimeException(e));
		}
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	public Integer getLength() {
		if (length != null) {
			return length;
		} else {
			length = 0;
		}

		for (RpcField rpcField : getChildren()) {
			length += rpcField.getLength();
		}
		return length;

	}

	public Class<?> getType() {
		return SimpleRpcStructureField.class;
	}

	public boolean isContainer() {
		return isContainer;
	}

	public void setIsContainer(Boolean isContainer) {
		this.isContainer = isContainer;
	}

	public int depth(int now, int maxDef) throws RpcStructureNotMappedException {

		if (now >= maxDef) {
			throw (new RpcStructureNotMappedException("Field: " + getName() + "excided max depth" + maxDef));
		}
		int maxtField = 0;
		for (RpcField rpcField : getChildren()) {
			maxtField = Math.max(maxtField, rpcField.depth(now + 1, maxDef));
		}
		return maxtField + 1;
	}

	public boolean isVirtual() {
		return virtual;
	}

	public void setVirtual(boolean virtual) {
		this.virtual = virtual;

	}

	public int getFieldRelativeOrder(int order) {
		if (virtual == false) {
			return order;
		}

		if (orderCorelator == null) {
			orderCorelator = new HashMap<Integer, Integer>();
			for (int i = 0; i < children.size(); i++) {
				orderCorelator.put(children.get(i).getOrder(), i);
			}
		}
		return orderCorelator.get(order);
	}
}
