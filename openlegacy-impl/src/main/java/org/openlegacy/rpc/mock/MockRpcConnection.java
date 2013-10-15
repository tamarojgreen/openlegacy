/*******************************************************************************
 * Copyright (c) 2012 OpenLegacy Inc.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     OpenLegacy Inc. - initial API and implementation
 *******************************************************************************/
package org.openlegacy.rpc.mock;

import org.openlegacy.rpc.RpcConnection;
import org.openlegacy.rpc.RpcInvokeAction;
import org.openlegacy.rpc.RpcResult;
import org.openlegacy.rpc.RpcSnapshot;

import java.util.List;

public class MockRpcConnection implements RpcConnection {

	private List<RpcSnapshot> snapshots;

	private RpcSnapshot lastSnapshpot;

	public MockRpcConnection(List<RpcSnapshot> rpcSnapshots) {
		snapshots = rpcSnapshots;
	}

	public Object getDelegate() {
		return this;
	}

	public boolean isConnected() {
		return true;
	}

	public void disconnect() {
		// do nothing
	}

	public RpcResult invoke(RpcInvokeAction rpcInvokeAction) {
		if (lastSnapshpot == null || lastSnapshpot.getSequence() >= snapshots.size()) {
			lastSnapshpot = snapshots.get(0);
		} else {
			lastSnapshpot = snapshots.get(lastSnapshpot.getSequence() + 1);
		}
		return lastSnapshpot.getRpcResult();
	}

	public Integer getSequence() {
		if (lastSnapshpot == null) {
			return 0;
		}
		return lastSnapshpot.getSequence();
	}

	public RpcSnapshot getSnapshot() {
		return lastSnapshpot;
	}

	public RpcSnapshot fetchSnapshot() {
		return lastSnapshpot;
	}

	public void doAction(RpcInvokeAction sendAction) {
		// do nothing. TODO implemented verifySend
	}

	public void login(String user, String password) {
		// do nothing
	}

}
