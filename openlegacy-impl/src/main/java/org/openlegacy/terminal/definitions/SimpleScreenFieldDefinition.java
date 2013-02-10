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
package org.openlegacy.terminal.definitions;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.openlegacy.FieldType;
import org.openlegacy.definitions.support.AbstractFieldDefinition;
import org.openlegacy.terminal.FieldAttributeType;
import org.openlegacy.terminal.TerminalField;
import org.openlegacy.terminal.TerminalPosition;
import org.openlegacy.terminal.support.SnapshotUtils;

import java.io.Serializable;

/**
 * Defines a mapping between a screenEntity java field name and it's screen position and length
 * 
 */
public class SimpleScreenFieldDefinition extends AbstractFieldDefinition<ScreenFieldDefinition> implements ScreenFieldDefinition, Serializable {

	private static final long serialVersionUID = 1L;

	private TerminalPosition position = null;
	private int length;
	private boolean editable;
	private String sampleValue;
	private Class<?> javaType;
	private TerminalPosition labelPosition;
	private boolean password;
	private TerminalField terminalField;
	private TerminalField terminalLabelfield;
	// Used for design-time to set a class name which doesn't exists during generation
	private String javaTypeName;
	private FieldAttributeType attribute;
	private boolean rectangle;

	private TerminalPosition endPosition;

	private boolean rightToLeft;
	private String whenFilter;
	private String unlessFilter;

	public SimpleScreenFieldDefinition() {
		super();
	}

	public SimpleScreenFieldDefinition(String name, Class<? extends FieldType> fieldType) {
		super(name, fieldType);
	}

	public TerminalPosition getPosition() {
		return position;
	}

	public void setPosition(TerminalPosition position) {
		this.position = position;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public int getLength() {
		return length;
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

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	public int compareTo(ScreenFieldDefinition o) {
		return SnapshotUtils.comparePositions(this.getPosition(), o.getPosition());
	}

	public TerminalPosition getEndPosition() {
		if (endPosition != null) {
			return endPosition;
		}
		return SnapshotUtils.getEndPosition(getPosition(), getLength());
	}

	public void setEndPosition(TerminalPosition endPosition) {
		this.endPosition = endPosition;
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

	public TerminalPosition getLabelPosition() {
		return labelPosition;
	}

	public void setLabelPosition(TerminalPosition labelPosition) {
		this.labelPosition = labelPosition;
	}

	public boolean isPassword() {
		return password;
	}

	public void setPassword(boolean password) {
		this.password = password;
	}

	public void setTerminalField(TerminalField terminalField) {
		this.terminalField = terminalField;
	}

	public TerminalField getTerminalField() {
		return terminalField;
	}

	public TerminalField getTerminalLabelField() {
		return terminalLabelfield;
	}

	public void setTerminalLabelField(TerminalField terminalLabelfield) {
		this.terminalLabelfield = terminalLabelfield;
	}

	public boolean isRectangle() {
		return rectangle;
	}

	public void setRectangle(boolean rectangle) {
		this.rectangle = rectangle;
	}

	public boolean isMultyLine() {
		return getEndPosition().getRow() != getPosition().getRow();
	}

	public boolean isRightToLeft() {
		return rightToLeft;
	}

	public void setRightToLeft(boolean rightToLeft) {
		this.rightToLeft = rightToLeft;
	}

	public FieldAttributeType getAttribute() {
		return attribute;
	}

	public void setAttribute(FieldAttributeType attribute) {
		this.attribute = attribute;
	}

	public String getWhenFilter() {
		return whenFilter;
	}

	public void setWhenFilter(String whenFilter) {
		this.whenFilter = whenFilter;
	}

	public String getUnlessFilter() {
		return unlessFilter;
	}

	public void setUnlessFilter(String unlessFilter) {
		this.unlessFilter = unlessFilter;
	}
}