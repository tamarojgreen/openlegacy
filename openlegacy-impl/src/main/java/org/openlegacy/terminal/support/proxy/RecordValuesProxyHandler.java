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

import org.aopalliance.intercept.MethodInvocation;
import org.openlegacy.exceptions.OpenLegacyRuntimeException;
import org.openlegacy.terminal.TerminalSession;
import org.openlegacy.terminal.support.binders.MultyScreenTableBindUtil;
import org.openlegacy.utils.PropertyUtil;

import java.io.Serializable;

import javax.inject.Inject;

public class RecordValuesProxyHandler implements ScreenEntityProxyHandler, Serializable {

	private static final long serialVersionUID = 1L;

	private static final String VALUES = "Values";

	@Inject
	private MultyScreenTableBindUtil tableBindUtil;

	@Override
	public Object invoke(TerminalSession terminalSession, MethodInvocation invocation) throws OpenLegacyRuntimeException {
		Object target = invocation.getThis();
		String methodName = invocation.getMethod().getName();
		Object[] arguments = invocation.getArguments();
		String searchText = null;
		if (arguments.length > 0) {
			searchText = arguments[0].toString();
		}

		if (methodName.endsWith(VALUES)) {
			methodName = methodName.substring(0, methodName.length() - VALUES.length());
		}
		String propertyName = PropertyUtil.getPropertyNameIfGetter(methodName);

		Class<?> entityClass = target.getClass();
		return tableBindUtil.getRecords(terminalSession, searchText, entityClass.getSimpleName(), propertyName);
	}

}
