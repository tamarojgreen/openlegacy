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
package org.openlegacy.providers.jt400;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.Trace;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openlegacy.rpc.LiveRpcConnectionFactory;
import org.openlegacy.rpc.RpcConnection;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;

import javax.inject.Inject;

public class Jt400RpcConnectionFactory implements LiveRpcConnectionFactory, InitializingBean {

	private final static Log logger = LogFactory.getLog(Jt400RpcConnectionFactory.class);

	@Inject
	private ApplicationContext applicationContext;

	@Override
	public RpcConnection getConnection() {
		AS400 as400 = applicationContext.getBean(AS400.class);
		Jt400RpcConnection rpcConnection = new Jt400RpcConnection();
		rpcConnection.setAs400Session(as400);
		return rpcConnection;
	}

	@Override
	public void disconnect(RpcConnection rpcConnection) {
		rpcConnection.disconnect();

	}

	@Override
	public void afterPropertiesSet() throws Exception {
		if (logger.isTraceEnabled()) {
			Trace.setTraceOn(true);
			Trace.setTraceDiagnosticOn(true);
			Trace.setTraceInformationOn(true);
			Trace.setTraceWarningOn(true);
			Trace.setTraceErrorOn(true);
			Trace.setTraceDatastreamOn(true);
			Trace.setTraceThreadOn(true);
			Trace.setTraceJDBCOn(true);
		}
	}

}
