package com.openlegacy.enterprise.ide.eclipse.editors.actions.screen;

import java.util.UUID;

import org.openlegacy.annotations.screen.ScreenDynamicField;

import com.openlegacy.enterprise.ide.eclipse.editors.actions.ActionType;
import com.openlegacy.enterprise.ide.eclipse.editors.models.screen.ScreenFieldModel;

public class ScreenDynamicFieldAction extends ScreenFieldAction {

	public ScreenDynamicFieldAction(UUID uuid, ScreenFieldModel namedObject, ActionType actionType, int target, String key,
			Object value) {
		super(uuid, namedObject, actionType, target, key, value);
	}

	@Override
	public Class<?> getAnnotationClass() {
		return ScreenDynamicField.class;
	}

}
