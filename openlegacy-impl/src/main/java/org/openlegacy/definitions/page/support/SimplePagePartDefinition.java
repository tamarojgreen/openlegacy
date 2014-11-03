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
package org.openlegacy.definitions.page.support;

import org.openlegacy.definitions.FieldDefinition;
import org.openlegacy.definitions.TableDefinition;
import org.openlegacy.layout.PagePartRowDefinition;
import org.openlegacy.terminal.layout.PositionedPagePartDefinition;

import java.util.ArrayList;
import java.util.List;

public class SimplePagePartDefinition implements PositionedPagePartDefinition {

	private List<PagePartRowDefinition> rowParts = new ArrayList<PagePartRowDefinition>();
	private int width;
	private int topMargin;
	private int leftMargin;
	private int columns;
	private String tableFieldName;
	private TableDefinition<?> tableDefinition;
	private String displayName;
	private boolean relative = false;

	@Override
	public int getColumns() {
		return columns;
	}

	public void setColumns(int columns) {
		this.columns = columns;
	}

	@Override
	public List<PagePartRowDefinition> getPartRows() {
		return rowParts;
	}

	@Override
	public int getWidth() {
		return width;
	}

	@Override
	public int getTopMargin() {
		return topMargin;
	}

	@Override
	public int getLeftMargin() {
		return leftMargin;
	}

	public void setTopMargin(int topMargin) {
		this.topMargin = topMargin;
	}

	public void setLeftMargin(int leftMargin) {
		this.leftMargin = leftMargin;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	/**
	 * Used as free-marker helper method
	 * 
	 * @return
	 */
	public boolean isOneField() {
		return getPartRows().size() == 1 && getPartRows().get(0).getFields().size() == 1;
	}

	public boolean isMultyFields() {
		return !isOneField();
	}

	/**
	 * Used as free-marker helper method
	 * 
	 * @return
	 */
	public FieldDefinition getSingleField() {
		if (isOneField()) {
			return getPartRows().get(0).getFields().get(0);
		}
		return null;
	}

	@Override
	public String getTableFieldName() {
		return tableFieldName;
	}

	public void setTableFieldName(String tableFieldName) {
		this.tableFieldName = tableFieldName;
	}

	public void setTableDefinition(TableDefinition<?> tableDefinition) {
		this.tableDefinition = tableDefinition;
	}

	@Override
	public TableDefinition<?> getTableDefinition() {
		return tableDefinition;
	}

	/**
	 * Used as free-marker helper method
	 * 
	 * @return
	 */
	public boolean isHasTable() {
		return tableFieldName != null;
	}

	@Override
	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	@Override
	public boolean isRelative() {
		return relative;
	}

	public void setRelative(boolean relative) {
		this.relative = relative;
	}

	@Override
	public List<FieldDefinition> getFields() {
		List<FieldDefinition> fields = new ArrayList<FieldDefinition>();
		for (PagePartRowDefinition row : getPartRows()) {
			fields.addAll(row.getFields());
		}
		return fields;
	}
}
