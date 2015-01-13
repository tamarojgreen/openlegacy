package com.openlegacy.enterprise.ide.eclipse.editors.actions.screen;

import com.openlegacy.enterprise.ide.eclipse.editors.actions.ActionType;
import com.openlegacy.enterprise.ide.eclipse.editors.models.screen.ScreenFieldModel;

import org.openlegacy.annotations.screen.ScreenDescriptionField;

import java.util.UUID;

public class ScreenDescriptionFieldAction extends ScreenFieldAction {

	public ScreenDescriptionFieldAction(UUID uuid, ScreenFieldModel namedObject, ActionType actionType, int target, String key,
			Object value) {
		super(uuid, namedObject, actionType, target, key, value);
	}

	@Override
	public Class<?> getAnnotationClass() {
		return ScreenDescriptionField.class;
	}

}
