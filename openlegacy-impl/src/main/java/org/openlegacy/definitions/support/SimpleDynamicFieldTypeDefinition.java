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

import java.io.Serializable;

import org.openlegacy.definitions.DynamicFieldDefinition;

public class SimpleDynamicFieldTypeDefinition implements
		DynamicFieldDefinition, Serializable {

	public SimpleDynamicFieldTypeDefinition(String text, Integer row,
			Integer column, Integer endColumn, Integer endRow,
			Integer fieldOffset) {
		super();
		this.text = text;
		this.row = row;
		this.column = column;
		this.endColumn = endColumn;
		this.endRow = endRow;
		this.fieldOffset = fieldOffset;
	}

	private static final long serialVersionUID = 1L;

	private String text;

	private Integer row;

	private Integer column;

	private Integer endColumn;

	private Integer endRow;

	private Integer fieldOffset;


	@Override
	public String getText() {
		return text;
	}

	@Override
	public Integer getRow() {
		return row;
	}

	@Override
	public Integer getColumn() {
		return column;
	}

	@Override
	public Integer getEndColumn() {
		return endColumn;
	}

	@Override
	public Integer getEndRow() {
		return endRow;
	}

	@Override
	public Integer getFieldOffset() {
		return fieldOffset;
	}

}
