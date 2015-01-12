package com.openlegacy.enterprise.ide.eclipse.editors.actions.jpa;

import com.openlegacy.enterprise.ide.eclipse.editors.actions.ActionType;
import com.openlegacy.enterprise.ide.eclipse.editors.models.jpa.JpaDateFieldModel;

import java.util.UUID;

/**
 * @author Ivan Bort
 * 
 */
public class JpaDateFieldAction extends JpaFieldAction {

	public JpaDateFieldAction(UUID uuid, JpaDateFieldModel namedObject, ActionType actionType, int kind, String key, Object value) {
		super(uuid, namedObject, actionType, kind, key, value);
	}

}
