package com.openlegacy.enterprise.ide.eclipse.editors.actions.screen;

import com.openlegacy.enterprise.ide.eclipse.editors.actions.ActionType;
import com.openlegacy.enterprise.ide.eclipse.editors.models.screen.ScreenBooleanFieldModel;

import org.openlegacy.annotations.screen.ScreenBooleanField;

import java.util.UUID;

/**
 * @author Ivan Bort
 * 
 */
public class ScreenBooleanFieldAction extends ScreenFieldAction {

	/**
	 * @param uuid
	 * @param namedObject
	 * @param actionType
	 * @param kind
	 * @param key
	 * @param value
	 */
	public ScreenBooleanFieldAction(UUID uuid, ScreenBooleanFieldModel namedObject, ActionType actionType, int kind, String key,
			Object value) {
		super(uuid, namedObject, actionType, kind, key, value);
	}

	@Override
	public Class<?> getAnnotationClass() {
		return ScreenBooleanField.class;
	}

}
