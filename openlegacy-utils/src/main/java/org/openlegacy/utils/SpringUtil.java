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

import org.springframework.context.ApplicationContext;

public class SpringUtil {

	public static ApplicationContext ApplicationContext = null;

	/**
	 * Utility class which relies on project conventions. When the provided class is an implementation class, then return it from
	 * spring context. If not, return bean named: "default"+className
	 * 
	 * @param applicationContext
	 * @param clazz
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getDefaultBean(ApplicationContext applicationContext, Class<T> clazz) {
		if (!clazz.isInterface()) {
			return applicationContext.getBean(clazz);
		}
		return (T)applicationContext.getBean("default" + clazz.getSimpleName());

	}
}
