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
import org.openlegacy.rpc.RpcFlatField;

import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

@XmlType
@XmlAccessorType(XmlAccessType.FIELD)
public class SimpleRpcFlatField implements RpcFlatField {

	private static final long serialVersionUID = 1L;

	@XmlTransient
	private Object value;

	private Object defaultValue = null;

	@XmlAttribute(name = "value")
	private String persistedValue;

	@XmlAttribute
	private Integer length;

	@XmlAttribute
	private Integer decimalPlaces = 0;

	@XmlTransient
	private Integer count = 1;

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
	private String name = "";

	@XmlTransient
	private String virtualGroup = "";

	@XmlAttribute
	private int order;

	public Object getValue() {
		if (value == null) {
			value = persistedValue;
			if (value != null) {
				if (type == Integer.class) {
					value = BigDecimal.valueOf(Integer.parseInt(String.valueOf(value)));
				} else if (type == Float.class) {
					value = BigDecimal.valueOf(Float.parseFloat(String.valueOf(value)));
				} else if (type == Double.class) {
					value = BigDecimal.valueOf(Double.parseDouble(String.valueOf(value)));
				}
			}
		}

		return value;
	}

	public void setDefaultValue(String value, Class<?> type) {
		this.type = type;
		if (type == Integer.class) {
			if (value.equals("")) {
				this.value = new Integer(0);
			} else {
				this.value = Integer.parseInt(value);
			}
		} else if (type == Long.class) {
			if (value.equals("")) {
				this.value = new Long(0);
			} else {
				this.value = Long.parseLong(value);
			}

		} else if (type == Float.class) {
			if (value.equals("")) {
				this.value = new Float(0);
			} else {
				this.value = Float.parseFloat(value);
			}
		} else {
			this.value = value;

		}
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

	public Object getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(Object defaultValue) {
		this.defaultValue = defaultValue;
	}

	public Integer getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public void resetLength() {
		length = null;
	}

	public Integer getDecimalPlaces() {
		return decimalPlaces;
	}

	public void setDecimalPlaces(Integer decimalPlaces) {
		this.decimalPlaces = decimalPlaces;
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
		if (value instanceof Byte) {
			return Byte.class;
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

	public void setName(String name) {
		this.name = name;
	}

	public Direction getDirection() {
		return direction;
	}

	public void setDirection(Direction direction) {
		this.direction = direction;
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	public Integer getCount() {

		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public int depth(int level, int maxDef) {

		return 1;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	public String getLegacyContainerName() {
		// Only structure can have container
		return null;
	}

	public boolean isContainer() {
		// Only structure can have container
		return false;
	}

	public Boolean isVirtual() {
		// only structure can be virtual
		return null;
	}

	public String getVirtualGroup() {
		return virtualGroup;
	}

	public void setVirtualGroup(String virtualGroup) {
		this.virtualGroup = virtualGroup;
	}

}