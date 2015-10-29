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

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class ClassUtils {

	private final static Log logger = LogFactory.getLog(ClassUtils.class);

	private final static String ASPECT = "_Aspect";

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

	public static boolean isPublicMethod(Method m) {
		return Modifier.isPublic(m.getModifiers());
	}

	public static Class<?> getAspectClass(Class<?> clazz) {
		try {
			return Class.forName(String.format("%s%s", clazz.getName(), ASPECT));
		} catch (Exception e) {
		}
		return null;
	}

	public static interface FindInClassProcessor {

		public Object process(Class<?> clazz, Object... args);
	}

	public static Object findInClass(Class<?> clazz, FindInClassProcessor process, Object... args) {
		if (clazz == null) {
			return null;
		}
		Object result;
		result = process.process(clazz, args);
		if (result != null) {
			return result;
		} else {
			for (Class<?> _interface : clazz.getInterfaces()) {
				result = findInClass(_interface, process, args);
				if (result != null) {
					return result;
				}
			}
			result = findInClass(clazz.getSuperclass(), process, args);
		}

		return result;
	}

	private static List<Field> collectDeclaredFields(Class<?> clazz, List<Field> fields) {
		if (clazz == null) {
			return fields;
		}

		fields.addAll(Arrays.asList(clazz.getDeclaredFields()));

		for (Class<?> _interface : clazz.getInterfaces()) {
			collectDeclaredFields(_interface, fields);
		}
		collectDeclaredFields(clazz.getSuperclass(), fields);
		return fields;
	}

	public static List<Field> getDeclaredFields(Class<?> clazz) {
		return collectDeclaredFields(clazz, new ArrayList<Field>());
	}
}