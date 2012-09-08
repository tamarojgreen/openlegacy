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
import org.springframework.web.servlet.ModelAndView;

import javax.inject.Inject;
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

	@Inject
	private ThemeUtil themeUtil;

	@Override
	protected void insertModelData(ModelAndView modelAndView, HttpServletRequest request, HttpServletResponse response) {

		modelAndView.addObject("ol_version", getClass().getPackage().getImplementationVersion());

		themeUtil.applyTheme(modelAndView, request, response);

		if (!getTerminalSession().isConnected()) {
			return;
		}

		Login loginModule = getTerminalSession().getModule(Login.class);
		if (loginModule.isLoggedIn()) {
			modelAndView.addObject("loggedInUser", loginModule.getLoggedInUser());
		}
	}

}
