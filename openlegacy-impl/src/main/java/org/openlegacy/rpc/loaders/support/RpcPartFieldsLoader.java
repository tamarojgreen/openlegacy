/*******************************************************************************
 * Copyright (c) 2012 OpenLegacy Inc.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     OpenLegacy Inc. - initial API and implementation
 *******************************************************************************/
package org.openlegacy.rpc.loaders.support;

import org.openlegacy.EntitiesRegistry;
import org.openlegacy.loaders.FieldLoader;
import org.openlegacy.rpc.definitions.RpcPartEntityDefinition;
import org.openlegacy.rpc.services.RpcEntitiesRegistry;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;

@Component
public class RpcPartFieldsLoader implements FieldLoader {

	@SuppressWarnings("rawtypes")
	public boolean match(EntitiesRegistry entitiesRegistry, Field field) {
		RpcEntitiesRegistry rpcEntitiesRegistry = (RpcEntitiesRegistry)entitiesRegistry;
		return (rpcEntitiesRegistry.getPart(field.getType()) != null);
	}

	@SuppressWarnings("rawtypes")
	public void load(EntitiesRegistry entitiesRegistry, Field field, Class<?> containingClass, int fieldOrder) {
		RpcEntitiesRegistry rpcEntitiesRegistry = (RpcEntitiesRegistry)entitiesRegistry;

		RpcPartEntityDefinition partDefinition = rpcEntitiesRegistry.getPart(field.getType());
		if (partDefinition != null) {
			rpcEntitiesRegistry.get(containingClass).getPartsDefinitions().put(field.getName(), partDefinition);
		}

	}
}
