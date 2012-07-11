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
package org.openlegacy.utils;

import org.apache.commons.beanutils.BeanUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;

public class ReflectionUtil {

	public static Class<?> getListType(Field field) {
		try {
			if (!(field.getGenericType() instanceof ParameterizedType)) {
				return null;
			}

			ParameterizedType type = (ParameterizedType)field.getGenericType();
			if (type.getActualTypeArguments().length == 0) {
				return null;
			}
			if (!(type.getActualTypeArguments()[0] instanceof Class)) {
				return null;
			}
			Class<?> listType = (Class<?>)type.getActualTypeArguments()[0];
			return listType;
		} catch (Exception e) {
			throw (new IllegalArgumentException(e));
		}

	}

	public static <T> T newInstance(Class<? extends T> clazz) {
		return org.springframework.beans.BeanUtils.instantiate(clazz);
	}

	public static void copyProperties(Object destination, Object source) {
		try {
			BeanUtils.copyProperties(destination, source);
		} catch (IllegalAccessException e) {
			throw (new IllegalArgumentException(e));
		} catch (InvocationTargetException e) {
			throw (new IllegalArgumentException(e));
		}
	}

	public static Object getStaticFieldValue(Class<?> class1, String value) {
		try {
			Field field = class1.getField(value);
			return field.get(class1.newInstance());
		} catch (Exception e) {
			// do nothing
		}
		return null;
	}
}
