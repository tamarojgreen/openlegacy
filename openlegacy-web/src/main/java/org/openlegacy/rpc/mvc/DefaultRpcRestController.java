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
package org.openlegacy.rpc.mvc;

import org.openlegacy.EntitiesRegistry;
import org.openlegacy.Session;
import org.openlegacy.mvc.AbstractRestController;
import org.openlegacy.rpc.RpcEntity;
import org.openlegacy.rpc.RpcSession;
import org.openlegacy.rpc.services.RpcEntitiesRegistry;
import org.openlegacy.rpc.utils.RpcEntityUtils;
import org.springframework.stereotype.Controller;

import javax.inject.Inject;

/**
 * OpenLegacy default REST API RPC controller. Handles GET/POST requests in the format of JSON or XML. Also handles login /logoff
 * of the host session
 * 
 * @author Roi Mor
 * 
 */
@Controller
public class DefaultRpcRestController extends AbstractRestController {

	@Inject
	private RpcSession rpcSession;

	@Inject
	private RpcEntitiesRegistry rpcEntitiesRegistry;

	@Inject
	private RpcEntityUtils rpcEntityUtils;

	@Override
	protected Session getSession() {
		return rpcSession;
	}

	@Override
	protected EntitiesRegistry<?, ?, ?> getEntitiesRegistry() {
		return rpcEntitiesRegistry;
	}

	@Override
	protected Object sendEntity(Object entity, String action) {
		return rpcEntityUtils.sendEntity(rpcSession, (RpcEntity)entity, action);
	}

}
