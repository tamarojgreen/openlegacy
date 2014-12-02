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

package com.openlegacy.enterprise.ide.eclipse.editors.models.enums;

import com.openlegacy.enterprise.ide.eclipse.editors.models.NamedObject;

import org.openlegacy.db.definitions.DbFieldDefinition;
import org.openlegacy.designtime.db.generators.support.CodeBasedDbEntityDefinition;
import org.openlegacy.designtime.generators.CodeBasedScreenPartDefinition;
import org.openlegacy.designtime.rpc.generators.support.CodeBasedRpcEntityDefinition;
import org.openlegacy.designtime.rpc.generators.support.CodeBasedRpcPartDefinition;
import org.openlegacy.designtime.terminal.generators.support.CodeBasedScreenEntityDefinition;
import org.openlegacy.rpc.definitions.RpcFieldDefinition;
import org.openlegacy.terminal.definitions.ScreenFieldDefinition;

import java.util.UUID;

/**
 * @author Ivan Bort
 * 
 */
public class EnumEntryModel extends NamedObject {

	public EnumEntryModel(NamedObject parent) {
		super(EnumEntryModel.class.getSimpleName(), parent);
	}

	public EnumEntryModel(UUID uuid, NamedObject parent) {
		super(EnumEntryModel.class.getSimpleName(), parent);
		this.uuid = uuid;
	}

	@Override
	public void init(CodeBasedRpcEntityDefinition entityDefinition) {}

	@Override
	public void init(RpcFieldDefinition rpcFieldDefinition) {}

	@Override
	public void init(CodeBasedRpcPartDefinition partDefinition) {}

	@Override
	public void init(CodeBasedScreenEntityDefinition entityDefinition) {}

	@Override
	public void init(ScreenFieldDefinition screenFieldDefinition) {}

	@Override
	public void init(CodeBasedScreenPartDefinition partDefinition) {}

	@Override
	public void init(CodeBasedDbEntityDefinition dbEntityDefinition) {}

	@Override
	public void init(DbFieldDefinition dbFieldDefinition) {}

	private String name = "";
	private String value = "";
	private String displayName = "";

	@Override
	public EnumEntryModel clone() {
		EnumEntryModel model = new EnumEntryModel(this.uuid, this.parent);
		model.setName(this.name);
		model.setValue(this.value);
		model.setDisplayName(this.displayName);
		return model;
	}

	public boolean isAllowToSave() {
		return (this.name != null) && !this.name.isEmpty() && (this.value != null) && !this.value.isEmpty()
				&& (this.displayName != null) && !this.displayName.isEmpty();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

}
