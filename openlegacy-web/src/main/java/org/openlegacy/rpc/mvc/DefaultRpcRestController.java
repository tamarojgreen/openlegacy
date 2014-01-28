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
package org.openlegacy.rpc.mvc;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openlegacy.EntitiesRegistry;
import org.openlegacy.Session;
import org.openlegacy.exceptions.OpenLegacyRuntimeException;
import org.openlegacy.modules.trail.TrailUtil;
import org.openlegacy.mvc.AbstractRestController;
import org.openlegacy.rpc.RpcEntity;
import org.openlegacy.rpc.RpcSession;
import org.openlegacy.rpc.services.RpcEntitiesRegistry;
import org.openlegacy.rpc.utils.RpcEntityUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;

/**
 * OpenLegacy default REST API RPC controller. Handles GET/POST requests in the format of JSON or XML. Also handles login /logoff
 * of the host session
 * 
 * @author Roi Mor
 * 
 */
@Controller
public class DefaultRpcRestController extends AbstractRestController {

	private static final String USER = "user";
	private static final String PASSWORD = "password";

	private final static Log logger = LogFactory.getLog(DefaultRpcRestController.class);

	@Inject
	private RpcSession rpcSession;

	@Inject
	private RpcEntitiesRegistry rpcEntitiesRegistry;

	@Inject
	private RpcEntityUtils rpcEntityUtils;

	@Inject
	private TrailUtil trailUtil;

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

	@RequestMapping(value = "/login", consumes = { JSON, XML })
	public void login(@RequestParam(USER) String user, @RequestParam(PASSWORD) String password, HttpServletResponse response)
			throws IOException {
		try {
			rpcSession.login(user, password);
		} catch (OpenLegacyRuntimeException e) {
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
		}
		response.setStatus(HttpServletResponse.SC_OK);

	}

	@RequestMapping(value = "/logoff", consumes = { JSON, XML })
	public void logoff(HttpServletResponse response) throws IOException {
		try {
			trailUtil.saveTrail(getSession());
		} catch (Exception e) {
			logger.warn("Failed to save trail - " + e.getMessage(), e);
		} finally {
			getSession().disconnect();
		}

	}
}
