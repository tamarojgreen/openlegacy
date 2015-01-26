package com.openlegacy.enterprise.ide.eclipse.editors.actions.jpa;

import com.openlegacy.enterprise.ide.eclipse.editors.actions.ActionType;
import com.openlegacy.enterprise.ide.eclipse.editors.models.NamedObject;

import java.util.UUID;

/**
 * @author Aleksey Yeremeyev
 * 
 */
public class JpaGeneratedValueFieldAction extends JpaFieldAction {

	public JpaGeneratedValueFieldAction(UUID uuid, NamedObject namedObject, ActionType actionType, int kind, String key,
			Object value) {
		super(uuid, namedObject, actionType, kind, key, value);
	}

}
