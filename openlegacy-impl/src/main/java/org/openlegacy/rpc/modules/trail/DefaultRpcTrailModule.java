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
package org.openlegacy.rpc.modules.trail;

import org.openlegacy.ApplicationConnection;
import org.openlegacy.RemoteAction;
import org.openlegacy.Snapshot;
import org.openlegacy.modules.trail.SessionTrail;
import org.openlegacy.modules.trail.Trail;
import org.openlegacy.rpc.RpcConnection;
import org.openlegacy.rpc.RpcInvokeAction;
import org.openlegacy.rpc.RpcResult;
import org.openlegacy.rpc.RpcSnapshot;
import org.openlegacy.rpc.persistance.RpcPersistedSnapshot;
import org.openlegacy.rpc.support.RpcSessionModuleAdapter;

public class DefaultRpcTrailModule extends RpcSessionModuleAdapter implements Trail {

	private static final long serialVersionUID = 1L;

	private SessionTrail<RpcSnapshot> sessionTrail;

	public SessionTrail<RpcSnapshot> getSessionTrail() {
		return sessionTrail;
	}

	public void beforeConnect(RpcConnection RpcConnection) {
		sessionTrail.clear();
	}

	@Override
	public void afterAction(ApplicationConnection<?, ?> connection, RemoteAction action, Snapshot result) {
		sessionTrail.appendSnapshot(new RpcPersistedSnapshot((RpcInvokeAction)action, (RpcResult)result, connection.getSequence()));
	}

	public void setSessionTrail(SessionTrail<RpcSnapshot> sessionTrail) {
		this.sessionTrail = sessionTrail;
	}

}
