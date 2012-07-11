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
