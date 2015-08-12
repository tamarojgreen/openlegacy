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

package org.openlegacy.utils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.PropertyValue;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.TypedStringValue;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

public class BeanUtils {

	private final static Log logger = LogFactory.getLog(BeanUtils.class);

	// Object must contain such named and typed fields as bean props
	public static void fillFromBeanDef(BeanDefinition beanDef, Class<?> beanClass, Object object) {
		List<Field> fields = ClassUtils.getDeclaredFields(beanClass);
		for (Field field : fields) {
			Method method = ClassUtils.getWriteMethod(field.getName(), object.getClass(), field.getType());
			if (method == null || !ClassUtils.isPublicMethod(method)) {
				continue;
			}

			PropertyValue prop = beanDef.getPropertyValues().getPropertyValue(field.getName());
			if (prop == null) {
				continue;
			}

			Object value = prop.getValue();
			if (value instanceof TypedStringValue) {
				value = ((TypedStringValue)value).getValue();
			}
			try {
				if (FieldUtil.isPrimitive(field.getType())) {
					if (!(value instanceof String)) {
						value = FieldUtil.getPrimitiveClass(field.getType()).getMethod(FieldUtil.VALUE_OF, String.class).invoke(
								null, value);
					}
				} else {
					continue;
				}
			} catch (Exception e) {
				value = null;
			}

			if (value != null) {
				try {
					method.invoke(object, value);
				} catch (Exception e) {
					if (logger.isDebugEnabled()) {
						logger.error(String.format("Smt wrong with %s property", field.getName()));
					}
				}
			} else {
				if (logger.isDebugEnabled()) {
					logger.error(String.format("Null %s property", field.getName()));
				}
			}
		}

	}

}
