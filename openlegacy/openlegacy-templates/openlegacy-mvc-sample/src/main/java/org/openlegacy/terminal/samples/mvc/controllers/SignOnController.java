package org.openlegacy.terminal.samples.mvc.controllers;

import org.openlegacy.modules.login.Login;
import org.openlegacy.modules.login.LoginException;
import org.openlegacy.terminal.TerminalSession;
import org.openlegacy.terminal.samples.model.SignOn;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

/**
 * Handles requests for screen SignOn
 */
@Controller
@RequestMapping({ "/", "/SignOn" })
public class SignOnController {

	@Inject
	private TerminalSession terminalSession;

	// handle submit action
	@RequestMapping(method = RequestMethod.POST)
	public String submit(SignOn signOn, Model uiModel, HttpServletRequest httpServletRequest) {
		try {
			terminalSession.getModule(Login.class).logoff();
			terminalSession.getModule(Login.class).login(signOn);
		} catch (LoginException e) {
			// errorMessage is assigned within login method
			return "SignOn";
		}

		// mobile
		if (httpServletRequest.getParameter("partial") != null) {
			return "MainMenu";

		}
		return "redirect:Items";
	}

}
