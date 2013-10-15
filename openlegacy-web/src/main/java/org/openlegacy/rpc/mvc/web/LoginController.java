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

import org.openlegacy.mvc.web.MvcConstants;
import org.openlegacy.rpc.RpcSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping(value = { "/login", "/" })
public class LoginController {

	@Inject
	private RpcSession rpcSession;

	@RequestMapping(method = RequestMethod.GET)
	public String home(Model uiModel) {

		if (rpcSession.isConnected()) {
			// MenuItem mainMenu = rpcSession.getModule(Menu.class).getMenuTree();
			return MvcConstants.REDIRECT + MvcConstants.MENU;
		}

		// not connected - create empty login entity for binding
		LoginModel loginModel = new LoginModel();
		uiModel.addAttribute(MvcConstants.LOGIN_MODEL, loginModel);
		return MvcConstants.LOGIN_VIEW;
	}

	@RequestMapping(value = "Login", method = RequestMethod.POST)
	public String login(Model uiModel, HttpServletRequest request,
			@RequestParam(value = "partial", required = false) String partial) {

		rpcSession.disconnect();
		LoginModel loginModel = new LoginModel();
		ServletRequestDataBinder binder = new ServletRequestDataBinder(loginModel);
		binder.bind(request);

		try {
			rpcSession.login(loginModel.getUser(), loginModel.getPassword());
		} catch (Exception e) {
			rpcSession.disconnect();
			loginModel.setErrorMessage(e.getMessage());
			uiModel.addAttribute(MvcConstants.LOGIN_MODEL, loginModel);
			return MvcConstants.LOGIN_VIEW;
		}

		return MvcConstants.REDIRECT + MvcConstants.MENU;
	}
}
