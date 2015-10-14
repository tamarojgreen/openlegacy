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

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import java.rmi.Remote;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;

public class SecuredGatewayConnectionFactoryInterceptor implements MethodInterceptor {

	private static final String GET_CONNECTION = "getConnection";
	private static final String DISCONNECT = "disconnect";
	private static final Map<Remote, Object> connectionMap = new HashMap<Remote, Object>();

	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {

		Object result = null;

		if (invocation.getMethod().getName().equals(GET_CONNECTION)) {
			result = invocation.proceed();
			try {
				Remote remoteConnection = UnicastRemoteObject.exportObject((Remote)result, 0);
				connectionMap.put(remoteConnection, result);
				return remoteConnection;
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if (invocation.getMethod().getName().equals(DISCONNECT)) {
			Object remote = invocation.getArguments()[0];
			invocation.getArguments()[0] = connectionMap.get(remote);// avoid call through network from remote app =)
			connectionMap.remove(remote);
			result = invocation.proceed();
		} else {
			result = invocation.proceed();
		}
		return result;
	}

}
