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

import org.openlegacy.utils.ClassUtils;
import org.openlegacy.utils.PropertyChangedUtils;
import org.openlegacy.utils.PropertyChangedUtils.PropertyChangedEvent;
import org.openlegacy.utils.SpringUtil;
import org.springframework.context.ApplicationContext;

import java.lang.reflect.Field;

public class SimpleServicePoolDefinition implements ServicePoolDefinition {

	private String name;
	private Class<?> poolClass;
	private int maxConnections;
	private long keepAliveInterval, returnSessionsInterval = 100/* def from class */;
	private ServicePoolInitActionDefinition initActionDefinition;

	@Override
	public String getName() {
		return name;
	}

	@Override
	public Class<?> getPoolClass() {
		return poolClass;
	}

	@Override
	public int getMaxConnection() {
		return maxConnections;
	}

	@Override
	public long getKeepAliveInterval() {
		return keepAliveInterval;
	}

	@Override
	public long getReturnSessionsInterval() {
		return returnSessionsInterval;
	}

	public int getMaxConnections() {
		return maxConnections;
	}

	public void setMaxConnections(int maxConnections) {
		this.maxConnections = maxConnections;
		PropertyChangedUtils.sendEvent(new PropertyChangedEvent().setBeanOrClassName("serviceRegistry").setEvent(
				PropertyChangedUtils.getEvent(this, "maxConnections", maxConnections)));
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPoolClass(Class<?> poolClass) {
		this.poolClass = poolClass;
	}

	public void setKeepAliveInterval(long keepAliveInterval) {
		this.keepAliveInterval = keepAliveInterval;
		PropertyChangedUtils.sendEvent(new PropertyChangedEvent().setBeanOrClassName("serviceRegistry").setEvent(
				PropertyChangedUtils.getEvent(this, "keepAliveInterval", keepAliveInterval)));
	}

	public void setReturnSessionsInterval(long returnSessionsInterval) {
		this.returnSessionsInterval = returnSessionsInterval;
		PropertyChangedUtils.sendEvent(new PropertyChangedEvent().setBeanOrClassName("serviceRegistry").setEvent(
				PropertyChangedUtils.getEvent(this, "returnSessionsInterval", keepAliveInterval)));
	}

	@Override
	public void updatePoolInstance() {// check for correct working
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

	@Override
	public ServicePoolInitActionDefinition getInitActionDefinition() {
		return initActionDefinition;
	}

	public void setInitActionDefinition(ServicePoolInitActionDefinition initActionDefinition) {
		this.initActionDefinition = initActionDefinition;
	}
}