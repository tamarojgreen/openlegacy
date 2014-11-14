package com.openlegacy.enterprise.ide.eclipse.editors.actions.jpa;

import com.openlegacy.enterprise.ide.eclipse.editors.actions.ActionType;
import com.openlegacy.enterprise.ide.eclipse.editors.models.jpa.JpaBooleanFieldModel;

import java.util.UUID;

/**
 * @author Ivan Bort
 * 
 */
public class JpaBooleanFieldAction extends JpaFieldAction {

	public JpaBooleanFieldAction(UUID uuid, JpaBooleanFieldModel namedObject, ActionType actionType, int kind, String key,
			Object value) {
		super(uuid, namedObject, actionType, kind, key, value);
	}

}
