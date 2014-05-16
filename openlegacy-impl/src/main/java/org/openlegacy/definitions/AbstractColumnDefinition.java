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
package org.openlegacy.definitions;

import org.openlegacy.definitions.TableDefinition.ColumnDefinition;

import java.io.Serializable;

public abstract class AbstractColumnDefinition implements ColumnDefinition, Serializable {

	private static final long serialVersionUID = 1L;

	private final String name;
	private int rowsOffset;

	private boolean key;
	private boolean editable;
	private String sampleValue;
	private String displayName;
	private Class<?> javaType;
	private String helpText;
	private boolean mainDisplayField;

	private int sortIndex;
	private Class<?> targetEntity;
	private String targetEntityClassName;

	private int colSpan;

	public AbstractColumnDefinition(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public boolean isKey() {
		return key;
	}

	public void setKey(boolean key) {
		this.key = key;
	}

	public boolean isEditable() {
		return editable;
	}

	public void setEditable(boolean editable) {
		this.editable = editable;
	}

	public String getSampleValue() {
		return sampleValue;
	}

	public void setSampleValue(String sampleValue) {
		this.sampleValue = sampleValue;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public Class<?> getJavaType() {
		return javaType;
	}

	public String getJavaTypeName() {
		if (javaType == null) {
			return String.class.getSimpleName();
		}
		return javaType.getSimpleName();
	}

	public void setJavaType(Class<?> javaType) {
		this.javaType = javaType;
	}

	public int getRowsOffset() {
		return rowsOffset;
	}

	public void setRowsOffset(int rowsOffset) {
		this.rowsOffset = rowsOffset;
	}

	public String getHelpText() {
		return helpText;
	}

	public void setHelpText(String helpText) {
		this.helpText = helpText;
	}

	public boolean isMainDisplayField() {
		return mainDisplayField;
	}

	public void setMainDisplayField(boolean mainDisplayField) {
		this.mainDisplayField = mainDisplayField;
	}

	public int getSortIndex() {
		return sortIndex;
	}

	public void setSortIndex(int sortIndex) {
		this.sortIndex = sortIndex;
	}

	public Class<?> getTargetEntity() {
		return targetEntity;
	}

	public void setTargetEntity(Class<?> targetEntity) {
		this.targetEntity = targetEntity;
	}

	public String getTargetEntityClassName() {
		return targetEntityClassName;
	}

	public void setTargetEntityClassName(String targetEntityClassName) {
		this.targetEntityClassName = targetEntityClassName;
	}

	public int compareTo(ColumnDefinition arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	public int getColSpan() {
		return colSpan;
	}

	public void setColSpan(int colSpan) {
		this.colSpan = colSpan;
	}
}
