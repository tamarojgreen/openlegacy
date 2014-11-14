package com.openlegacy.enterprise.ide.eclipse.editors.models.rpc;

import org.openlegacy.annotations.rpc.RpcNavigation;
import org.openlegacy.designtime.rpc.generators.support.CodeBasedRpcEntityDefinition;
import org.openlegacy.rpc.definitions.RpcNavigationDefinition;

import java.util.UUID;

/**
 * @author Ivan Bort
 * 
 */
public class RpcNavigationModel extends RpcNamedObject {

	// NOTE: if someone will add new annotation attributes, he must handle a logic for adding/removing annotation from *.java in
	// com.openlegacy.enterprise.ide.eclipse.editors.utils.rpc.RpcEntityUtils.ActionGenerator.generateRpcNavigationActions(RpcEntity
	// entity, RpcNavigationModel model)

	// annotation attributes
	private String category = "";

	public RpcNavigationModel() {
		super(RpcNavigation.class.getSimpleName(), null);
	}

	public RpcNavigationModel(UUID uuid) {
		super(RpcNavigation.class.getSimpleName(), null);
		this.uuid = uuid;
	}

	@Override
	public void init(CodeBasedRpcEntityDefinition entityDefinition) {
		RpcNavigationDefinition navigationDefinition = entityDefinition.getNavigationDefinition();
		if (navigationDefinition != null) {
			category = navigationDefinition.getCategory();
		}
	}

	@Override
	public RpcNavigationModel clone() {
		RpcNavigationModel model = new RpcNavigationModel(this.uuid);
		model.setCategory(this.category);
		return model;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

}
