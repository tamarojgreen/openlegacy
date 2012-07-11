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

import org.springframework.beans.DirectFieldAccessor;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

public class PropertyUtil {

	public static final String GET = "get";
	public static final String SET = "set";
	private static Map<Class<?>, DirectFieldAccessor> fieldAccessorsCache = new HashMap<Class<?>, DirectFieldAccessor>();

	/**
	 * Return a bean property name from a getter method, if the method is in the format of getXXX
	 * 
	 * @param methodName
	 * @return bean property name of a getter method. null otherwise
	 */
	public static String getPropertyNameIfGetter(String methodName) {
		if (methodName.startsWith(GET)) {
			String noGetMethodName = methodName.substring(GET.length());
			return StringUtils.uncapitalize(noGetMethodName);

		}
		return null;
	}

	/**
	 * Return a bean property name from a setter method, if the method is in the format of setXXX
	 * 
	 * @param methodName
	 * @return bean property name of a setter method. null otherwise
	 */
	public static String getPropertyNameIfSetter(String methodName) {
		if (methodName.startsWith(SET)) {
			String nosetMethodName = methodName.substring(SET.length());
			return StringUtils.uncapitalize(nosetMethodName);

		}
		return null;
	}

	public static Object getPropertyDefaultValue(Class<?> clazz, String propertyName) {
		DirectFieldAccessor fieldAccessor = getEmptyInstanceFieldAccessor(clazz);
		if (!fieldAccessor.isReadableProperty(propertyName)) {
			return null;
		}
		return fieldAccessor.getPropertyValue(propertyName);
	}

	public static Object getPropertyValue(Object object, String propertyName) {
		DirectFieldAccessor fieldAccessor = getEmptyInstanceFieldAccessor(object.getClass());
		if (!fieldAccessor.isReadableProperty(propertyName)) {
			return null;
		}
		return fieldAccessor.getPropertyValue(propertyName);
	}

	private static DirectFieldAccessor getEmptyInstanceFieldAccessor(Class<?> clazz) {
		Object emptyObject = fieldAccessorsCache.get(clazz);
		if (emptyObject == null) {
			try {
				emptyObject = clazz.newInstance();
			} catch (InstantiationException e) {
				throw (new RuntimeException(e));
			} catch (IllegalAccessException e) {
				throw (new RuntimeException(e));
			}

			fieldAccessorsCache.put(clazz, new DirectFieldAccessor(emptyObject));
		}

		DirectFieldAccessor fieldAccessor = fieldAccessorsCache.get(clazz);
		return fieldAccessor;
	}

}
