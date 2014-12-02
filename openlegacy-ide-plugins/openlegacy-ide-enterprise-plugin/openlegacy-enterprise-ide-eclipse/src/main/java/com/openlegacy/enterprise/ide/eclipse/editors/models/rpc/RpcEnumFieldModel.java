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

package com.openlegacy.enterprise.ide.eclipse.editors.models.rpc;

import com.openlegacy.enterprise.ide.eclipse.Messages;
import com.openlegacy.enterprise.ide.eclipse.editors.models.enums.EnumEntryModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.enums.IEnumFieldModel;

import org.openlegacy.DisplayItem;
import org.openlegacy.definitions.support.SimpleEnumFieldTypeDefinition;
import org.openlegacy.rpc.definitions.RpcFieldDefinition;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * @author Ivan Bort
 * 
 */
public class RpcEnumFieldModel extends RpcFieldModel implements IEnumFieldModel {

	private Class<?> type = null;
	private String prevJavaTypeName = "";
	private List<EnumEntryModel> entries = new ArrayList<EnumEntryModel>();

	public RpcEnumFieldModel(RpcNamedObject parent) {
		super(parent);
		this.javaTypeName = Messages.getString("type.enum");//$NON-NLS-1$
	}

	public RpcEnumFieldModel(UUID uuid, RpcNamedObject parent) {
		super(uuid, parent);
		this.javaTypeName = Messages.getString("type.enum");//$NON-NLS-1$
	}

	@Override
	public void init(RpcFieldDefinition rpcFieldDefinition) {
		super.init(rpcFieldDefinition);
		if (super.isInitialized()) {
			this.prevJavaTypeName = this.javaTypeName;
			SimpleEnumFieldTypeDefinition definition = (SimpleEnumFieldTypeDefinition)rpcFieldDefinition.getFieldTypeDefinition();
			Map<Object, DisplayItem> enums = definition.getEnums();
			Set<Object> keySet = enums.keySet();
			for (Object key : keySet) {
				EnumEntryModel entryModel = new EnumEntryModel(this);
				entryModel.setName((String)key);
				entryModel.setValue((String)enums.get(key).getValue());
				entryModel.setDisplayName((String)enums.get(key).getDisplay());
				this.entries.add(entryModel);
			}

		}
	}

	@Override
	public RpcEnumFieldModel clone() {
		// when cloning, innerBranchesCount should not be modified in parent
		int count = ((RpcNamedObject)this.parent).getInnerBranchesCount();

		RpcEnumFieldModel model = new RpcEnumFieldModel(uuid, (RpcNamedObject)parent);

		fillModel(model);

		((RpcNamedObject)this.parent).setInnerBranchesCount(count);

		model.prevJavaTypeName = this.prevJavaTypeName;
		List<EnumEntryModel> list = new ArrayList<EnumEntryModel>();
		for (EnumEntryModel entry : this.entries) {
			list.add(entry.clone());
		}
		model.getEntries().addAll(list);
		return model;
	}

	public Class<?> getType() {
		return type;
	}

	public void setType(Class<?> type) {
		this.type = type;
	}

	@Override
	public String getPrevJavaTypeName() {
		return prevJavaTypeName;
	}

	public void setPrevJavaTypeName(String prevJavaTypeName) {
		if (this.prevJavaTypeName.isEmpty()) {
			this.prevJavaTypeName = prevJavaTypeName;
		}
	}

	public void setJavaTypeName(String javaTypeName) {
		this.javaTypeName = javaTypeName;
		setPrevJavaTypeName(javaTypeName);
	}

	@Override
	public List<EnumEntryModel> getEntries() {
		return entries;
	}

}
