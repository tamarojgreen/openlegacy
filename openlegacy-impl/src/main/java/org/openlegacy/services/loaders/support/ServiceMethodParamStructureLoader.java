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

package org.openlegacy.services.loaders.support;

import org.openlegacy.loaders.support.AbstractServiceMethodParamLoader;
import org.openlegacy.services.definitions.ServiceMethodDefinition;
import org.openlegacy.services.definitions.SimpleServiceMethodDefinition;
import org.openlegacy.services.definitions.SimpleServiceParamDetailsDefinition;
import org.openlegacy.utils.ClassUtils;
import org.openlegacy.utils.FieldUtil;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.List;

@Component
@Order(1)
public class ServiceMethodParamStructureLoader extends AbstractServiceMethodParamLoader {

	private static final String ARG = "arg";
	private static final String RESULT = "result";
	private static final String ITEM = "listItem";

	@Override
	public void load(ServiceMethodDefinition definition, Method method) {
		if (!(definition instanceof SimpleServiceMethodDefinition)) {
			return;
		}

		int i = 0;
		SimpleServiceParamDetailsDefinition paramDef;
		for (Class<?> type : method.getParameterTypes()) {
			String name = String.format("%s%d", ARG, i++);
			paramDef = buildParamDefinition(type);
			paramDef.setFieldName(name);
			definition.getInputParams().add(paramDef);
		}

		Class<?> type = method.getReturnType();
		paramDef = buildParamDefinition(type);
		paramDef.setFieldName(RESULT);
		definition.getOutputParams().add(paramDef);
	}

	private SimpleServiceParamDetailsDefinition buildParamDefinition(Class<?> paramClass) {
		SimpleServiceParamDetailsDefinition def = new SimpleServiceParamDetailsDefinition();
		def.setFieldClass(paramClass);

		if (!FieldUtil.isPrimitive(paramClass)) {
			for (Field field : paramClass.getDeclaredFields()) {

				if (!hasGetterAndSetter(field, paramClass)) {
					continue;
				}

				SimpleServiceParamDetailsDefinition childDef = buildParamDefinition(field.getType());
				childDef.setFieldName(field.getName());
				def.getFields().add(childDef);

				if (childDef.getFieldClass() == List.class) {
					ParameterizedType type = (ParameterizedType)field.getGenericType();
					SimpleServiceParamDetailsDefinition itemDef = buildParamDefinition((Class<?>)type.getActualTypeArguments()[0]);
					itemDef.setFieldName(ITEM);
					childDef.getFields().add(itemDef);
				}
			}
		}
		return def;
	}

	private boolean hasGetterAndSetter(Field field, Class<?> clazz) {
		Method getter = ClassUtils.getReadMethod(field.getName(), clazz);
		Method setter = ClassUtils.getWriteMethod(field.getName(), clazz, field.getType());
		return (getter != null && ClassUtils.isPublicMethod(getter)) && (setter != null && ClassUtils.isPublicMethod(setter));
	}
}
