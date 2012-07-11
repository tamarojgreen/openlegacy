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

import org.springframework.util.ClassUtils;

public class TypesUtil {

	public static boolean isPrimitive(Class<?> type) {
		if (ClassUtils.isPrimitiveOrWrapper(type) || type == String.class) {
			return true;
		}
		return false;
	}

	public static boolean isPrimitive(String typeName) {
		// TODO add more type
		if (typeName.equals("int")) {
			return true;
		}
		if (typeName.equals("String")) {
			return true;
		}
		return false;
	}

	public static boolean isNumber(Class<?> type) {
		if (Number.class.isAssignableFrom(type)) {
			return true;
		}
		return false;
	}

	public static boolean isNumberOrString(Class<?> type) {
		return isNumber(type) || type == String.class;
	}
}
