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

import java.util.UUID;

/**
 * @author Ivan Bort
 * 
 */
public class JpaManyToOneFieldModel extends JpaFieldModel {

	public JpaManyToOneFieldModel(NamedObject parent) {
		super(parent);
		this.javaTypeName = void.class.getSimpleName();
	}

	public JpaManyToOneFieldModel(UUID uuid, NamedObject parent) {
		super(uuid, parent);
		this.javaTypeName = void.class.getSimpleName();
	}

	@Override
	public JpaManyToOneFieldModel clone() {
		JpaManyToOneFieldModel model = new JpaManyToOneFieldModel(this.uuid, this.parent);
		model.setModelName(this.getModelName());
		model.setFieldName(this.getFieldName());
		model.previousFieldName = this.previousFieldName;
		model.javaTypeName = this.javaTypeName;
		model.setName(getName());
		model.setUnique(isUnique());
		model.setNullable(isNullable());
		model.setInsertable(isInsertable());
		model.setUpdatable(isUpdatable());
		model.setColumnDefinition(getColumnDefinition());
		model.setTable(getTable());
		model.setLength(getLength());
		model.setPrecision(getPrecision());
		model.setScale(getScale());
		model.setKey(isKey());

		model.setDisplayName(getDisplayName());
		model.setPassword(isPassword());
		model.setSampleValue(getSampleValue());
		model.setDefaultValue(getDefaultValue());
		model.setHelpText(getHelpText());
		model.setRightToLeft(isRightToLeft());
		model.setInternal(isInternal());
		model.setMainDisplayFiled(isMainDisplayFiled());

		model.setManyToOneModel(getManyToOneModel().clone());
		model.getManyToOneModel().setParent(model);
		model.setJoinColumnModel(getJoinColumnModel().clone());
		model.getJoinColumnModel().setParent(model);

		model.initialized = initialized;
		return model;
	}

	public void setJavaType(Class<?> clazz) {
		this.javaType = clazz;
	}

	public Class<?> getJavaType() {
		return this.javaType;
	}

	public void setJavaTypeName(String javaTypeName) {
		this.javaTypeName = javaTypeName;
	}

}
