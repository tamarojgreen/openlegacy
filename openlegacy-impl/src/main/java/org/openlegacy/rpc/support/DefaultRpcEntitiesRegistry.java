/*******************************************************************************
 * Copyright (c) 2014 OpenLegacy Inc.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     OpenLegacy Inc. - initial API and implementation
 *******************************************************************************/
package org.openlegacy.rpc.support;

import org.openlegacy.rpc.RpcSnapshot;
import org.openlegacy.rpc.definitions.RpcEntityDefinition;
import org.openlegacy.rpc.definitions.RpcFieldDefinition;
import org.openlegacy.rpc.definitions.RpcPartEntityDefinition;
import org.openlegacy.rpc.services.RpcEntitiesRegistry;
import org.openlegacy.support.AbstractEntitiesRegistry;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class DefaultRpcEntitiesRegistry extends AbstractEntitiesRegistry<RpcEntityDefinition, RpcFieldDefinition, RpcPartEntityDefinition> implements RpcEntitiesRegistry {

	private static final long serialVersionUID = 1L;
	private Map<String, RpcEntityDefinition> rpcDefinitionsByProgram;

	public RpcEntityDefinition match(RpcSnapshot rpcSnapshot) {

		if (rpcDefinitionsByProgram == null) {
			rpcDefinitionsByProgram = new HashMap<String, RpcEntityDefinition>();
			Collection<RpcEntityDefinition> rpcDefinitionsValues = getEntitiesDefinitions();
			for (RpcEntityDefinition rpcDefinition : rpcDefinitionsValues) {
				rpcDefinitionsByProgram.put(rpcDefinition.getIdentification(), rpcDefinition);

			}

			if (rpcDefinitionsByProgram.containsKey(null)) {
				rpcDefinitionsByProgram.remove(null);
			}

		}
		String programPath = rpcSnapshot.getRpcInvokeAction().getRpcPath();
		if (rpcDefinitionsByProgram.containsKey(programPath)) {
			return rpcDefinitionsByProgram.get(programPath);
		}
		String entityName = rpcSnapshot.getEntityName();
		if (entityName != null) {
			return get(entityName);

		}
		return null;
	}

}
