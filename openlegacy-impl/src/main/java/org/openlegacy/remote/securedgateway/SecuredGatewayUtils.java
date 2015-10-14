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

package org.openlegacy.remote.securedgateway;

import org.openlegacy.exceptions.OpenLegacyRuntimeException;
import org.openlegacy.rpc.LiveRpcConnectionFactory;
import org.openlegacy.rpc.MockRpcConnectionFactory;
import org.openlegacy.terminal.LiveTerminalConnectionFactory;
import org.openlegacy.terminal.MockTerminalConnectionFactory;
import org.openlegacy.utils.SpringUtil;
import org.springframework.context.ApplicationContext;

public class SecuredGatewayUtils {

	public static String getBeanNameForType(Class<?> type) {
		if (type.isAssignableFrom(MockTerminalConnectionFactory.class)) {
			return "mockTerminalConnectionFactory";
		} else if (type.isAssignableFrom(LiveTerminalConnectionFactory.class)) {
			return "liveHostTerminalConnectionFactory";
		} else if (type.isAssignableFrom(MockRpcConnectionFactory.class)) {
			return "mockRpcConnectionFactory";
		} else if (type.isAssignableFrom(LiveRpcConnectionFactory.class)) {
			return "rpcConnectionFactory";
		}
		return null;
	}

	public static Object getProxyBeanByType(Class<?> type) {
		if (SpringUtil.getApplicationContext() == null) {
			throw (new OpenLegacyRuntimeException("Application context not ready yet"));
		}
		return getProxyBeanByType(type, SpringUtil.getApplicationContext());
	}

	public static Object getProxyBeanByType(Class<?> type, ApplicationContext context) {
		try {
			return context.getBean(SecuredGatewayUtils.getBeanNameForType(type));
		} catch (Exception e) {
			return null;
		}

	}
}
