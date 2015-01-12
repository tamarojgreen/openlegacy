package com.openlegacy.enterprise.ide.eclipse.editors.actions.screen;

import com.openlegacy.enterprise.ide.eclipse.editors.actions.ActionType;
import com.openlegacy.enterprise.ide.eclipse.editors.models.screen.ScreenFieldValuesModel;

import org.openlegacy.annotations.screen.ScreenFieldValues;

import java.util.UUID;

/**
 * @author Ivan Bort
 * 
 */
public class ScreenFieldValuesAction extends ScreenFieldAction {

	/**
	 * @param uuid
	 * @param namedObject
	 * @param actionType
	 * @param kind
	 * @param key
	 * @param value
	 */
	public ScreenFieldValuesAction(UUID uuid, ScreenFieldValuesModel namedObject, ActionType actionType, int kind, String key,
			Object value) {
		super(uuid, namedObject, actionType, kind, key, value);
	}

	@Override
	public Class<?> getAnnotationClass() {
		return ScreenFieldValues.class;
	}

}
