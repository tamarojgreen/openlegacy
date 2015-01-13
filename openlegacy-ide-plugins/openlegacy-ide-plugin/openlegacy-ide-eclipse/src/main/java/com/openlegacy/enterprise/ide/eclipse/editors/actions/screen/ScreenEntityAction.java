package com.openlegacy.enterprise.ide.eclipse.editors.actions.screen;

import com.openlegacy.enterprise.ide.eclipse.editors.actions.AbstractAction;
import com.openlegacy.enterprise.ide.eclipse.editors.actions.ActionType;
import com.openlegacy.enterprise.ide.eclipse.editors.models.screen.ScreenEntityModel;

import org.openlegacy.annotations.screen.ScreenEntity;

import java.util.UUID;

/**
 * @author Ivan Bort
 * 
 */
public class ScreenEntityAction extends AbstractAction {

	/**
	 * @param uuid
	 * @param namedObject
	 * @param actionType
	 *            - ActionType.MODIFY or ActionType.REMOVE only
	 * @param target
	 * @param key
	 * @param value
	 */
	public ScreenEntityAction(UUID uuid, ScreenEntityModel namedObject, ActionType actionType, int target, String key,
			Object value) {
		super(uuid, namedObject, actionType, target, key, value);
	}

	@Override
	public Class<?> getAnnotationClass() {
		return ScreenEntity.class;
	}

}
