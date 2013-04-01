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
package org.openlegacy.terminal.mvc.web.interceptors;

import org.openlegacy.OpenLegacyProperties;
import org.openlegacy.modules.globals.Globals;
import org.openlegacy.modules.login.Login;
import org.openlegacy.mvc.OpenLegacyWebProperties;
import org.openlegacy.terminal.mvc.web.ThemeUtil;
import org.springframework.web.servlet.ModelAndView;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Intercepter class for spring MVC. Injects various globals (login info, etc) into the page context so they can be display within
 * the web page\
 * 
 * 
 * @author RoiM
 * 
 */
public class InsertGlobalsInterceptor extends AbstractInterceptor {

	@Inject
	private ThemeUtil themeUtil;

	@Inject
	private OpenLegacyProperties openLegacyProperties;

	@Inject
	private OpenLegacyWebProperties openLegacyWebProperties;
	
	@Override
	protected void insertModelData(ModelAndView modelAndView, HttpServletRequest request, HttpServletResponse response) {

		themeUtil.applyTheme(modelAndView, request, response);

		modelAndView.addObject("openLegacyProperties", openLegacyProperties);
		modelAndView.addObject("openLegacyWebProperties", openLegacyWebProperties);

		if (!getTerminalSession().isConnected()) {
			return;
		}

		modelAndView.addObject("ol_connected", true);

		Login loginModule = getTerminalSession().getModule(Login.class);
		if (loginModule.isLoggedIn()) {
			modelAndView.addObject("ol_loggedInUser", loginModule.getLoggedInUser());
		}
		modelAndView.addObject("ol_globals", getTerminalSession().getModule(Globals.class).getGlobals());
	}

}
