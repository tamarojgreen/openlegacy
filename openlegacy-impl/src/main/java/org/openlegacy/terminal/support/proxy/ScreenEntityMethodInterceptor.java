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
package org.openlegacy.terminal.support.proxy;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.openlegacy.terminal.ScreenPojoFieldAccessor;
import org.openlegacy.terminal.TerminalSession;
import org.openlegacy.terminal.services.ScreenEntitiesRegistry;
import org.openlegacy.terminal.utils.SimpleScreenPojoFieldAccessor;
import org.openlegacy.utils.PropertyUtil;
import org.openlegacy.utils.TypesUtil;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

public class ScreenEntityMethodInterceptor implements MethodInterceptor, Serializable {

	private static final long serialVersionUID = 1L;

	private TerminalSession terminalSession;

	@Inject
	private ScreenEntitiesRegistry screenEntitiesRegistry;

	private List<ScreenEntityProxyHandler> proxyHandlers;

	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {

		Class<?> returnType = invocation.getMethod().getReturnType();

		// exit if return type is primitive
		if (TypesUtil.isPrimitive(returnType) || Collection.class.isAssignableFrom(returnType)) {
			return invocation.proceed();
		}

		if (screenEntitiesRegistry.getPart(returnType) != null) {
			return invocation.proceed();
		}

		String fieldName = PropertyUtil.getPropertyNameIfGetter(invocation.getMethod().getName());

		Object target = invocation.getThis();
		ScreenPojoFieldAccessor fieldAccessor = new SimpleScreenPojoFieldAccessor(target);

		if (!fieldAccessor.isExists(fieldName)) {
			return invocation.proceed();
		}

		// if value already been set - continue

		if (fieldAccessor.getFieldValue(fieldName) != null) {
			return invocation.proceed();
		}

		for (ScreenEntityProxyHandler screenEntityProxyHandler : proxyHandlers) {
			Object result = screenEntityProxyHandler.invoke(terminalSession, invocation);
			if (result != null) {
				fieldAccessor.setFieldValue(fieldName, result);
				break;
			}
		}

		return invocation.proceed();
	}

	public void setTerminalSession(TerminalSession terminalSession) {
		this.terminalSession = terminalSession;
	}

	public void setProxyHandlers(List<ScreenEntityProxyHandler> proxyHandlers) {
		this.proxyHandlers = proxyHandlers;
	}
}
