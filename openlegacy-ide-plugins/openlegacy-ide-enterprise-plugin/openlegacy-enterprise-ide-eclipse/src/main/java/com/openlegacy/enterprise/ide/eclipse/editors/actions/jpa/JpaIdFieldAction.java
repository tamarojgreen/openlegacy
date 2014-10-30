package com.openlegacy.enterprise.ide.eclipse.editors.actions.jpa;

import com.openlegacy.enterprise.ide.eclipse.editors.actions.ActionType;
import com.openlegacy.enterprise.ide.eclipse.editors.models.NamedObject;

import java.util.UUID;

import javax.persistence.Id;

/**
 * @author Ivan Bort
 * 
 */
public class JpaIdFieldAction extends JpaFieldAction {

	public JpaIdFieldAction(UUID uuid, NamedObject namedObject, ActionType actionType, int kind, String key, Object value) {
		super(uuid, namedObject, actionType, kind, key, value);
	}

	@Override
	public Class<?> getAnnotationClass() {
		return Id.class;
	}

}
