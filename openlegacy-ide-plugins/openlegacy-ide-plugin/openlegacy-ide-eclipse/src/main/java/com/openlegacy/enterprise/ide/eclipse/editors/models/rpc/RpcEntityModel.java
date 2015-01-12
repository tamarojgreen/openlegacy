package com.openlegacy.enterprise.ide.eclipse.editors.models.rpc;

import org.openlegacy.annotations.rpc.Languages;
import org.openlegacy.annotations.rpc.RpcEntity;
import org.openlegacy.designtime.rpc.generators.support.CodeBasedRpcEntityDefinition;
import org.openlegacy.exceptions.OpenLegacyRuntimeException;
import org.openlegacy.rpc.definitions.RpcFieldDefinition;

import java.util.UUID;

/**
 * @author Ivan Bort
 * 
 */
public class RpcEntityModel extends RpcNamedObject {

	protected String className;

	// annotation attributes
	private String name = "";
	private String displayName = "";
	private Languages language = Languages.UNDEFINED;

	public RpcEntityModel() {
		super(RpcEntity.class.getSimpleName(), null);
	}

	public RpcEntityModel(UUID uuid) {
		super(RpcEntity.class.getSimpleName(), null);
		this.uuid = uuid;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.openlegacy.enterprise.ide.eclipse.editors.rpc.models.NamedObject#init(org.openlegacy.designtime.rpc.generators.support
	 * .CodeBasedRpcEntityDefinition)
	 */
	@Override
	public void init(CodeBasedRpcEntityDefinition entityDefinition) {
		this.name = entityDefinition.getEntityName();
		this.displayName = entityDefinition.getDisplayName();
		this.language = entityDefinition.getLanguage();

		this.className = entityDefinition.getEntityClassName();
	}

	/**
	 * Method <code>init(RpcFieldDefinition rpcFieldDefinition)</code> is not supported by this model. Use
	 * <code>init(CodeBasedRpcEntityDefinition entityDefinition)</code> instead.
	 * 
	 * @throws OpenLegacyRuntimeException
	 */
	@Override
	public void init(RpcFieldDefinition rpcFieldDefinition) {
		throw new OpenLegacyRuntimeException(
				"Method init(RpcFieldDefinition rpcFieldDefinition) is not supported by this model. Use init(CodeBasedRpcEntityDefinition entityDefinition) instead.");//$NON-NLS-1$
	}

	@Override
	public RpcEntityModel clone() {
		RpcEntityModel model = new RpcEntityModel(this.uuid);
		model.setName(this.name);
		model.setDisplayName(this.displayName);
		model.setLanguage(this.language);
		return model;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public Languages getLanguage() {
		return language;
	}

	public void setLanguage(Languages language) {
		this.language = language;
	}

	public String getClassName() {
		return className;
	}

}
