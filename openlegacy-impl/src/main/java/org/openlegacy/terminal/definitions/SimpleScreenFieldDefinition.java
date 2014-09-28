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
package org.openlegacy.terminal.definitions;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.openlegacy.FieldType;
import org.openlegacy.definitions.FieldDefinition;
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
	private TerminalPosition labelPosition;
	private TerminalField terminalField;
	private TerminalField terminalLabelfield;
	// Used for design-time to set a class name which doesn't exists during generation
	private FieldAttributeType attribute;
	private boolean rectangle;

	private TerminalPosition endPosition;

	private String whenFilter;
	private String unlessFilter;

	private ScreenFieldDefinition descriptionFieldDefinition;

	private boolean internal;
	private boolean global;
	private String nullValue;

	private boolean tableKey;

	private boolean forceUpdate;

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

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	public int compareTo(ScreenFieldDefinition o) {
		return SnapshotUtils.comparePositions(this.getPosition(), o.getPosition(), false);
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

	public TerminalPosition getLabelPosition() {
		return labelPosition;
	}

	public void setLabelPosition(TerminalPosition labelPosition) {
		this.labelPosition = labelPosition;
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

	public ScreenFieldDefinition getDescriptionFieldDefinition() {
		return descriptionFieldDefinition;
	}

	public void setDescriptionFieldDefinition(ScreenFieldDefinition descriptionFieldDefinition) {
		this.descriptionFieldDefinition = descriptionFieldDefinition;
	}

	@Override
	public boolean isInternal() {
		return internal;
	}

	@Override
	public void setInternal(boolean internal) {
		this.internal = internal;
	}

	@Override
	public boolean isGlobal() {
		return global;
	}

	@Override
	public void setGlobal(boolean global) {
		this.global = global;
	}

	@Override
	public String getNullValue() {
		return nullValue;
	}

	@Override
	public void setNullValue(String nullValue) {
		this.nullValue = nullValue;
	}

	public int compareTo(FieldDefinition o) {
		return 0;
	}

	public boolean isTableKey() {
		return tableKey;
	}

	public void setTableKey(boolean tableKey) {
		this.tableKey = tableKey;
	}

	public boolean isForceUpdate() {
		return forceUpdate;
	}

	public void setForceUpdate(boolean forceUpdate) {
		this.forceUpdate = forceUpdate;
	}

}
