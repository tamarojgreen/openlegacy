package org.openlegacy.samples.mvc.controllers;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.openlegacy.modules.login.Login;
import org.openlegacy.modules.login.LoginException;
import org.openlegacy.samples.model.SignOn;
import org.openlegacy.terminal.TerminalSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Handles requests for the application home page.
 */
@Controller
public class SignOnController {

	
	@Inject
	private TerminalSession terminalSession;
	
    @RequestMapping(value="/signon", method = RequestMethod.GET)
    public String createForm(Model uiModel) {
        uiModel.addAttribute("signon", new SignOn());
        // JSP view
        return "/signon";
    }
    
    @RequestMapping(value="/signon",method = RequestMethod.POST)
    public String create(SignOn signOn, Model uiModel, HttpServletRequest httpServletRequest) {
        try {
            terminalSession.getModule(Login.class).login(signOn);
		} catch (LoginException e) {
            uiModel.addAttribute("signon", signOn);
            // JSP view
            return "/signon";
		}
        return "redirect:/items";
    }    
	
}
