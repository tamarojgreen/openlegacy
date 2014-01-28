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
package org.openlegacy.utils;

import org.apache.commons.beanutils.BeanUtils;
import org.openlegacy.exceptions.OpenLegacyRuntimeException;
import org.springframework.util.Assert;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;

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

	public static Object newInstance(String className) throws ClassNotFoundException {
		return newInstance(Class.forName(className));
	}

	public static <T> List<T> newListInstance(Class<? extends T> clazz, int count) {
		List<T> result = new ArrayList<T>();

		for (int i = 0; i < count; i++) {
			result.add(org.springframework.beans.BeanUtils.instantiate(clazz));
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	public static <T> T[] newArrayInstance(Class<? extends T> clazz, int count) {
		return (T[])newListInstance(clazz, count).toArray();
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

	public static Object invoke(Object object, String methodName, Object... args) {
		try {
			Method[] methods = object.getClass().getMethods();
			Method method = null;
			for (Method method1 : methods) {
				if (method1.getName().equals(methodName)) {
					method = method1;
				}
			}
			Assert.notNull("Method not found:" + methodName, methodName);
			return method.invoke(object, args);
		} catch (Exception e) {
			throw (new OpenLegacyRuntimeException(e));
		}
	}
}
