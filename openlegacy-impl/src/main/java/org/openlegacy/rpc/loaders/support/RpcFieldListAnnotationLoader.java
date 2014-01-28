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
import org.openlegacy.annotations.rpc.RpcListField;
import org.openlegacy.loaders.support.AbstractFieldAnnotationLoader;
import org.openlegacy.rpc.definitions.RpcEntityDefinition;
import org.openlegacy.rpc.definitions.RpcPartEntityDefinition;
import org.openlegacy.rpc.definitions.SimpleRpcFieldDefinition;
import org.openlegacy.rpc.services.RpcEntitiesRegistry;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.text.MessageFormat;

public class RpcFieldListAnnotationLoader extends AbstractFieldAnnotationLoader {

	public boolean match(Annotation annotation) {
		return annotation.annotationType() == RpcListField.class;
	}

	@SuppressWarnings("rawtypes")
	public void load(EntitiesRegistry entitiesRegistry, Field field, Annotation annotation, Class<?> containingClass,
			int fieldOrder) {

		RpcEntitiesRegistry rpcEntitiesRegistry = (RpcEntitiesRegistry)entitiesRegistry;

		RpcListField fieldAnnotation = (RpcListField)annotation;

		RpcEntityDefinition rpcEntityDefinition = rpcEntitiesRegistry.get(containingClass);
		String fieldName = field.getName();

		if (rpcEntityDefinition != null) {
			SimpleRpcFieldDefinition fieldDefinition = (SimpleRpcFieldDefinition)rpcEntityDefinition.getFieldsDefinitions().get(
					fieldName);
			fieldDefinition.setCount(fieldAnnotation.count());

		} else {
			// look in rpc entities parts
			RpcPartEntityDefinition rpcPart = rpcEntitiesRegistry.getPart(containingClass);
			if (rpcPart != null) {
				fieldName = MessageFormat.format("{0}.{1}", rpcPart.getPartName(), fieldName);
				SimpleRpcFieldDefinition fieldDefinition = (SimpleRpcFieldDefinition)rpcPart.getFieldsDefinitions().get(fieldName);
				fieldDefinition.setCount(fieldAnnotation.count());
			}

		}
	}

}
