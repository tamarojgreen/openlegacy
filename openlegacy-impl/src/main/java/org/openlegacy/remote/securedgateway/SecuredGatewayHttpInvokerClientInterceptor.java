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

import org.aopalliance.intercept.MethodInvocation;
import org.springframework.remoting.httpinvoker.HttpInvokerProxyFactoryBean;

public class SecuredGatewayHttpInvokerClientInterceptor extends HttpInvokerProxyFactoryBean {

	private static final int TIMES_TO_RETRY = 10;

	@Override
	public Object invoke(MethodInvocation methodInvocation) throws Throwable {
		Throwable result = null;
		int times = 0;
		while (true) {
			try {
				return super.invoke(methodInvocation);
			} catch (Throwable e) {
				result = e;
				times++;
			}
			try {
				Thread.sleep(10000);
			} catch (Exception e) {
			}

			if (times == TIMES_TO_RETRY) {
				throw (new Throwable(result));
			}
		}
	}
}
