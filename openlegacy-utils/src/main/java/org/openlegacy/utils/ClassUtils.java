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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Locale;

public class ClassUtils {

	private final static Log logger = LogFactory.getLog(ClassUtils.class);

	public static String getImportDeclaration(Class<?> cls) {
		return cls.getName().replace(org.apache.commons.lang.ClassUtils.INNER_CLASS_SEPARATOR_CHAR, '.');
	}

	public static boolean isAbstract(Class<?> cls) {
		return Modifier.isAbstract(cls.getModifiers());
	}

	public static String packageToResourcePath(String thePackage) {
		return thePackage.replaceAll("\\.", "/");
	}

	public static Method getReadMethod(String fieldName, Class<?> clazz, Class<?>... args) {
		Method result = getMethod(StringUtil.toJavaMethodName("get" + capitalize(fieldName)), clazz, args);
		if (result == null) {
			result = getMethod(StringUtil.toJavaMethodName("is" + capitalize(fieldName)), clazz, args);
		}
		return result;
	}

	public static Method getWriteMethod(String fieldName, Class<?> clazz, Class<?>... args) {
		return getMethod(StringUtil.toJavaMethodName("set" + capitalize(fieldName)), clazz, args);
	}

	private static Method getMethod(String methodName, Class<?> clazz, Class<?>... args) {
		try {
			return clazz.getMethod(methodName, args);
		} catch (Exception e) {
			if (logger.isDebugEnabled()) {
				logger.error(e.getMessage());
			}

		}
		return null;
	}

	// took from java.beans.NameGenerator
	private static String capitalize(String s) {
		return s.substring(0, 1).toUpperCase(Locale.ENGLISH) + s.substring(1);
	}

	public static boolean isAbstractMethod(Method m) {
		return Modifier.isPublic(m.getModifiers());
	}
}
