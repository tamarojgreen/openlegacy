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
package org.openlegacy.rpc.support;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.openlegacy.annotations.rpc.Direction;
import org.openlegacy.exceptions.OpenLegacyRuntimeException;
import org.openlegacy.rpc.RpcField;
import org.openlegacy.rpc.RpcStructureField;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

@XmlType
@XmlAccessorType(XmlAccessType.FIELD)
public class SimpleRpcStructureField implements RpcStructureField {

	private static final long serialVersionUID = 1L;

	@XmlAttribute
	private String name = null;

	@XmlElementWrapper
	@XmlElements({ @XmlElement(name = "field", type = SimpleRpcFlatField.class),
			@XmlElement(name = "structure", type = SimpleRpcStructureField.class) })
	private List<RpcField> children = new ArrayList<RpcField>();

	@XmlTransient
	private int order;

	@XmlAttribute
	private Direction direction;

	private Integer length;

	public String getName() {
		return name;
	}

	public Direction getDirection() {
		return direction;
	}

	public void setDirection(Direction direction) {
		this.direction = direction;
	}

	public List<RpcField> getChildren() {
		return children;
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

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
}
