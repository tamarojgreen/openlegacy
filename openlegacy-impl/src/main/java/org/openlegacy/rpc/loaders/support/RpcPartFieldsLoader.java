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
import org.openlegacy.exceptions.RegistryException;
import org.openlegacy.loaders.FieldLoader;
import org.openlegacy.rpc.definitions.RpcEntityDefinition;
import org.openlegacy.rpc.definitions.RpcPartEntityDefinition;
import org.openlegacy.rpc.services.RpcEntitiesRegistry;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.List;

@Component
public class RpcPartFieldsLoader implements FieldLoader {

	@SuppressWarnings("static-method")
	private Class<?> getType(Field field) {
		Class<?> fieldType = field.getType();
		if (fieldType == List.class) {
			try {
				ParameterizedType itemType = (ParameterizedType)field.getGenericType();
				fieldType = (Class<?>)itemType.getActualTypeArguments()[0];

			} catch (SecurityException e) {
				throw (new RegistryException("SecurityException"));
			}
		}
		return fieldType;
	}

	@SuppressWarnings("rawtypes")
	public boolean match(EntitiesRegistry entitiesRegistry, Field field) {
		RpcEntitiesRegistry rpcEntitiesRegistry = (RpcEntitiesRegistry)entitiesRegistry;
		Class<?> fieldJavaType = getType(field);
		return (rpcEntitiesRegistry.getPart(fieldJavaType) != null);
	}

	@SuppressWarnings("rawtypes")
	public void load(EntitiesRegistry entitiesRegistry, Field field, Class<?> containingClass, int fieldOrder) {
		RpcEntitiesRegistry rpcEntitiesRegistry = (RpcEntitiesRegistry)entitiesRegistry;

		RpcPartEntityDefinition partDefinition = rpcEntitiesRegistry.getPart(getType(field));
		if (partDefinition != null) {
			RpcEntityDefinition rpcEntityDefinition = rpcEntitiesRegistry.get(containingClass);
			if (rpcEntityDefinition == null) { // nested class

				RpcPartEntityDefinition containingPartDefinition = rpcEntitiesRegistry.getPart(containingClass);
				containingPartDefinition.getInnerPartsDefinitions().put(field.getName(), partDefinition);

			} else {

				rpcEntitiesRegistry.get(containingClass).getPartsDefinitions().put(field.getName(), partDefinition);
			}
		}

	}
}
