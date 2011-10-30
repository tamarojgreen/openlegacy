package org.openlegacy.utils;

public class TypesUtil {

	public static boolean isPrimitive(Class<?> type) {
		if (type.isPrimitive() || type == String.class) {
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

}
