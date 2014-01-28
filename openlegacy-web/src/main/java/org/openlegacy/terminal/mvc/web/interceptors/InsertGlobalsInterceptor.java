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

import org.openlegacy.modules.globals.Globals;
import org.openlegacy.modules.login.Login;
import org.openlegacy.mvc.MvcUtils;
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
public class InsertGlobalsInterceptor extends AbstractScreensInterceptor {

	@Inject
	private MvcUtils mvcUtils;

	@Override
	protected void insertModelData(ModelAndView modelAndView, HttpServletRequest request, HttpServletResponse response) {

		mvcUtils.insertGlobalData(modelAndView, request, response, getSession());

		Login loginModule = getSession().getModule(Login.class);
		if (loginModule.isLoggedIn()) {
			modelAndView.addObject("ol_loggedInUser", loginModule.getLoggedInUser());
		}
		modelAndView.addObject("ol_globals", getSession().getModule(Globals.class).getGlobals());
	}

}
