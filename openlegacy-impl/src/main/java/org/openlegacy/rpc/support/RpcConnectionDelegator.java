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
package org.openlegacy.rpc.support;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openlegacy.OpenLegacyProperties;
import org.openlegacy.exceptions.OpenLegacyProviderException;
import org.openlegacy.exceptions.OpenLegacyRuntimeException;
import org.openlegacy.exceptions.SessionEndedException;
import org.openlegacy.rpc.LiveRpcConnectionFactory;
import org.openlegacy.rpc.MockRpcConnectionFactory;
import org.openlegacy.rpc.RpcConnection;
import org.openlegacy.rpc.RpcConnectionFactory;
import org.openlegacy.rpc.RpcInvokeAction;
import org.openlegacy.rpc.RpcResult;
import org.openlegacy.rpc.RpcSnapshot;
import org.springframework.context.ApplicationContext;

import java.io.Serializable;

import javax.inject.Inject;

public class RpcConnectionDelegator implements RpcConnection, Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private transient ApplicationContext applicationContext;

	private RpcConnection rpcConnection;
	private RpcSnapshot rpcSnapshot;

	private final static Log logger = LogFactory.getLog(RpcConnectionDelegator.class);

	@Override
	public RpcSnapshot getSnapshot() {

		lazyConnect();

		if (!isConnected()) {
			disconnect();
			throw (new OpenLegacyRuntimeException("Session is not connected"));
		}

		// clear the snapshot sequence is different from the session, clear it so it will re-build
		if (rpcSnapshot != null && rpcSnapshot.getSequence() != null
				&& !rpcConnection.getSequence().equals(rpcSnapshot.getSequence())) {
			rpcSnapshot = null;
		}

		rpcSnapshot = rpcConnection.fetchSnapshot();

		if (rpcSnapshot == null) {
			throw (new OpenLegacyProviderException("Current rpc result is empty"));
		}
		return rpcSnapshot;
	}

	@Override
	public RpcSnapshot fetchSnapshot() {
		rpcSnapshot = null;
		return getSnapshot();
	}

	@Override
	public void doAction(RpcInvokeAction rpcInvokeAction) {
		lazyConnect();
		rpcSnapshot = null;
		rpcConnection.doAction(rpcInvokeAction);
	}

	@Override
	public Integer getSequence() {
		if (!isConnected()) {
			return 0;
		}
		return rpcConnection.getSequence();
	}

	@Override
	public Object getDelegate() {
		lazyConnect();
		return rpcConnection.getDelegate();
	}

	@Override
	public boolean isConnected() {
		return rpcConnection != null && rpcConnection.isConnected();
	}

	@Override
	public void disconnect() {
		logger.info("Disconnecting session");

		if (rpcConnection == null) {
			logger.debug("Session not connected");
			return;
		}
		rpcConnection.disconnect();
		rpcConnection = null;
		rpcSnapshot = null;

	}

	@Override
	public RpcResult invoke(RpcInvokeAction rpcInvokeAction) {
		lazyConnect();
		return rpcConnection.invoke(rpcInvokeAction);
	}

	@Override
	public void login(String user, String password) {
		lazyConnect();
		rpcConnection.login(user, password);

	}

	private void lazyConnect() {
		if (rpcConnection == null) {
			RpcConnectionFactory rpcConnectionFactory = getConnectionFactory();
			rpcConnection = rpcConnectionFactory.getConnection();
			logger.info("Opened new session");
		}
		if (rpcConnection == null) {
			throw (new SessionEndedException("Session is not connected"));
		}
	}

	private RpcConnectionFactory getConnectionFactory() {
		final OpenLegacyProperties olProperties = applicationContext.getBean(OpenLegacyProperties.class);
		boolean isLiveSession = olProperties.isLiveSession();
		RpcConnectionFactory rpcConnectionFactory;
		if (isLiveSession) {
			rpcConnectionFactory = applicationContext.getBean(LiveRpcConnectionFactory.class);
		} else {
			rpcConnectionFactory = applicationContext.getBean(MockRpcConnectionFactory.class);
		}
		return rpcConnectionFactory;
	}

}
