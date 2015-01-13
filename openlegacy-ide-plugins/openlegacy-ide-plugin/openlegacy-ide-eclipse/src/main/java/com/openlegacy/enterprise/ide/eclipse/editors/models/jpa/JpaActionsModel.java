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

import org.openlegacy.annotations.db.DbActions;
import org.openlegacy.db.definitions.SimpleDbActionDefinition;
import org.openlegacy.definitions.ActionDefinition;
import org.openlegacy.designtime.db.generators.support.CodeBasedDbEntityDefinition;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author Ivan Bort
 * 
 */
public class JpaActionsModel extends JpaNamedObject {

	private List<ActionModel> actions = new ArrayList<ActionModel>();

	public JpaActionsModel() {
		super(DbActions.class.getSimpleName());
	}

	public JpaActionsModel(UUID uuid) {
		super(DbActions.class.getSimpleName());
		this.uuid = uuid;
	}

	@Override
	public void init(CodeBasedDbEntityDefinition dbEntityDefinition) {
		List<ActionDefinition> list = dbEntityDefinition.getActions();
		for (ActionDefinition actionDefinition : list) {
			if (actionDefinition instanceof SimpleDbActionDefinition) {
				SimpleDbActionDefinition definition = (SimpleDbActionDefinition)actionDefinition;
				actions.add(new ActionModel(definition.getActionName(), definition.getDisplayName(), definition.isGlobal(),
						definition.getAlias(), definition.getTargetEntityName()));
			}
		}
	}

	@Override
	public JpaActionsModel clone() {
		JpaActionsModel model = new JpaActionsModel(this.uuid);
		model.setModelName(this.modelName);
		List<ActionModel> list = new ArrayList<ActionModel>();
		for (ActionModel item : this.actions) {
			list.add(item.cloneModel());
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
