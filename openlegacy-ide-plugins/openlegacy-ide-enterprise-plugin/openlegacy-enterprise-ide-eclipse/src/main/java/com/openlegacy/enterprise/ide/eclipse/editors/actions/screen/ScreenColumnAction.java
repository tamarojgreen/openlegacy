package com.openlegacy.enterprise.ide.eclipse.editors.actions.screen;

import com.openlegacy.enterprise.ide.eclipse.editors.actions.AbstractAction;
import com.openlegacy.enterprise.ide.eclipse.editors.actions.ActionType;
import com.openlegacy.enterprise.ide.eclipse.editors.models.screen.ScreenColumnModel;

import org.openlegacy.annotations.screen.ScreenColumn;

import java.util.UUID;

/**
 * @author Ivan Bort
 * 
 */
public class ScreenColumnAction extends AbstractAction {

	/**
	 * @param uuid
	 * @param namedObject
	 * @param actionType
	 * @param kind
	 * @param key
	 * @param value
	 */
	public ScreenColumnAction(UUID uuid, ScreenColumnModel namedObject, ActionType actionType, int kind, String key, Object value) {
		super(uuid, namedObject, actionType, kind, key, value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.openlegacy.enterprise.ide.eclipse.actions.AbstractAction#getAnnotationClass()
	 */
	@Override
	public Class<?> getAnnotationClass() {
		return ScreenColumn.class;
	}

}
