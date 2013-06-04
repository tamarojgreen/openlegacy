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
package org.openlegacy.definitions.support;

import org.openlegacy.FieldType;
import org.openlegacy.definitions.FieldDefinition;
import org.openlegacy.definitions.FieldTypeDefinition;

import java.io.Serializable;

public abstract class AbstractFieldDefinition<D extends FieldDefinition> implements FieldDefinition, Serializable {

	private static final long serialVersionUID = 1L;

	private String name;
	private String sampleValue;
	private Class<? extends FieldType> type;
	private String displayName;
	private FieldTypeDefinition fieldTypeDefinition;
	private boolean key;

	private boolean password;
	private boolean editable;

	private String helpText;

	/**
	 * for serialization purpose only
	 */
	public AbstractFieldDefinition() {}

	private Class<?> javaType;
	private String javaTypeName;

	private boolean rightToLeft;

	public AbstractFieldDefinition(String name, Class<? extends FieldType> type) {
		this.name = name;
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Class<? extends FieldType> getType() {
		return type;
	}

	public void setType(Class<? extends FieldType> type) {
		this.type = type;
	}

	public String getTypeName() {
		if (type == null) {
			return null;
		}
		return type.getSimpleName();
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public FieldTypeDefinition getFieldTypeDefinition() {
		return fieldTypeDefinition;
	}

	public void setFieldTypeDefinition(FieldTypeDefinition fieldTypeDefinition) {
		this.fieldTypeDefinition = fieldTypeDefinition;
	}

	public boolean isKey() {
		return key;
	}

	public void setKey(boolean key) {
		this.key = key;
	}

	public String getHelpText() {
		return helpText;
	}

	public void setHelpText(String helpText) {
		this.helpText = helpText;
	}

	public String getSampleValue() {
		return sampleValue;
	}

	public void setSampleValue(String sampleValue) {
		this.sampleValue = sampleValue;
	}

	public Class<?> getJavaType() {
		if (javaType == null) {
			javaType = String.class;
		}
		return javaType;
	}

	public String getJavaTypeName() {
		if (javaTypeName != null) {
			return javaTypeName;
		}
		return getJavaType().getSimpleName();
	}

	public void setJavaType(Class<?> javaType) {
		this.javaType = javaType;
	}

	/*
	 * Used for design-time to set a class name which doesn't exists during generation
	 */
	public void setJavaTypeName(String javaTypeName) {
		this.javaTypeName = javaTypeName;
	}

	public boolean isPassword() {
		return password;
	}

	public void setPassword(boolean password) {
		this.password = password;
	}

	public boolean isEditable() {
		return editable;
	}

	public void setEditable(boolean editable) {
		this.editable = editable;
	}

	public boolean isRightToLeft() {
		return rightToLeft;
	}

	public void setRightToLeft(boolean rightToLeft) {
		this.rightToLeft = rightToLeft;
	}

}
