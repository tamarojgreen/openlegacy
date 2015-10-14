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

import org.apache.commons.lang.StringUtils;
import org.openlegacy.rpc.LiveRpcConnectionFactory;
import org.openlegacy.rpc.MockRpcConnectionFactory;
import org.openlegacy.rpc.RpcConnectionFactory;
import org.openlegacy.terminal.LiveTerminalConnectionFactory;
import org.openlegacy.terminal.MockTerminalConnectionFactory;
import org.openlegacy.terminal.TerminalConnectionFactory;
import org.springframework.stereotype.Component;

@Component
public class SecuredGatewayProperties {

	public static final String RPC = "rpc";
	public static final String TERMINAL = "terminal";

	public static final String PROPERTY_PREFIX = "securedGateway.";
	private static final String LIVE = "RemoteLiveConnectionFactory";
	private static final String MOCK = "RemoteMockConnectionFactory";

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
		return isRpc() ? RpcConnectionFactory.class : TerminalConnectionFactory.class;
	}

	public String getRemoteConnectionFactoryUrl() {
		return host == null ? null : String.format("%s/%s", host, getRemoteConnectionFactoryBeanName());
	}

	public String getRemoteConnectionFactoryBeanName() {
		return solution + (liveSession ? LIVE : MOCK);
	}
}