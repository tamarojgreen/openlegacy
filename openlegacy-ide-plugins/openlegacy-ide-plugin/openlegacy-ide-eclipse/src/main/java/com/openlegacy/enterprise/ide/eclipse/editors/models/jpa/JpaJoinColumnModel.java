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

package com.openlegacy.enterprise.ide.eclipse.editors.models.jpa;

import com.openlegacy.enterprise.ide.eclipse.editors.models.NamedObject;

import org.apache.commons.lang.StringUtils;
import org.openlegacy.db.definitions.DbJoinColumnDefinition;

import java.util.UUID;

import javax.persistence.JoinColumn;

/**
 * @author Ivan Bort
 * 
 */
public class JpaJoinColumnModel extends JpaNamedObject implements IJpaNamedObject {

	// annotation attributes
	private String name = "";
	private String referencedColumnName = "";
	private boolean unique = false;
	private boolean nullable = true;
	private boolean insertable = true;
	private boolean updatable = true;
	private String columnDefinition = "";
	private String table = "";

	public JpaJoinColumnModel(NamedObject parent) {
		super(JoinColumn.class.getSimpleName());
		this.parent = parent;
	}

	public JpaJoinColumnModel(UUID uuid, NamedObject parent) {
		super(JoinColumn.class.getSimpleName());
		this.parent = parent;
		this.uuid = uuid;
	}

	public void init(DbJoinColumnDefinition joinColumnDefinition) {
		if (joinColumnDefinition == null) {
			return;
		}
		name = joinColumnDefinition.getName();
		referencedColumnName = joinColumnDefinition.getReferencedColumnName();
		unique = joinColumnDefinition.isUnique();
		nullable = joinColumnDefinition.isNullable();
		insertable = joinColumnDefinition.isInsertable();
		updatable = joinColumnDefinition.isUpdatable();
		columnDefinition = joinColumnDefinition.getColumnDefinition();
		table = joinColumnDefinition.getTable();
	}

	@Override
	public JpaJoinColumnModel clone() {
		JpaJoinColumnModel model = new JpaJoinColumnModel(uuid, parent);
		model.setName(name);
		model.setReferencedColumnName(referencedColumnName);
		model.setUnique(unique);
		model.setNullable(nullable);
		model.setInsertable(insertable);
		model.setUpdatable(updatable);
		model.setColumnDefinition(columnDefinition);
		model.setTable(table);
		return model;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getReferencedColumnName() {
		return referencedColumnName;
	}

	public void setReferencedColumnName(String referencedColumnName) {
		this.referencedColumnName = referencedColumnName;
	}

	public boolean isUnique() {
		return unique;
	}

	public void setUnique(boolean unique) {
		this.unique = unique;
	}

	public boolean isNullable() {
		return nullable;
	}

	public void setNullable(boolean nullable) {
		this.nullable = nullable;
	}

	public boolean isInsertable() {
		return insertable;
	}

	public void setInsertable(boolean insertable) {
		this.insertable = insertable;
	}

	public boolean isUpdatable() {
		return updatable;
	}

	public void setUpdatable(boolean updatable) {
		this.updatable = updatable;
	}

	public String getColumnDefinition() {
		return columnDefinition;
	}

	public void setColumnDefinition(String columnDefinition) {
		this.columnDefinition = columnDefinition;
	}

	public String getTable() {
		return table;
	}

	public void setTable(String table) {
		this.table = table;
	}

	@Override
	public boolean isDefaultAttrs() {
		return StringUtils.isEmpty(name) && StringUtils.isEmpty(referencedColumnName) && StringUtils.isEmpty(columnDefinition)
				&& StringUtils.isEmpty(table) && !unique && nullable && insertable && updatable;
	}

	@Override
	public boolean equalsAttrs(IJpaNamedObject object) {
		if (object instanceof JpaJoinColumnModel) {
			JpaJoinColumnModel model = (JpaJoinColumnModel)object;
			return StringUtils.equals(name, model.getName())
					&& StringUtils.equals(referencedColumnName, model.getReferencedColumnName())
					&& StringUtils.equals(columnDefinition, model.getColumnDefinition())
					&& StringUtils.equals(table, model.getTable()) && unique == model.isUnique()
					&& nullable == model.isNullable() && insertable == model.isInsertable() && updatable == model.isUpdatable();
		}
		return false;
	}

}
