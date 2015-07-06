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

import org.apache.commons.lang.StringUtils;
import org.openlegacy.exceptions.SessionEndedException;
import org.openlegacy.rpc.RpcConnection;
import org.openlegacy.rpc.RpcField;
import org.openlegacy.rpc.RpcFlatField;
import org.openlegacy.rpc.RpcInvokeAction;
import org.openlegacy.rpc.RpcResult;
import org.openlegacy.rpc.RpcSnapshot;
import org.openlegacy.rpc.support.SimpleRpcInvokeAction;
import org.openlegacy.rpc.support.SimpleRpcStructureField;

import java.util.Iterator;
import java.util.List;

import javax.xml.namespace.QName;

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
		// if (lastSnapshpot == null || lastSnapshpot.getSequence() > snapshots.size()) {WTF???
		// lastSnapshpot = snapshots.get(0);
		// } else {
		// moveToRequiredSnapshot(rpcInvokeAction);
		// // lastSnapshpot = snapshots.get(lastSnapshpot.getSequence() - 1);
		// }
		moveToRequiredSnapshot(rpcInvokeAction);
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

	private void moveToRequiredSnapshot(RpcInvokeAction invokeAction) {
		for (RpcSnapshot rpcSnapshot : snapshots) {
			if (rpcSnapshot.getRpcInvokeAction().getFields().isEmpty() || invokeAction.getFields().isEmpty()) {
				continue;
			}
			if (StringUtils.equals(rpcSnapshot.getRpcInvokeAction().getRpcPath(), invokeAction.getRpcPath())
					&& additionalCheck(rpcSnapshot.getRpcInvokeAction(), invokeAction)) {
				try {
					if (invokeAction instanceof SimpleRpcInvokeAction
							&& ((SimpleRpcInvokeAction)invokeAction).getProperties().size() > 0) {
						String key = ((SimpleRpcInvokeAction)rpcSnapshot.getRpcInvokeAction()).getProperties().get(
								new QName("key"));
						if (key == null) {
							lastSnapshpot = rpcSnapshot;
							break;
						} else {
							Object snaphotKey = getKey(rpcSnapshot.getRpcInvokeAction().getFields(), key);
							Object invokeKey = getKey(invokeAction.getFields(), key);
							if (snaphotKey.getClass().isAssignableFrom(invokeKey.getClass()) && snaphotKey.equals(invokeKey)) {
								lastSnapshpot = rpcSnapshot;
								break;
							}
						}
					} else {// what about another entity structure xD?
						RpcFlatField rpcField = (RpcFlatField)rpcSnapshot.getRpcInvokeAction().getFields().get(0);
						RpcFlatField field = (RpcFlatField)invokeAction.getFields().get(0);
						if (rpcField.getValue().equals(field.getValue())) {
							lastSnapshpot = rpcSnapshot;
							break;
						}
					}
				} catch (Exception e) {
					lastSnapshpot = rpcSnapshot;
				}
			}

		}
	}

	private boolean additionalCheck(RpcInvokeAction snapShotAction, RpcInvokeAction processingAction) {
		if (snapShotAction instanceof SimpleRpcInvokeAction && processingAction instanceof SimpleRpcInvokeAction) {
			SimpleRpcInvokeAction snapshot = (SimpleRpcInvokeAction)snapShotAction;
			SimpleRpcInvokeAction process = (SimpleRpcInvokeAction)processingAction;
			if (snapshot.getProperties() != null) {
				Iterator<QName> iter = snapshot.getProperties().keySet().iterator();
				while (iter.hasNext()) {
					QName key = iter.next();

					if (!process.getProperties().containsKey(key)) {
						return false;
					} else {
						String snapValue = snapshot.getProperties().get(key);
						String processValue = process.getProperties().get(key);
						if (!snapValue.equals(processValue)) {
							return false;
						} else {
							return true;
						}
					}
				}
			}
			return true;
		}
		return true;
	}

	private Object getKey(List<RpcField> fields, String key) {
		for (RpcField field : fields) {
			if (field instanceof RpcFlatField) {
				if (field.getName().equals(key)) {
					return ((RpcFlatField)field).getValue();
				}
			} else if (field instanceof SimpleRpcStructureField) {
				Object obj = getKey(((SimpleRpcStructureField)field).getChildrens(), key);
				if (obj != null) {
					return obj;
				}
			}
		}
		return null;
	}

}
