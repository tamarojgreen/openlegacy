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
package org.openlegacy.rpc.loaders.support;

import org.openlegacy.EntitiesRegistry;
import org.openlegacy.exceptions.RegistryException;
import org.openlegacy.loaders.FieldLoader;
import org.openlegacy.rpc.definitions.RpcFieldDefinition;
import org.openlegacy.rpc.definitions.RpcPartEntityDefinition;
import org.openlegacy.rpc.definitions.SimpleRpcFieldDefinition;
import org.openlegacy.rpc.definitions.SimpleRpcPartEntityDefinition;
import org.openlegacy.rpc.services.RpcEntitiesRegistry;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.text.MessageFormat;
import java.util.List;
import java.util.Map;

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

	@Override
	@SuppressWarnings("rawtypes")
	public boolean match(EntitiesRegistry entitiesRegistry, Field field) {
		RpcEntitiesRegistry rpcEntitiesRegistry = (RpcEntitiesRegistry)entitiesRegistry;
		Class<?> fieldJavaType = getType(field);
		return (rpcEntitiesRegistry.getPart(fieldJavaType) != null);
	}

	@Override
	@SuppressWarnings("rawtypes")
	public void load(EntitiesRegistry entitiesRegistry, Field field, Class<?> containingClass, int fieldOrder) {
		RpcEntitiesRegistry rpcEntitiesRegistry = (RpcEntitiesRegistry)entitiesRegistry;
		String fieldName = field.getName();

		SimpleRpcPartEntityDefinition partDefinition = (SimpleRpcPartEntityDefinition)rpcEntitiesRegistry.getPart(getType(field));
		partDefinition.setOrder(fieldOrder);
		if (partDefinition != null) {

			if (rpcEntitiesRegistry.get(containingClass) != null) {
				Map<String, RpcFieldDefinition> rpcFieldDefinitions = rpcEntitiesRegistry.get(containingClass).getFieldsDefinitions();
				RpcFieldDefinition rpcFieldDefinition = rpcFieldDefinitions.get(fieldName);
				if (rpcFieldDefinition != null) {
					partDefinition.setCount(((SimpleRpcFieldDefinition)rpcFieldDefinition).getCount());
					partDefinition.setListElementName(((SimpleRpcFieldDefinition)rpcFieldDefinition).getListElementName());
					rpcFieldDefinitions.remove(fieldName);
				}
				rpcEntitiesRegistry.get(containingClass).getPartsDefinitions().put(field.getName(), partDefinition);
			} else { // it is inner class
				RpcPartEntityDefinition containgPartDefinion = rpcEntitiesRegistry.getPart(containingClass);
				fieldName = MessageFormat.format("{0}.{1}", containgPartDefinion.getPartName(), fieldName);

				RpcFieldDefinition rpcFieldDefinition = containgPartDefinion.getFieldsDefinitions().get(fieldName);
				if (rpcFieldDefinition != null) {
					partDefinition.setCount(((SimpleRpcFieldDefinition)rpcFieldDefinition).getCount());
					partDefinition.setListElementName(((SimpleRpcFieldDefinition)rpcFieldDefinition).getListElementName());
					containgPartDefinion.getFieldsDefinitions().remove(fieldName);
				}
				containgPartDefinion.getInnerPartsDefinitions().put(field.getName(), partDefinition);
			}
		}
	}
}
