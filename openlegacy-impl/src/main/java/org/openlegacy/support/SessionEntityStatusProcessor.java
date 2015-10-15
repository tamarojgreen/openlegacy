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

package org.openlegacy.support;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.lang.ClassUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openlegacy.EntitiesRegistry;
import org.openlegacy.db.DbEntity;
import org.openlegacy.definitions.support.AbstractEntityDefinition;
import org.openlegacy.exceptions.OpenLegacyRuntimeException;
import org.openlegacy.rpc.RpcEntity;
import org.openlegacy.terminal.ScreenEntity;

import java.lang.reflect.Method;
import java.util.List;

import javax.inject.Inject;

public class SessionEntityStatusProcessor implements MethodInterceptor {

	private static final String MESSAGE = "Entity %s with %s status used in %s session.";
	private static final String VERIFY = " Verify it usage.";
	private static final String GET_ENTITY = "getEntity";
	private static final String DO_ACTION = "doAction";

	private static final Log logger = LogFactory.getLog(SessionEntityStatusProcessor.class);

	@SuppressWarnings("rawtypes")
	@Inject
	EntitiesRegistry registry;

	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {
		Method method = invocation.getMethod();
		boolean isDoAction = false;

		if (!(method.getName().equals(GET_ENTITY) || method.getName().equals(DO_ACTION))) {
			return invocation.proceed();
		}

		isDoAction = method.getName().equals(DO_ACTION);

		Object[] arguments = invocation.getArguments();

		if (arguments == null || arguments.length == 0) {
			return invocation.proceed();
		}

		if (isDoAction && arguments.length == 1) {
			return invocation.proceed();
		}

		if (isDoAction) {
			@SuppressWarnings("unchecked")
			List<Class<?>> interfaces = ClassUtils.getAllInterfaces(arguments[1].getClass());
			if (!(interfaces.contains(RpcEntity.class) || interfaces.contains(ScreenEntity.class) || interfaces.contains(DbEntity.class))) {
				return invocation.proceed();
			}
		}

		AbstractEntityDefinition<?> entityDef = isDoAction ? getEntityDefinition(arguments[1])
				: getEntityDefinition(arguments[0]);

		Class<?> session = method.getDeclaringClass();
		String message = String.format(MESSAGE, entityDef.getEntityName(), entityDef.getStatus().getAsString(),
				session.getSimpleName());

		if (entityDef.getStatus().isExcluded()) {
			logger.error(message, new OpenLegacyRuntimeException(message));
		} else {
			if (entityDef.getStatus().isLoggable()) {
				logger.info(message + VERIFY);
			}
			return invocation.proceed();
		}

		return null;
	}

	@SuppressWarnings("unchecked")
	private AbstractEntityDefinition<?> getEntityDefinition(Object firstArgument) {
		if (firstArgument instanceof Object) {
			return (AbstractEntityDefinition<?>)registry.get(firstArgument.getClass());
		} else {
			return (AbstractEntityDefinition<?>)(firstArgument instanceof Class ? registry.get((Class<?>)firstArgument)
					: registry.get((String)firstArgument));
		}
	}

}
