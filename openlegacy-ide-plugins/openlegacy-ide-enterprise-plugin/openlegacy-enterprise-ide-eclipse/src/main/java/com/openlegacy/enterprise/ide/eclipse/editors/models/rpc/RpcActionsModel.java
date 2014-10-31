package com.openlegacy.enterprise.ide.eclipse.editors.models.rpc;

import org.openlegacy.annotations.rpc.RpcActions;
import org.openlegacy.definitions.ActionDefinition;
import org.openlegacy.designtime.rpc.generators.support.CodeBasedRpcEntityDefinition;
import org.openlegacy.rpc.definitions.SimpleRpcActionDefinition;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author Ivan Bort
 * 
 */
public class RpcActionsModel extends RpcNamedObject {

	private List<ActionModel> actions = new ArrayList<ActionModel>();

	public RpcActionsModel(UUID uuid) {
		super(RpcActions.class.getSimpleName(), null);
		this.uuid = uuid;
	}

	public RpcActionsModel() {
		super(RpcActions.class.getSimpleName(), null);
	}

	@Override
	public void init(CodeBasedRpcEntityDefinition entityDefinition) {
		List<ActionDefinition> list = entityDefinition.getActions();
		for (ActionDefinition actionDefinition : list) {
			if (actionDefinition instanceof SimpleRpcActionDefinition) {
				this.actions.add(new ActionModel(actionDefinition.getActionName(), actionDefinition.getDisplayName(),
						actionDefinition.getAlias(), ((SimpleRpcActionDefinition)actionDefinition).getProgramPath(),
						actionDefinition.isGlobal()));
			}
		}
	}

	@Override
	public RpcActionsModel clone() {
		RpcActionsModel model = new RpcActionsModel(this.uuid);
		model.setModelName(this.modelName);
		List<ActionModel> list = new ArrayList<ActionModel>();
		for (ActionModel item : this.actions) {
			ActionModel newAction = new ActionModel(item.getActionName(), item.getDisplayName(), item.getAlias(),
					item.getProgramPath(), item.isGlobal());
			list.add(newAction);
		}
		model.setActions(list);
		return model;
	}

	public List<ActionModel> getActions() {
		return actions;
	}

	public void setActions(List<ActionModel> actions) {
		this.actions = actions;
	}

}
