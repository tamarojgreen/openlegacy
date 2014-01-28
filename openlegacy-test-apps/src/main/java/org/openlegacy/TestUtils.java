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
package org.openlegacy;

import org.junit.Test;

import java.lang.reflect.Method;

public class TestUtils {

	public static String getTestMethodName() {
		StackTraceElement[] stackElements = Thread.currentThread().getStackTrace();
		String methodName = null;
		for (StackTraceElement stackTraceElement : stackElements) {
			String clsName = stackTraceElement.getClassName();
			methodName = stackTraceElement.getMethodName();
			try {
				Class<?> cls = Class.forName(clsName);
				Method method = cls.getMethod(methodName);
				Test test = method.getAnnotation(Test.class);
				if (test != null) {
					methodName = method.getName();
					break;
				}
			} catch (Exception ex) {
				// do nothing
			}
		}
		return methodName;
	}

}
