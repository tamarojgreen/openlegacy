package com.openlegacy.enterprise.ide.eclipse.editors.actions.screen;

import com.openlegacy.enterprise.ide.eclipse.editors.actions.ActionType;
import com.openlegacy.enterprise.ide.eclipse.editors.models.screen.ScreenIntegerFieldModel;

import org.openlegacy.annotations.screen.ScreenNumericField;

import java.util.UUID;

/**
 * @author Ivan Bort
 * 
 */
public class ScreenNumericFieldAction extends ScreenFieldAction {

	/**
	 * @param uuid
	 * @param namedObject
	 * @param actionType
	 * @param target
	 * @param key
	 * @param value
	 */
	public ScreenNumericFieldAction(UUID uuid, ScreenIntegerFieldModel namedObject, ActionType actionType, int target,
			String key, Object value) {
		super(uuid, namedObject, actionType, target, key, value);
	}

	@Override
	public Class<?> getAnnotationClass() {
		return ScreenNumericField.class;
	}

}
