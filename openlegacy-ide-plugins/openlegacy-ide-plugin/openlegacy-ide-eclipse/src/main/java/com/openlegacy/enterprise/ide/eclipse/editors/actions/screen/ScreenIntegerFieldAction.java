package com.openlegacy.enterprise.ide.eclipse.editors.actions.screen;

import com.openlegacy.enterprise.ide.eclipse.editors.actions.ActionType;
import com.openlegacy.enterprise.ide.eclipse.editors.models.screen.ScreenIntegerFieldModel;

import java.util.UUID;

/**
 * @author Ivan Bort
 * 
 */
public class ScreenIntegerFieldAction extends ScreenFieldAction {

	/**
	 * @param uuid
	 * @param namedObject
	 * @param actionType
	 * @param target
	 * @param key
	 * @param value
	 */
	public ScreenIntegerFieldAction(UUID uuid, ScreenIntegerFieldModel namedObject, ActionType actionType, int target,
			String key, Object value) {
		super(uuid, namedObject, actionType, target, key, value);
	}

}
