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

package org.openlegacy.rpc.mvc.rest;

import org.openlegacy.EntitiesRegistry;
import org.openlegacy.Session;
import org.openlegacy.definitions.ActionDefinition;
import org.openlegacy.modules.login.Login;
import org.openlegacy.modules.roles.Roles;
import org.openlegacy.mvc.AbstractRestController;
import org.openlegacy.rpc.RpcSession;
import org.openlegacy.rpc.services.RpcEntitiesRegistry;

import java.util.List;

import javax.inject.Inject;

/**
 * @author Ivan Bort
 */
public class AbstractRpcRestController extends AbstractRestController {

	public static final String JSON = "application/json";
	public static final String XML = "application/xml";
	public static final String MODEL = "model";

	@Inject
	private RpcSession rpcSession;

	@Inject
	private RpcEntitiesRegistry rpcEntitiesRegistry;

	@Override
	protected Session getSession() {
		return rpcSession;
	}

	@Override
	protected EntitiesRegistry<?, ?, ?> getEntitiesRegistry() {
		return rpcEntitiesRegistry;
	}

	@Override
	protected List<ActionDefinition> getActions(Object entity) {
		// actions for screen exists on the entity. No need to fetch from registry
		return null;
	}

	@Override
	protected Object sendEntity(Object entity, String action) {
		Roles rolesModule = rpcSession.getModule(Roles.class);
		if (rolesModule != null) {
			rolesModule.populateEntity(entity, rpcSession.getModule(Login.class));
		}
		return entity;
	}

}
