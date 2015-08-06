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

package org.openlegacy.ws.loaders.support;

import org.openlegacy.loaders.support.AbstractWsMethodParamLoader;
import org.openlegacy.utils.ClassUtil;
import org.openlegacy.utils.FieldUtil;
import org.openlegacy.ws.definitions.SimpleWebServiceMethodDefinition;
import org.openlegacy.ws.definitions.SimpleWebServiceParamDefinition;
import org.openlegacy.ws.definitions.SimpleWebServiceParamDetailsDefinition;
import org.openlegacy.ws.definitions.WebServiceMethodDefinition;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

@Component
@Order(1)
public class ServiceMethodParamStructureLoader extends AbstractWsMethodParamLoader {

	private static final String ARG = "arg";
	private static final String RESULT = "result";

	@Override
	public void load(WebServiceMethodDefinition definition, Method method) {
		if (!(definition instanceof SimpleWebServiceMethodDefinition)) {
			return;
		}

		int i = 0;
		for (Class<?> type : method.getParameterTypes()) {
			String name = String.format("%s%d", ARG, i++);
			SimpleWebServiceParamDefinition simpleDef = (SimpleWebServiceParamDefinition)definition.getInputParam();
			SimpleWebServiceParamDetailsDefinition paramDef = buildParamDefinition(type, true);
			paramDef.setFieldName(name);
			simpleDef.setParamDetails(paramDef);
		}

		Class<?> type = method.getReturnType();
		SimpleWebServiceParamDefinition simpleDef = (SimpleWebServiceParamDefinition)definition.getInputParam();
		SimpleWebServiceParamDetailsDefinition paramDef = buildParamDefinition(type, false);
		paramDef.setFieldName(RESULT);
		simpleDef.setParamDetails(paramDef);
	}

	private SimpleWebServiceParamDetailsDefinition buildParamDefinition(Class<?> paramClass, boolean isInputSection) {
		SimpleWebServiceParamDetailsDefinition def = new SimpleWebServiceParamDetailsDefinition();
		def.setFieldClass(paramClass);

		if (!FieldUtil.isPrimitive(paramClass)) {
			for (Field field : paramClass.getDeclaredFields()) {
				if (!hasGetterOrSetter(field, paramClass)) {
					continue;
				}

				SimpleWebServiceParamDetailsDefinition childDef = buildParamDefinition(field.getType(), isInputSection);
				childDef.setFieldName(field.getName());
				def.getFields().add(childDef);
			}
		}
		return def;
	}

	private boolean hasGetterOrSetter(Field field, Class<?> clazz) {
		Method getter = ClassUtil.getReadMethod(field.getName(), clazz);
		Method setter = ClassUtil.getWriteMethod(field.getName(), clazz, field.getType());
		return (getter != null && hasPublicModifier(getter)) || (setter != null && hasPublicModifier(setter));
	}

	private boolean hasPublicModifier(Method m) {
		return (m.getModifiers() & Modifier.PUBLIC) == Modifier.PUBLIC;
	}
}
