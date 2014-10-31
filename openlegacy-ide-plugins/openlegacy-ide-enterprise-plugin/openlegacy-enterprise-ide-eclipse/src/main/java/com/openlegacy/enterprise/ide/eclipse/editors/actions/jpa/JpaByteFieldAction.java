package com.openlegacy.enterprise.ide.eclipse.editors.actions.jpa;

import com.openlegacy.enterprise.ide.eclipse.editors.actions.ActionType;
import com.openlegacy.enterprise.ide.eclipse.editors.models.jpa.JpaByteFieldModel;

import java.util.UUID;

/**
 * @author Ivan Bort
 * 
 */
public class JpaByteFieldAction extends JpaFieldAction {

	public JpaByteFieldAction(UUID uuid, JpaByteFieldModel namedObject, ActionType actionType, int kind, String key, Object value) {
		super(uuid, namedObject, actionType, kind, key, value);
	}

}
