package org.openlegacy.util;

public class TypesUtil {

	public static boolean isPrimitive(Class<?> type) {
		if (type.isPrimitive() || type == String.class) {
			return true;
		}
		return false;
	}

}
