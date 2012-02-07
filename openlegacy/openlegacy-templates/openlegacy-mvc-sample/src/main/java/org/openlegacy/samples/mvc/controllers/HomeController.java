package org.openlegacy.samples.mvc.controllers;

import javax.inject.Inject;

import org.openlegacy.modules.login.Login;
import org.openlegacy.terminal.TerminalSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController {

	
	@Inject
	private TerminalSession terminalSession;
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Model model) {
	
		if (!terminalSession.getModule(Login.class).isLoggedIn()){
			return "redirect:/SignOn";
		}
		return "redirect:/ItemsList";
	}

	
}
