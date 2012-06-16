package org.openlegacy.utils;

public class ClassUtils {

	public static String getImportDeclaration(Class<?> cls) {
		return cls.getName().replace(org.apache.commons.lang.ClassUtils.INNER_CLASS_SEPARATOR_CHAR, '.');
	}
}
