package org.openlegacy.utils;

import java.lang.reflect.Modifier;

public class ClassUtils {

	public static String getImportDeclaration(Class<?> cls) {
		return cls.getName().replace(org.apache.commons.lang.ClassUtils.INNER_CLASS_SEPARATOR_CHAR, '.');
	}
	
	public static boolean isAbstract(Class<?> cls){
		return Modifier.isAbstract(cls.getModifiers());		
	}
	
	public static String packageToResourcePath(String thePackage){
		return thePackage.replaceAll("\\.", "/");
	}
}
