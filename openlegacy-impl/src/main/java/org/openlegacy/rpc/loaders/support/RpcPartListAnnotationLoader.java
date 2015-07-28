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
import org.openlegacy.annotations.rpc.RpcPartList;
import org.openlegacy.loaders.support.AbstractFieldAnnotationLoader;
import org.openlegacy.rpc.RpcFieldTypes;
import org.openlegacy.rpc.definitions.RpcEntityDefinition;
import org.openlegacy.rpc.definitions.RpcPartEntityDefinition;
import org.openlegacy.rpc.definitions.SimpleRpcFieldDefinition;
import org.openlegacy.rpc.services.RpcEntitiesRegistry;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.text.MessageFormat;

@Component
public class RpcPartListAnnotationLoader extends AbstractFieldAnnotationLoader {

	@Override
	public boolean match(Annotation annotation) {
		return annotation.annotationType() == RpcPartList.class;
	}

	@Override
	@SuppressWarnings("rawtypes")
	public void load(EntitiesRegistry entitiesRegistry, Field field, Annotation annotation, Class<?> containingClass,
			int fieldOrder) {

		RpcEntitiesRegistry rpcEntitiesRegistry = (RpcEntitiesRegistry)entitiesRegistry;

		RpcPartList fieldAnnotation = (RpcPartList)annotation;

		RpcEntityDefinition rpcEntityDefinition = rpcEntitiesRegistry.get(containingClass);
		String fieldName = field.getName();

		// look in rpc entities
		if (rpcEntityDefinition != null) {
			SimpleRpcFieldDefinition fieldDefinition = new SimpleRpcFieldDefinition(fieldName, RpcFieldTypes.General.class);
			fieldDefinition.setCount(fieldAnnotation.count());
			fieldDefinition.setListElementName(fieldAnnotation.listElementName());
			rpcEntityDefinition.getFieldsDefinitions().put(fieldName, fieldDefinition);
		} else {
			// look in rpc entities parts
			RpcPartEntityDefinition rpcPart = rpcEntitiesRegistry.getPart(containingClass);
			if (rpcPart != null) {
				fieldName = MessageFormat.format("{0}.{1}", rpcPart.getPartName(), fieldName);
				SimpleRpcFieldDefinition fieldDefinition = new SimpleRpcFieldDefinition(fieldName, RpcFieldTypes.General.class);
				fieldDefinition.setCount(fieldAnnotation.count());
				fieldDefinition.setListElementName(fieldAnnotation.listElementName());
				rpcPart.getFieldsDefinitions().put(fieldName, fieldDefinition);

			}

		}
	}

}
