/*******************************************************************************
 * Copyright (c) 2015 OpenLegacy Inc.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     OpenLegacy Inc. - initial API and implementation
 *******************************************************************************/

package org.openlegacy.rpc.support.binders;

import org.openlegacy.definitions.ActionDefinition;
import org.openlegacy.definitions.RpcActionDefinition;
import org.openlegacy.rpc.RpcEntity;
import org.openlegacy.rpc.RpcEntityBinder;
import org.openlegacy.rpc.RpcFlatField;
import org.openlegacy.rpc.RpcInvokeAction;
import org.openlegacy.rpc.RpcResult;
import org.openlegacy.rpc.definitions.RpcEntityDefinition;
import org.openlegacy.rpc.definitions.RpcFieldDefinition;
import org.openlegacy.rpc.services.RpcEntitiesRegistry;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * @author Ivan Bort
 */
public class RpcEntityActionsBinder implements RpcEntityBinder {

	@Inject
	private RpcEntitiesRegistry entitiesRegistry;

	@Override
	public void populateEntity(Object entity, RpcResult result) {
		RpcEntityDefinition entityDefinition = entitiesRegistry.get(entity.getClass());
		List<ActionDefinition> actions = entityDefinition.getActions();

		List<RpcActionDefinition> entityActions = new ArrayList<RpcActionDefinition>();
		for (ActionDefinition actionDefinition : actions) {
			RpcActionDefinition rpcActionDefinition = (RpcActionDefinition) actionDefinition;
			entityActions.add(rpcActionDefinition);
		}

		((RpcEntity) entity).getActions().addAll(entityActions);
	}

	@Override
	public void populateAction(RpcInvokeAction remoteAction, Object entity) {}

	@Override
	public boolean fieldMatch(RpcFieldDefinition rpcFieldDefinition) {
		return false;
	}

	@Override
	public Object toApi(RpcFieldDefinition rpcFieldDefinition, Object fiieldValue) {
		return null;
	}

	@Override
	public Object toLegacy(RpcFieldDefinition rpcFieldDefinition, Object apiFieldValue, RpcFlatField rpcFlatField) {
		return null;
	}

}
