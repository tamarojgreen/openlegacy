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
package org.openlegacy.definitions.support;

import org.openlegacy.FieldType;
import org.openlegacy.definitions.FieldDefinition;
import org.openlegacy.definitions.FieldTypeDefinition;
import org.openlegacy.utils.StringUtil;

import java.io.Serializable;

public abstract class AbstractFieldDefinition<D extends FieldDefinition> implements FieldDefinition, Serializable {

	private static final long serialVersionUID = 1L;

	private String name;
	private String sampleValue;
	private String defaultValue;
	private Class<? extends FieldType> type;
	private String displayName;
	private FieldTypeDefinition fieldTypeDefinition;
	private boolean key;

	private boolean password;
	private boolean editable;

	private String helpText;

	private boolean global;

	// @author Ivan Bort, refs assembla #112
	private String fieldTypeName;

	/**
	 * for serialization purpose only
	 */
	public AbstractFieldDefinition() {}

	private Class<?> javaType;
	private String javaTypeName;

	private boolean rightToLeft;

	private boolean internal = false;

	private String nullValue = null;

	private int keyIndex;

	private String expression;

	private boolean staticField = false;

	public AbstractFieldDefinition(String name, Class<? extends FieldType> type) {
		this.name = name;
		this.type = type;
	}

	@Override
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
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

	@Override
	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	@Override
	public FieldTypeDefinition getFieldTypeDefinition() {
		return fieldTypeDefinition;
	}

	public void setFieldTypeDefinition(FieldTypeDefinition fieldTypeDefinition) {
		this.fieldTypeDefinition = fieldTypeDefinition;
	}

	@Override
	public boolean isKey() {
		return key;
	}

	public void setKey(boolean key) {
		this.key = key;
	}

	@Override
	public String getHelpText() {
		return helpText;
	}

	public void setHelpText(String helpText) {
		this.helpText = StringUtil.escapingQuotes(helpText);
	}

	@Override
	public String getSampleValue() {
		return sampleValue;
	}

	public void setSampleValue(String sampleValue) {
		this.sampleValue = sampleValue;
	}

	@Override
	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	@Override
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

	protected boolean isEditable() {
		return editable;
	}

	protected void setEditable(boolean editable) {
		this.editable = editable;
	}

	@Override
	public boolean isRightToLeft() {
		return rightToLeft;
	}

	public void setRightToLeft(boolean rightToLeft) {
		this.rightToLeft = rightToLeft;
	}

	public String getFieldTypeName() {
		if (type != null) {
			return type.getSimpleName();
		}
		return fieldTypeName;
	}

	public void setFieldTypeName(String fieldTypeName) {
		this.fieldTypeName = fieldTypeName;
	}

	@Override
	public boolean isInternal() {
		return internal;
	}

	public void setInternal(boolean internal) {
		this.internal = internal;
	}

	@Override
	public boolean isGlobal() {
		return global;
	}

	public void setGlobal(boolean global) {
		this.global = global;
	}

	@Override
	public String getNullValue() {
		return nullValue;
	}

	public void setNullValue(String nullValue) {
		this.nullValue = nullValue;
	}

	@Override
	public int getKeyIndex() {
		return keyIndex;
	}

	public void setKeyIndex(int keyIndex) {
		this.keyIndex = keyIndex;
	}

	@Override
	public String getExpression() {
		return expression;
	}

	public void setExpression(String expression) {
		this.expression = expression;
	}

	public boolean isStaticField() {
		return staticField;
	}

	public void setStaticField(boolean staticField) {
		this.staticField = staticField;
	}

}
