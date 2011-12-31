package org.openlegacy.samples.mvc.controllers;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.openlegacy.modules.login.Login;
import org.openlegacy.modules.login.LoginException;
import org.openlegacy.terminal.TerminalSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import apps.inventory.screens.SignOn;

/**
 * Handles requests for the application home page.
 */
@Controller
public class SignOnController {

	
	@Inject
	private TerminalSession terminalSession;
	
    @RequestMapping(value="/SignOn", method = RequestMethod.GET)
    public String createForm(Model uiModel) {
        uiModel.addAttribute("signOn", new SignOn());
        // jspx view
        return "SignOn";
    }
    
    @RequestMapping(value="/SignOn",method = RequestMethod.POST)
    public String create(SignOn signOn, Model uiModel, HttpServletRequest httpServletRequest) {
        try {
            terminalSession.getModule(Login.class).login(signOn);
		} catch (LoginException e) {
            uiModel.addAttribute("signOn", signOn);
            // JSP view
            return "SignOn";
		}
        return "redirect:ItemsList";
    }    
	
}
