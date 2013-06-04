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
package org.openlegacy.rpc.mvc.web.interceptors;

import org.openlegacy.Session;
import org.openlegacy.mvc.web.interceptors.AbstractDesigntimeInterceptor;
import org.openlegacy.rpc.RpcSession;

import javax.inject.Inject;

public class DesigntimeInterceptor extends AbstractDesigntimeInterceptor {

	@Inject
	private RpcSession rpcSession;

	@Override
	protected Session getSession() {
		return rpcSession;
	}

}
