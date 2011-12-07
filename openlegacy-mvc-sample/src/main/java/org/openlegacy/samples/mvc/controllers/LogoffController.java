package org.openlegacy.samples.mvc.controllers;

import javax.inject.Inject;

import org.openlegacy.modules.login.Login;
import org.openlegacy.terminal.TerminalSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Handles requests for the application home page.
 */
@Controller
public class LogoffController {

	
	@Inject
	private TerminalSession terminalSession;
	
    @RequestMapping(value="/logoff", method = RequestMethod.GET)
    public String logoff() {
    	terminalSession.getModule(Login.class).logoff();
        return "redirect:/SignOn";
    }
	
}
