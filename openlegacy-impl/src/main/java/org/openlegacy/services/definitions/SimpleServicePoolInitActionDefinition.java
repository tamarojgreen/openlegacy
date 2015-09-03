/*******************************************************************************
 * Copyright (c) 2015 OpenLegacy Inc.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     OpenLegacy Inc. - initial API and implementation
 *******************************************************************************/

package org.openlegacy.services.definitions;

import org.openlegacy.services.definitions.ServicePoolInitActionDefinition;
import org.openlegacy.utils.ClassUtils;
import org.openlegacy.utils.SpringUtil;
import org.springframework.context.ApplicationContext;

import java.lang.reflect.Field;

public class SimpleServicePoolInitActionDefinition implements ServicePoolInitActionDefinition {

	String name, user, password;

	Class<?> initActionClass;

	@Override
	public String getName() {
		return name;
	}

	@Override
	public Class<?> getInitActionClass() {
		return initActionClass;
	}

	@Override
	public String getUser() {
		return user;
	}

	@Override
	public String getPassword() {
		return password;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setInitActionClass(Class<?> initActionClass) {
		this.initActionClass = initActionClass;
	}

	@Override
	public void updateActionInstance() {// check for correct working
		ApplicationContext applicationContext = SpringUtil.getApplicationContext();
		if (applicationContext == null) {
			return;
		}
		Object instance = applicationContext.getBean(getName());
		try {
			for (Field field : getClass().getDeclaredFields()) {
				ClassUtils.getWriteMethod(field.getName(), instance.getClass(), field.getType()).invoke(instance, field.get(this));
			}
		} catch (Exception e) {

		}
	}

}
