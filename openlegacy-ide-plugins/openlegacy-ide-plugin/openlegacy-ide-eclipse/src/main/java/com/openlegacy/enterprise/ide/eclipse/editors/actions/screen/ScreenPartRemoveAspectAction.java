package com.openlegacy.enterprise.ide.eclipse.editors.actions.screen;

import com.openlegacy.enterprise.ide.eclipse.editors.actions.ActionType;
import com.openlegacy.enterprise.ide.eclipse.editors.models.screen.ScreenPartModel;

import java.util.UUID;

/**
 * @author Ivan Bort
 * 
 */
public class ScreenPartRemoveAspectAction extends ScreenPartAction {

	private String entityClassName;

	public ScreenPartRemoveAspectAction(UUID uuid, ScreenPartModel namedObject, ActionType actionType, int kind, String key,
			String entityClassName) {
		super(uuid, namedObject, actionType, kind, key, null);
		this.entityClassName = entityClassName;
	}

	public String getEntityClassName() {
		return entityClassName;
	}

}
