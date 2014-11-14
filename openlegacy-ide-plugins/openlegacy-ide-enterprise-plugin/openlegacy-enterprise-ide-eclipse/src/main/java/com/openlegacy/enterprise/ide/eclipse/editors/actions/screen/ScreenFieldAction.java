package com.openlegacy.enterprise.ide.eclipse.editors.actions.screen;

import com.openlegacy.enterprise.ide.eclipse.editors.actions.AbstractAction;
import com.openlegacy.enterprise.ide.eclipse.editors.actions.ActionType;
import com.openlegacy.enterprise.ide.eclipse.editors.models.screen.ScreenFieldModel;

import org.openlegacy.annotations.screen.ScreenField;

import java.util.UUID;

/**
 * @author Ivan Bort
 * 
 */
public class ScreenFieldAction extends AbstractAction {

	/**
	 * @param uuid
	 * @param namedObject
	 * @param actionType
	 * @param target
	 * @param key
	 * @param value
	 */
	public ScreenFieldAction(UUID uuid, ScreenFieldModel namedObject, ActionType actionType, int target, String key, Object value) {
		super(uuid, namedObject, actionType, target, key, value);
	}

	@Override
	public Class<?> getAnnotationClass() {
		return ScreenField.class;
	}

}
