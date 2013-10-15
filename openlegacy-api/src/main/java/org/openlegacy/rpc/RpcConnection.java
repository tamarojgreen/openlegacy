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
package org.openlegacy.rpc;

import org.openlegacy.ApplicationConnection;

public interface RpcConnection extends ApplicationConnection<RpcSnapshot, RpcInvokeAction> {

	Object getDelegate();

	boolean isConnected();

	void disconnect();

	RpcResult invoke(RpcInvokeAction rpcInvokeAction);

	void login(String user, String password);
}
