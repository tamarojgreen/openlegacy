package com.openlegacy.enterprise.ide.eclipse.editors.actions.rpc;

import com.openlegacy.enterprise.ide.eclipse.editors.actions.AbstractAction;
import com.openlegacy.enterprise.ide.eclipse.editors.actions.ActionType;
import com.openlegacy.enterprise.ide.eclipse.editors.models.NamedObject;

import org.openlegacy.annotations.rpc.RpcPart;

import java.util.UUID;

/**
 * @author Ivan Bort
 * 
 */
public class RpcPartAction extends AbstractAction {

	public RpcPartAction(UUID uuid, NamedObject namedObject, ActionType actionType, int kind, String key, Object value) {
		super(uuid, namedObject, actionType, kind, key, value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.openlegacy.enterprise.ide.eclipse.editors.actions.AbstractAction#getAnnotationClass()
	 */
	@Override
	public Class<?> getAnnotationClass() {
		return RpcPart.class;
	}

}
