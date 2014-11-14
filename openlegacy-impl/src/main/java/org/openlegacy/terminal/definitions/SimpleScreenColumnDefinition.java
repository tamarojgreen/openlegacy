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

import org.openlegacy.definitions.AbstractColumnDefinition;
import org.openlegacy.definitions.TableDefinition.ColumnDefinition;
import org.openlegacy.terminal.FieldAttributeType;
import org.openlegacy.terminal.definitions.ScreenTableDefinition.ScreenColumnDefinition;

import java.io.Serializable;

public class SimpleScreenColumnDefinition extends AbstractColumnDefinition implements ScreenColumnDefinition, Serializable {

	private static final long serialVersionUID = 1L;

	private int startColumn;
	private int endColumn;

	private boolean selectionField;
	String expression;

	private FieldAttributeType attribute;

	public SimpleScreenColumnDefinition(String name) {
		super(name);
	}

	@Override
	public int getStartColumn() {
		return startColumn;
	}

	public void setStartColumn(int startColumn) {
		this.startColumn = startColumn;
	}

	@Override
	public int getEndColumn() {
		return endColumn;
	}

	public void setEndColumn(int endColumn) {
		this.endColumn = endColumn;
	}

	@Override
	public boolean isSelectionField() {
		return selectionField;
	}

	public void setSelectionField(boolean selectionField) {
		this.selectionField = selectionField;
	}

	@Override
	public int getLength() {
		return getEndColumn() - getStartColumn() + 1;
	}

	@Override
	public int compareTo(ColumnDefinition other) {
		if (!(other instanceof ScreenColumnDefinition)) {
			return -1;
		}
		ScreenColumnDefinition otherColumn = (ScreenColumnDefinition)other;

		return getStartColumn() - otherColumn.getStartColumn();
	}

	@Override
	public FieldAttributeType getAttribute() {
		return attribute;
	}

	public void setAttribute(FieldAttributeType attribute) {
		this.attribute = attribute;
	}

	@Override
	public String getExpression() {
		return expression;
	}

	public void setExpression(String expression) {
		this.expression = expression;
	}

}
