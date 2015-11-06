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

import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.lang.StringUtils;
import org.openlegacy.rpc.LiveRpcConnectionFactory;
import org.openlegacy.rpc.MockRpcConnectionFactory;
import org.openlegacy.terminal.LiveTerminalConnectionFactory;
import org.openlegacy.terminal.MockTerminalConnectionFactory;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.Properties;

@Component
public class SecuredGatewayProperties {

	public static final String RPC = "rpc";
	public static final String TERMINAL = "terminal";

	public static final String ENDPOINT_BEAN_NAME = "remoteConnectionFactory";

	public static final String TERMINAL_MOCK_BEAN_NAME = "mockTerminalConnectionFactory";
	public static final String TERMINAl_LIVE_BEAN_NAME = "liveHostTerminalConnectionFactory";
	public static final String RPC_MOCK_BEAN_NAME = "mockRpcConnectionFactory";
	public static final String RPC_LIVE_BEAN_NAME = "rpcConnectionFactory";

	public static final String FILE_PATH = "/src/main/resources/securedGateway.properties";

	private String host = null;
	private boolean liveSession = false;
	private String solution = null;

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host.endsWith("/") ? StringUtils.removeEnd(host, "") : host;
	}

	public boolean isLiveSession() {
		return liveSession;
	}

	public void setLiveSession(boolean liveSession) {
		this.liveSession = liveSession;
	}

	public String getSolution() {
		return solution;
	}

	public void setSolution(String solution) {
		this.solution = solution.trim().toLowerCase();
	}

	public boolean isRpc() {
		return this.solution.equals(RPC);
	}

	public Class<?> getConnectionFactory() {
		return isRpc() ? (liveSession ? LiveRpcConnectionFactory.class : MockRpcConnectionFactory.class)
				: (liveSession ? LiveTerminalConnectionFactory.class : MockTerminalConnectionFactory.class);
	}

	public Class<?> getConnectionFactoryInterface() {
		if (isRpc()) {
			return isLiveSession() ? LiveRpcConnectionFactory.class : MockRpcConnectionFactory.class;
		} else {
			return isLiveSession() ? LiveTerminalConnectionFactory.class : MockTerminalConnectionFactory.class;
		}
	}

	public String getRemoteConnectionFactoryUrl() {
		return host == null ? null : String.format("%s/%s", host, getRemoteConnectionFactoryBeanName());
	}

	public String getRemoteConnectionFactoryBeanName() {
		return ENDPOINT_BEAN_NAME;
	}

	public String getConnectionFactoryBeanName() {
		if (isRpc()) {
			return isLiveSession() ? RPC_LIVE_BEAN_NAME : RPC_MOCK_BEAN_NAME;
		} else {
			return isLiveSession() ? TERMINAl_LIVE_BEAN_NAME : TERMINAL_MOCK_BEAN_NAME;
		}
	}

	public Properties toProperties() {
		Properties result = new Properties();
		result.put("securedGatewayProperties.host", host);
		result.put("securedGatewayProperties.liveSession", String.valueOf(liveSession));
		result.put("securedGatewayProperties.solution", solution);
		return result;
	}

	public void saveTo(File file) {
		try {
			PropertiesConfiguration config = new PropertiesConfiguration(file);
			config.setProperty("securedGatewayProperties.host", host);
			config.setProperty("securedGatewayProperties.liveSession", String.valueOf(liveSession));
			config.setProperty("securedGatewayProperties.solution", solution);
			config.save();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String getSecuredGatewayProjectName(String name) {
		return String.format("_%s-secured-gateway", name);
	}
}