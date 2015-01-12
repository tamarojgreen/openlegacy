package com.openlegacy.enterprise.ide.eclipse.editors.models;

import org.openlegacy.db.definitions.DbFieldDefinition;
import org.openlegacy.designtime.db.generators.support.CodeBasedDbEntityDefinition;
import org.openlegacy.designtime.generators.CodeBasedScreenPartDefinition;
import org.openlegacy.designtime.rpc.generators.support.CodeBasedRpcEntityDefinition;
import org.openlegacy.designtime.rpc.generators.support.CodeBasedRpcPartDefinition;
import org.openlegacy.designtime.terminal.generators.support.CodeBasedScreenEntityDefinition;
import org.openlegacy.rpc.definitions.RpcFieldDefinition;
import org.openlegacy.terminal.definitions.ScreenFieldDefinition;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author Ivan Bort
 * 
 */
public abstract class NamedObject {

	protected String modelName;

	protected UUID uuid = UUID.randomUUID();

	protected NamedObject parent = null;

	private Map<String, String> validationMessages = new HashMap<String, String>();

	// RPC
	public abstract void init(CodeBasedRpcEntityDefinition entityDefinition);

	public abstract void init(RpcFieldDefinition rpcFieldDefinition);

	public abstract void init(CodeBasedRpcPartDefinition partDefinition);

	// Screen
	public abstract void init(CodeBasedScreenEntityDefinition entityDefinition);

	public abstract void init(ScreenFieldDefinition screenFieldDefinition);

	public abstract void init(CodeBasedScreenPartDefinition partDefinition);

	// JPA
	public abstract void init(CodeBasedDbEntityDefinition dbEntityDefinition);

	public abstract void init(DbFieldDefinition dbFieldDefinition);

	public NamedObject(String name) {
		this.modelName = name;
		this.parent = null;
	}

	public NamedObject(String name, NamedObject parent) {
		this.modelName = name;
		this.parent = parent;
	}

	@Override
	public String toString() {
		return this.getModelName();
	}

	public String getModelName() {
		return modelName;
	}

	public void setModelName(String name) {
		this.modelName = name;
	}

	public UUID getUUID() {
		return uuid;
	}

	public NamedObject getParent() {
		return parent;
	}

	public void setParent(NamedObject parent) {
		this.parent = parent;
	}

	public Map<String, String> getValidationMessages() {
		return validationMessages;
	}

}
