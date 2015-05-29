/*******************************************************************************
 * Copyright (c) 2015 OpenLegacy Inc.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     OpenLegacy Inc. - initial API and implementation
 *******************************************************************************/
package com.openlegacy.enterprise.ide.eclipse.editors.models.jpa;

import com.openlegacy.enterprise.ide.eclipse.Messages;
import com.openlegacy.enterprise.ide.eclipse.editors.models.NamedObject;
import com.openlegacy.enterprise.ide.eclipse.editors.models.enums.EnumEntryModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.enums.IEnumFieldModel;

import org.openlegacy.DisplayItem;
import org.openlegacy.db.definitions.DbFieldDefinition;
import org.openlegacy.definitions.support.SimpleEnumFieldTypeDefinition;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * @author Aleksey Yeremeyev
 * 
 */
public class JpaEnumFieldModel extends JpaFieldModel implements IEnumFieldModel {

	private Class<?> type = null;
	private String prevJavaTypeName = "";
	private List<EnumEntryModel> entries = new ArrayList<EnumEntryModel>();

	/**
	 * @param parent
	 */
	public JpaEnumFieldModel(NamedObject parent) {
		super(parent);
		this.javaTypeName = Messages.getString("type.enum");
	}

	/**
	 * @param uuid
	 * @param parent
	 */
	public JpaEnumFieldModel(UUID uuid, NamedObject parent) {
		super(uuid, parent);
		this.uuid = uuid;
		this.javaTypeName = Messages.getString("type.enum");
	}

	/* (non-Javadoc)
	 * @see com.openlegacy.enterprise.ide.eclipse.editors.models.jpa.JpaFieldModel#init(org.openlegacy.db.definitions.DbFieldDefinition)
	 */
	@Override
	public void init(DbFieldDefinition dbFieldDefinition) {
		super.init(dbFieldDefinition);
		if (super.isInitialized()) {
			this.prevJavaTypeName = this.javaTypeName;
			SimpleEnumFieldTypeDefinition fieldTypeDefinition = (SimpleEnumFieldTypeDefinition) dbFieldDefinition.getFieldTypeDefinition();
			Map<Object, DisplayItem> map = fieldTypeDefinition.getEnums();
			Set<Object> keySet = map.keySet();
			for (Object key : keySet) {
				EnumEntryModel entryModel = new EnumEntryModel(this);
				entryModel.setName((String) key);
				entryModel.setValue((String) map.get(key).getValue());
				entryModel.setDisplayName((String) map.get(key).getDisplay());
				this.entries.add(entryModel);
			}
		}
	}

	@Override
	public String getPrevJavaTypeName() {
		return prevJavaTypeName;
	}

	@Override
	public List<EnumEntryModel> getEntries() {
		return entries;
	}

	/**
	 * @return the type
	 */
	public Class<?> getType() {
		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(Class<?> type) {
		this.type = type;
	}

	/* (non-Javadoc)
	 * @see com.openlegacy.enterprise.ide.eclipse.editors.models.jpa.JpaFieldModel#clone()
	 */
	@Override
	public JpaEnumFieldModel clone() {
		JpaEnumFieldModel model = new JpaEnumFieldModel(this.uuid, this.parent);

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
		model.setGeneratedValue(isGeneratedValue());

		model.setDisplayName(getDisplayName());
		model.setPassword(isPassword());
		model.setSampleValue(getSampleValue());
		model.setDefaultValue(getDefaultValue());
		model.setHelpText(getHelpText());
		model.setRightToLeft(isRightToLeft());
		model.setInternal(isInternal());
		model.setMainDisplayFiled(isMainDisplayFiled());

		model.initialized = isInitialized();

		model.prevJavaTypeName = this.prevJavaTypeName;

		List<EnumEntryModel> list = new ArrayList<EnumEntryModel>();
		for (EnumEntryModel entry : this.entries) {
			list.add(entry.clone());
		}
		model.getEntries().addAll(list);
		return model;
	}

	public void setPrevJavaTypeName(String typeName) {
		if (this.prevJavaTypeName.isEmpty()) {
			this.prevJavaTypeName = typeName;
		}
	}

	public void setJavaTypeName(String javaTypeName) {
		this.javaTypeName = javaTypeName;
		setPrevJavaTypeName(javaTypeName);
	}

}
