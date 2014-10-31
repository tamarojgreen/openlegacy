package com.openlegacy.enterprise.ide.eclipse.editors.actions.jpa;

import com.openlegacy.enterprise.ide.eclipse.editors.actions.ActionType;
import com.openlegacy.enterprise.ide.eclipse.editors.models.jpa.JpaIntegerFieldModel;

import java.util.UUID;

/**
 * @author Ivan Bort
 * 
 */
public class JpaIntegerFieldAction extends JpaFieldAction {

	public JpaIntegerFieldAction(UUID uuid, JpaIntegerFieldModel namedObject, ActionType actionType, int kind, String key,
			Object value) {
		super(uuid, namedObject, actionType, kind, key, value);
	}

}
