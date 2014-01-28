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
package org.openlegacy.terminal.mvc.web.interceptors;

import org.openlegacy.mvc.web.MvcConstants;
import org.openlegacy.terminal.TerminalSession;
import org.openlegacy.terminal.modules.login.LoginMetadata;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import java.net.URI;
import java.text.MessageFormat;
import java.util.Set;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoginInterceptor extends HandlerInterceptorAdapter {

	@Inject
	private TerminalSession terminalSession;

	@Inject
	private LoginMetadata loginMetadata;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		String requestUri = request.getRequestURI().substring(1);
		if (!request.getRequestURI().contains(".") && !requestUri.toLowerCase().endsWith(MvcConstants.LOGIN_VIEW)
				&& !requestUri.toLowerCase().contains("/management") && !requestUri.endsWith("logoff")
				&& loginMetadata.getLoginScreenDefinition() != null && !terminalSession.isConnected()) {
			String requestedPage = requestUri.substring(requestUri.indexOf("/") + 1);
			@SuppressWarnings("unchecked")
			Set<String> paramNames = request.getParameterMap().keySet();
			for (String paramName : paramNames) {
				requestedPage = MessageFormat.format("{0}&{1}={2}", requestedPage, paramName,
						request.getParameter(paramName).replaceAll(" ", "%20"));
			}
			requestedPage = requestedPage.replaceFirst("&", "?");
			URI uri = new URI(requestedPage);
			response.sendRedirect(request.getContextPath() + "/login?requestedUrl=" + uri.toASCIIString().replaceAll("&", "%26"));
			return false;
		}
		return true;
	}
}
