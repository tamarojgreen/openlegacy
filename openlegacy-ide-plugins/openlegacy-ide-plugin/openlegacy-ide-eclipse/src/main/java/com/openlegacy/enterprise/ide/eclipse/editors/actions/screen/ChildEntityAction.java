package com.openlegacy.enterprise.ide.eclipse.editors.actions.screen;

import com.openlegacy.enterprise.ide.eclipse.editors.actions.AbstractAction;
import com.openlegacy.enterprise.ide.eclipse.editors.actions.ActionType;
import com.openlegacy.enterprise.ide.eclipse.editors.models.NamedObject;

import java.util.UUID;

/**
 * @author Ivan Bort
 * 
 */
public class ChildEntityAction extends AbstractAction {

	/**
	 * @param uuid
	 * @param namedObject
	 * @param actionType
	 * @param kind
	 * @param key
	 * @param value
	 */
	public ChildEntityAction(UUID uuid, NamedObject namedObject, ActionType actionType, int kind, String key, Object value) {
		super(uuid, namedObject, actionType, kind, key, value);
	}

	@Override
	public Class<?> getAnnotationClass() {
		return Object.class;
	}

}
