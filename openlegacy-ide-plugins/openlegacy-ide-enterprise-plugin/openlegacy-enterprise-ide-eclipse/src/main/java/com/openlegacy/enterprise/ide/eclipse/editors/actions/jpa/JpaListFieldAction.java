package com.openlegacy.enterprise.ide.eclipse.editors.actions.jpa;

import com.openlegacy.enterprise.ide.eclipse.editors.actions.ActionType;
import com.openlegacy.enterprise.ide.eclipse.editors.models.jpa.JpaListFieldModel;

import java.util.UUID;

import javax.persistence.OneToMany;

/**
 * @author Ivan Bort
 * 
 */
public class JpaListFieldAction extends JpaFieldAction {

	public JpaListFieldAction(UUID uuid, JpaListFieldModel namedObject, ActionType actionType, int kind, String key, Object value) {
		super(uuid, namedObject, actionType, kind, key, value);
	}

	@Override
	public Class<?> getAnnotationClass() {
		return OneToMany.class;
	}

}
