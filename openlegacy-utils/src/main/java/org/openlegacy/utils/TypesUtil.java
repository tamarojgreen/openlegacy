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
