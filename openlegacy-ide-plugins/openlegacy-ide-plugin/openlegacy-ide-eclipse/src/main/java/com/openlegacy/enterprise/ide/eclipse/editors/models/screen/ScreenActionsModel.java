package com.openlegacy.enterprise.ide.eclipse.editors.models.screen;

import org.openlegacy.annotations.screen.ScreenActions;
import org.openlegacy.definitions.ActionDefinition;
import org.openlegacy.designtime.terminal.generators.support.CodeBasedScreenEntityDefinition;
import org.openlegacy.terminal.definitions.SimpleTerminalActionDefinition;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Represents @ScreenActions annotation
 * 
 * @author Ivan Bort
 * 
 */
public class ScreenActionsModel extends ScreenNamedObject {

	private List<ActionModel> actions = new ArrayList<ActionModel>();

	public ScreenActionsModel() {
		super(ScreenActions.class.getSimpleName());
	}

	public ScreenActionsModel(UUID uuid) {
		super(ScreenActions.class.getSimpleName());
		this.uuid = uuid;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.openlegacy.enterprise.ide.eclipse.editors.models.NamedObject#init(org.openlegacy.designtime.terminal.generators.support
	 * .CodeBasedScreenEntityDefinition)
	 */
	@Override
	public void init(CodeBasedScreenEntityDefinition entityDefinition) {
		List<ActionDefinition> list = entityDefinition.getActions();
		for (ActionDefinition actionDefinition : list) {
			if (actionDefinition instanceof SimpleTerminalActionDefinition) {
				SimpleTerminalActionDefinition definition = (SimpleTerminalActionDefinition)actionDefinition;
				this.actions.add(new ActionModel(definition.getActionName(), definition.getDisplayName(),
						actionDefinition.getAlias(), definition.getAdditionalKey(), definition.getPosition().getRow(),
						definition.getPosition().getColumn(), definition.getLength(), definition.getWhen(),
						definition.getFocusField(), definition.getType(), definition.getTargetEntityName(),
						definition.getSleep(), definition.isGlobal(), definition.getKeyboardKeyName()));
			}
		}
	}

	@Override
	public ScreenActionsModel clone() {
		ScreenActionsModel model = new ScreenActionsModel(this.uuid);
		model.setModelName(this.modelName);
		List<ActionModel> list = new ArrayList<ActionModel>();
		for (ActionModel item : this.actions) {
			ActionModel newAction = item.cloneModel();
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
