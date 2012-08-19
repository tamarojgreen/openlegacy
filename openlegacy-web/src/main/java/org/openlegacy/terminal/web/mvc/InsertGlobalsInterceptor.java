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
package org.openlegacy.terminal.web.mvc;

import org.openlegacy.modules.login.Login;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Intercepter class for spring MVC. Injects various globals (login info, etc) into the page context so they can be display within
 * the web page
 * 
 * @author RoiM
 * 
 */
public class InsertGlobalsInterceptor extends AbstractInterceptor {

	@Value("${defaultTheme}")
	private String defaultTheme;

	private static final String OL_THEME = "ol_theme";

	@Override
	protected void insertModelData(ModelAndView modelAndView, HttpServletRequest request, HttpServletResponse response) {

		modelAndView.addObject("ol_version", getClass().getPackage().getImplementationVersion());

		handleTheme(modelAndView, request, response);

		if (!getTerminalSession().isConnected()) {
			return;
		}

		Login loginModule = getTerminalSession().getModule(Login.class);
		if (loginModule.isLoggedIn()) {
			modelAndView.addObject("loggedInUser", loginModule.getLoggedInUser());
		}
	}

	private void handleTheme(ModelAndView modelAndView, HttpServletRequest request, HttpServletResponse response) {
		String theme = defaultTheme;
		String requestTheme = request.getParameter(OL_THEME);

		if (requestTheme != null) {
			response.addCookie(new Cookie(OL_THEME, requestTheme));
			theme = requestTheme;
		} else {
			Cookie cookieTheme = WebUtils.getCookie(request, OL_THEME);
			if (cookieTheme != null) {
				theme = cookieTheme.getValue();
			} else {
				response.addCookie(new Cookie(OL_THEME, theme));
			}
		}
		modelAndView.addObject(OL_THEME, theme);
	}
}
