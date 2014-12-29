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

package org.openlegacy.db.definitions;

/**
 * @author Ivan Bort
 * 
 */
public class SimpleDbJoinColumnDefinition implements DbJoinColumnDefinition {

	private String name = "";
	private String referencedColumnName = "";
	private boolean unique = false;
	private boolean nullable = true;
	private boolean insertable = true;
	private boolean updatable = true;
	private String columnDefinition = "";
	private String table = "";

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getReferencedColumnName() {
		return referencedColumnName;
	}

	@Override
	public boolean isUnique() {
		return unique;
	}

	@Override
	public boolean isNullable() {
		return nullable;
	}

	@Override
	public boolean isInsertable() {
		return insertable;
	}

	@Override
	public boolean isUpdatable() {
		return updatable;
	}

	@Override
	public String getColumnDefinition() {
		return columnDefinition;
	}

	@Override
	public String getTable() {
		return table;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setReferencedColumnName(String referencedColumnName) {
		this.referencedColumnName = referencedColumnName;
	}

	public void setUnique(boolean unique) {
		this.unique = unique;
	}

	public void setNullable(boolean nullable) {
		this.nullable = nullable;
	}

	public void setInsertable(boolean insertable) {
		this.insertable = insertable;
	}

	public void setUpdatable(boolean updatable) {
		this.updatable = updatable;
	}

	public void setColumnDefinition(String columnDefinition) {
		this.columnDefinition = columnDefinition;
	}

	public void setTable(String table) {
		this.table = table;
	}

}
