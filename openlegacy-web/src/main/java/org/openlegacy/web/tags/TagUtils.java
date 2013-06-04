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
package org.openlegacy.web.tags;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;

import java.util.List;

import javax.servlet.ServletContext;

public class TagUtils {

	public static boolean isAssignable(Class<?> actualClass, String expectedClassName) {
		Class<?> expectedClass;
		try {
			expectedClass = Class.forName(expectedClassName);
		} catch (ClassNotFoundException e) {
			return false;
		}
		return expectedClass.isAssignableFrom(actualClass);
	}

	public static String capFirst(String text) {
		return StringUtils.capitalize(text);
	}

	public static String uncapFirst(String text) {
		return StringUtils.uncapitalize(text);
	}

	public static boolean instanceOf(Object o, String className) {
		boolean returnValue;

		try {
			returnValue = Class.forName(className).isInstance(o);
		}

		catch (ClassNotFoundException e) {
			returnValue = false;
		}

		return returnValue;
	}

	public static void throwException(String s) {
		throw (new IllegalStateException(s));
	}

	public static boolean fileExists(ServletContext application, String file) {
		return TagUtils.class.getResource(file) != null;
	}

	public static String join(List<?> array) {
		return StringUtils.join(array, ",");
	}

	public static boolean hasProperty(Object o, String propertyName) {
		if (o == null || propertyName == null) {
			return false;
		}
		try {
			return PropertyUtils.getPropertyDescriptor(o, propertyName) != null;
		} catch (Exception e) {
			return false;
		}
	}
}