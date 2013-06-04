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

import org.openlegacy.annotations.rpc.Direction;
import org.openlegacy.exceptions.OpenLegacyRuntimeException;
import org.openlegacy.rpc.RpcField;

import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

@XmlType
@XmlAccessorType(XmlAccessType.FIELD)
public class SimpleRpcField implements RpcField {

	private static final long serialVersionUID = 1L;

	@XmlTransient
	private Object value;

	@XmlAttribute(name = "value")
	private String persistedValue;

	@XmlAttribute
	private Double length;

	/**
	 * NOTE! - All Boolean fields should have default value set. XML serializer checks if the default value change, and reset it
	 * to reduce XML size
	 */

	@XmlAttribute
	private Boolean modified = false;

	@XmlAttribute
	private Boolean editable = false;

	@XmlAttribute
	private Class<?> type = String.class;

	@XmlAttribute
	private String visualValue;

	@XmlAttribute
	private Boolean rightToLeft = false;

	@XmlTransient
	private Object originalValue;

	@XmlAttribute
	private Boolean password = false;

	@XmlAttribute
	private Direction direction;

	@XmlAttribute
	private String name = null;

	public Object getValue() {
		if (value == null) {
			value = persistedValue;
			if (value != null) {
				if (type == Integer.class) {
					value = Integer.parseInt(String.valueOf(value));
				} else if (type == Float.class) {
					value = Float.parseFloat(String.valueOf(value));
				} else if (type == Double.class) {
					value = Double.parseDouble(String.valueOf(value));
				}
			}
		}

		return value;
	}

	public void setValue(Object value, boolean modified) {
		if (modified) {
			this.originalValue = this.value;
		}
		this.modified = modified;
		if (value instanceof Integer) {
			this.value = BigDecimal.valueOf((Integer)value);
			this.type = Integer.class;
		} else if (value instanceof Long) {
			this.value = BigDecimal.valueOf((Long)value);
			this.type = Long.class;
		} else if (value instanceof Float) {
			this.value = BigDecimal.valueOf((Float)value);
			this.type = Float.class;
		} else {
			this.value = value;
		}
		if (value == null) {
			this.persistedValue = "";
		} else {
			this.persistedValue = String.valueOf(value);
		}

	}

	public void setValue(Object value) {
		setValue(value, true);
	}

	public Double getLength() {
		return length;
	}

	public void setLength(Double length) {
		this.length = length;
	}

	public void resetLength() {
		length = null;
	}

	public boolean isEditable() {
		return editable;
	}

	public void setEditable(boolean editable) {
		this.editable = editable;
	}

	public boolean isModified() {
		return modified;
	}

	public void setModified(boolean modified) {
		this.modified = modified;
	}

	public Class<?> getType() {

		if (type != String.class) {
			return type;
		}
		if (value != null) {
			return value.getClass();
		}
		return String.class;
	}

	public void setType(Class<?> type) {
		this.type = type;
	}

	@Override
	public RpcField clone() {
		try {
			return (RpcField)super.clone();
		} catch (CloneNotSupportedException e) {
			throw (new OpenLegacyRuntimeException(e));
		}
	}

	public String getVisualValue() {
		return visualValue;
	}

	public void setVisualValue(String visualValue) {
		this.visualValue = visualValue;
	}

	public Object getOriginalValue() {
		if (originalValue != null) {
			return originalValue;
		}
		return getValue();
	}

	public Object getDelegate() {
		return null;
	}

	public boolean isRightToLeft() {
		return rightToLeft;
	}

	public void setRightToLeft(boolean rightToLeft) {
		this.rightToLeft = rightToLeft;
	}

	public boolean isPassword() {
		return password;
	}

	public String getName() {
		return name;
	}

	public Direction getDirection() {
		return direction;
	}

	public void setDirection(Direction direction) {
		this.direction = direction;
	}
}
