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

import java.util.ArrayList;
import java.util.List;
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
	 * @see com.openlegacy.enterprise.ide.eclipse.editors.models.enums.IEnumFieldModel#getPrevJavaTypeName()
	 */
	@Override
	public String getPrevJavaTypeName() {
		return prevJavaTypeName;
	}

	/* (non-Javadoc)
	 * @see com.openlegacy.enterprise.ide.eclipse.editors.models.enums.IEnumFieldModel#getEntries()
	 */
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

		model.prevJavaTypeName = this.prevJavaTypeName;

		List<EnumEntryModel> list = new ArrayList<EnumEntryModel>();
		for (EnumEntryModel entry : this.entries) {
			list.add(entry.clone());
		}
		model.getEntries().addAll(list);
		return model;

	}

}
