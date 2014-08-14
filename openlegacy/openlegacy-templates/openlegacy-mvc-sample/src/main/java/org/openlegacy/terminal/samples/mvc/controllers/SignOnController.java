package org.openlegacy.terminal.samples.mvc.controllers;

import org.openlegacy.modules.login.Login;
import org.openlegacy.modules.login.LoginException;
import org.openlegacy.modules.menu.Menu;
import org.openlegacy.modules.menu.MenuItem;
import org.openlegacy.terminal.TerminalSession;
import org.openlegacy.terminal.samples.model.SignOn;
import org.openlegacy.terminal.services.ScreenEntitiesRegistry;
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

	@Inject
	private ScreenEntitiesRegistry screenEntitiesRegistry;

	// handle page initial display
	@RequestMapping(method = RequestMethod.GET)
	public String show(Model uiModel) {
		if (terminalSession.isConnected()) {
			MenuItem mainMenu = terminalSession.getModule(Menu.class).getMenuTree();
			if (mainMenu != null) {
				Class<?> mainMenuEntity = mainMenu.getTargetEntity();
				return screenEntitiesRegistry.get(mainMenuEntity).getEntityClassName();
			}
		}
		SignOn signOn = new SignOn();
		uiModel.addAttribute("signOn", signOn);
		return "SignOn";
	}

	// handle submit action
	@RequestMapping(method = RequestMethod.POST)
	public String submit(SignOn signOn, Model uiModel, HttpServletRequest httpServletRequest) {
		try {
			terminalSession.getModule(Login.class).logoff();
			terminalSession.getModule(Login.class).login(signOn);
		} catch (LoginException e) {
			uiModel.addAttribute("signOn", signOn);
			// errorMessage is assigned within login method
			return "SignOn";
		}

		return "redirect:Items";
	}

}
