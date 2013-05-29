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

import org.openlegacy.definitions.TableDefinition.ColumnDefinition;
import org.openlegacy.terminal.definitions.ScreenTableDefinition.ScreenColumnDefinition;

import java.io.Serializable;

public class SimpleScreenColumnDefinition implements ScreenColumnDefinition, Serializable {

	private static final long serialVersionUID = 1L;

	private String name;
	private int startColumn;
	private int endColumn;
	private int rowsOffset;

	private boolean key;
	private boolean editable;
	private String sampleValue;
	private String displayName;
	private boolean selectionField;
	private Class<?> javaType;
	private String helpText;
	private boolean mainDisplayField;

	private int colSpan;

	private int sortIndex;

	public SimpleScreenColumnDefinition(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public int getStartColumn() {
		return startColumn;
	}

	public void setStartColumn(int startColumn) {
		this.startColumn = startColumn;
	}

	public int getEndColumn() {
		return endColumn;
	}

	public void setEndColumn(int endColumn) {
		this.endColumn = endColumn;
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

	public boolean isSelectionField() {
		return selectionField;
	}

	public void setSelectionField(boolean selectionField) {
		this.selectionField = selectionField;
	}

	public Class<?> getJavaType() {
		return javaType;
	}

	public String getJavaTypeName() {
		return javaType.getSimpleName();
	}

	public void setJavaType(Class<?> javaType) {
		this.javaType = javaType;
	}

	public int getLength() {
		return getEndColumn() - getStartColumn() + 1;
	}

	public int compareTo(ColumnDefinition other) {
		if (!(other instanceof ScreenColumnDefinition)) {
			return -1;
		}
		ScreenColumnDefinition otherColumn = (ScreenColumnDefinition)other;

		return getStartColumn() - otherColumn.getStartColumn();
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

	public int getColSpan() {
		return colSpan;
	}

	public void setColSpan(int colSpan) {
		this.colSpan = colSpan;
	}

	public int getSortIndex() {
		return sortIndex;
	}

	public void setSortIndex(int sortIndex) {
		this.sortIndex = sortIndex;
	}
}
