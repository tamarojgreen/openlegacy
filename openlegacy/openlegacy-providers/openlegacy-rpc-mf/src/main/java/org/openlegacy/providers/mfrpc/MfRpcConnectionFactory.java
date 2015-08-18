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
import org.openlegacy.rpc.LiveRpcConnectionFactory;
import org.openlegacy.rpc.RpcConnection;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;

import javax.inject.Inject;

public class MfRpcConnectionFactory implements LiveRpcConnectionFactory, InitializingBean {

	private final static Log logger = LogFactory.getLog(MfRpcConnectionFactory.class);

	@Inject
	private ApplicationContext applicationContext;

	private String cicsUrl;

	private String fontName;

	@Override
	public RpcConnection getConnection() {
		HttpClient httpClient = applicationContext.getBean(HttpClient.class);
		MfRpcConnection rpcConnection = new MfRpcConnection();

		rpcConnection.setHttpClient(httpClient);
		rpcConnection.setFontName(fontName);
		rpcConnection.setUri(cicsUrl);

		return rpcConnection;
	}

	@Override
	public void disconnect(RpcConnection rpcConnection) {
		rpcConnection.disconnect();

	}

	@Override
	public void afterPropertiesSet() throws Exception {

	}

	public String getCicsUrl() {
		return cicsUrl;
	}

	public void setCicsUrl(String cicsUrl) {
		this.cicsUrl = cicsUrl;
	}

	public String getFontName() {
		return fontName;
	}

	public void setFontName(String fontName) {
		this.fontName = fontName;
	}

}
