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
package org.openlegacy.rpc.mock;

import org.openlegacy.exceptions.SessionEndedException;
import org.openlegacy.rpc.RpcConnection;
import org.openlegacy.rpc.RpcInvokeAction;
import org.openlegacy.rpc.RpcResult;
import org.openlegacy.rpc.RpcSnapshot;

import java.util.List;

public class MockRpcConnection implements RpcConnection {

	private List<RpcSnapshot> snapshots;

	private RpcSnapshot lastSnapshpot;
	private Integer currentIndex = 0;
	private boolean connected = false;
	private boolean verifySend;

	public boolean isVerifySend() {
		return verifySend;
	}

	public void setVerifySend(boolean verifySend) {
		this.verifySend = verifySend;
	}

	public MockRpcConnection(List<RpcSnapshot> rpcSnapshots) {
		snapshots = rpcSnapshots;
	}

	@Override
	public Object getDelegate() {
		return this;
	}

	@Override
	public boolean isConnected() {
		return connected;
	}

	@Override
	public void disconnect() {

		connected = false;
		lastSnapshpot = null;
	}

	@Override
	public RpcResult invoke(RpcInvokeAction rpcInvokeAction) {
		if (lastSnapshpot == null || lastSnapshpot.getSequence() > snapshots.size()) {
			lastSnapshpot = snapshots.get(0);
		} else {
			lastSnapshpot = snapshots.get(lastSnapshpot.getSequence() - 1);
		}
		currentIndex = lastSnapshpot.getSequence();
		if (verifySend == true) {
			MockRpcSendValidationUtils.validateInvokeAction(lastSnapshpot.getRpcInvokeAction(), rpcInvokeAction);
		}
		return lastSnapshpot.getRpcResult();
	}

	@Override
	public Integer getSequence() {
		if (lastSnapshpot == null) {
			return 0;
		}
		return lastSnapshpot.getSequence();
	}

	@Override
	public RpcSnapshot getSnapshot() {
		return lastSnapshpot;
	}

	@Override
	public RpcSnapshot fetchSnapshot() {
		return lastSnapshpot;
	}

	@Override
	public void doAction(RpcInvokeAction sendAction) {
		currentIndex = currentIndex++ % snapshots.size();
		lastSnapshpot = snapshots.get(currentIndex);

	}

	@Override
	public void login(String user, String password) {
		connected = true;
		currentIndex = 0;
	}

	public List<RpcSnapshot> getSnapshots() {

		return snapshots;
	}

	public void setCurrentIndex(int currentIndex) {

		if (currentIndex >= snapshots.size()) {
			throw (new SessionEndedException("Mock session has been finished"));
		}

		lastSnapshpot = snapshots.get(currentIndex);
	}

}
