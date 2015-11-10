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
import org.openlegacy.rpc.RpcFields;
import org.openlegacy.rpc.RpcStructureListField;
import org.openlegacy.rpc.RpcStructureNotMappedException;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlType;

@XmlType
@XmlAccessorType(XmlAccessType.FIELD)
public class SimpleRpcStructureListField extends AbstractRpcStructure implements RpcStructureListField {

	private static final long serialVersionUID = 1L;

	private Integer length;

	@XmlAttribute
	private Direction direction;

	@XmlElements({ @XmlElement(name = "record", type = SimpleRpcFields.class) })
	private List<RpcFields> childrens = new ArrayList<RpcFields>();

	@Override
	public Integer count() {
		return childrens.size();
	}

	@Override
	public Integer getLength() {

		if (length != null) {
			return length;
		} else {
			length = 0;
		}
		// all records in list have the same size
		List<RpcField> firstRecord = childrens.get(0).getFields();

		for (RpcField rpcField : firstRecord) {

			length += (rpcField.getLength());
		}
		length *= childrens.size();

		return length;
	}

	@Override
	public Direction getDirection() {
		if (direction != null) {
			return direction;
		}

		if (direction == null && childrens.size() > 0) {
			Boolean input = false;
			Boolean output = false;

			// all records in list have the same structure
			List<RpcField> firstRecord = childrens.get(0).getFields();
			for (RpcField rpcField : firstRecord) {

				Direction itemDirection = rpcField.getDirection();
				if (itemDirection == Direction.INPUT || itemDirection == Direction.INPUT_OUTPUT) {
					input = true;
				}
				if (itemDirection == Direction.OUTPUT || itemDirection == Direction.INPUT_OUTPUT) {
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

	@Override
	public Class<?> getType() {

		return SimpleRpcStructureListField.class;
	}

	@Override
	public List<RpcField> getChildren(int i) {
		return childrens.get(i).getFields();
	}

	@Override
	public List<RpcFields> getChildrens() {
		return childrens;
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

	@Override
	public int depth(int level, int maxDef) throws RpcStructureNotMappedException {
		if (level >= maxDef) {
			throw (new RpcStructureNotMappedException("Field: " + getName() + "excided max depth" + maxDef));
		}
		int maxtField = 0;
		for (RpcField rpcField : getChildren(0)) {
			maxtField = Math.max(maxtField, rpcField.depth(level + 1, maxDef));
		}
		return maxtField + 1;
	}

	@Override
	public boolean isContainer() {
		// Only simple structure can have container
		return false;
	}

	@Override
	public Boolean isVirtual() {
		// only simple structure can be virtual
		return null;
	}

}
