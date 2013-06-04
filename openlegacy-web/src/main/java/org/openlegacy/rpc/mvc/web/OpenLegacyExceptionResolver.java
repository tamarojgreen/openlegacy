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
package org.openlegacy.rpc.mvc.web;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openlegacy.exceptions.SessionEndedException;
import org.openlegacy.mvc.MvcUtils;
import org.openlegacy.rpc.RpcSession;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class OpenLegacyExceptionResolver extends SimpleMappingExceptionResolver {

	public static final String RPC_SESSION_WEB_SESSION_ATTRIBUTE_NAME = "scopedTarget.rpcSession";

	@Inject
	private MvcUtils mvcUtils;

	private final static Log logger = LogFactory.getLog(OpenLegacyExceptionResolver.class);

	@Override
	public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
		RpcSession rpcSession = (RpcSession)request.getSession().getAttribute(RPC_SESSION_WEB_SESSION_ATTRIBUTE_NAME);
		if (ex instanceof SessionEndedException) {
			if (rpcSession != null) {
				try {
					rpcSession.disconnect();
				} catch (Exception e) {
					// do nothing
				}
			}
		}

		logger.fatal(ex.getMessage(), ex);

		ModelAndView modelAndView = super.resolveException(request, response, handler, ex);
		mvcUtils.insertGlobalData(modelAndView, request, response, rpcSession);
		return modelAndView;
	}
}
