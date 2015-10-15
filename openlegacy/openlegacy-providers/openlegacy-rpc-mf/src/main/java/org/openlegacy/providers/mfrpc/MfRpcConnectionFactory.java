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
package org.openlegacy.providers.mfrpc;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.client.HttpClient;
import org.openlegacy.exceptions.OpenlegacyRemoteRuntimeException;
import org.openlegacy.rpc.LiveRpcConnectionFactory;
import org.openlegacy.rpc.RpcConnection;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;

import java.rmi.RemoteException;

import javax.inject.Inject;

public class MfRpcConnectionFactory implements LiveRpcConnectionFactory, InitializingBean {

	private final static Log logger = LogFactory.getLog(MfRpcConnectionFactory.class);

	@Inject
	private ApplicationContext applicationContext;

	private String baseUrl;

	public String getBaseUrl() {
		return baseUrl;
	}

	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}

	public String getCodePage() {
		return codePage;
	}

	public void setCodePage(String codePage) {
		this.codePage = codePage;
	}

	private String codePage;

	public RpcConnection getConnection() {
		HttpClient httpClient = applicationContext.getBean(HttpClient.class);
		MfRpcConnection rpcConnection = new MfRpcConnection();

		rpcConnection.setHttpClient(httpClient);
		rpcConnection.setCodePage(codePage);
		rpcConnection.setUri(baseUrl);

		return rpcConnection;
	}

	public void disconnect(RpcConnection rpcConnection) {
		try {
			rpcConnection.disconnect();
		} catch (RemoteException e) {
			throw (new OpenlegacyRemoteRuntimeException(e));
		}

	}

	public void afterPropertiesSet() throws Exception {

	}

}
