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
package org.openlegacy.rpc.support;

import org.openlegacy.rpc.RpcSession;
import org.openlegacy.rpc.RpcSessionModule;
import org.openlegacy.support.SessionModuleAdapter;
import org.openlegacy.utils.SpringUtil;

import java.io.Serializable;

/**
 * Define an rpc session override-able methods which happens before & after a terminal session action
 * 
 */
public abstract class RpcSessionModuleAdapter extends SessionModuleAdapter<RpcSession> implements RpcSessionModule, Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * for serialization purpose only
	 */
	public RpcSessionModuleAdapter() {}

	public void destroy() {
		// allow override
	}

	public Object readResolve() {
		RpcSession bean = SpringUtil.getApplicationContext().getBean(RpcSession.class);
		setSession(bean);
		return this;
	}
}
