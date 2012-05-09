package org.openlegacy.terminal.web.mvc;

import org.openlegacy.modules.login.Login;
import org.springframework.web.servlet.ModelAndView;

/**
 * Intercepter class for spring MVC. Injects various globals (login info, etc) into the page context so they can be display within
 * the web page
 * 
 * @author RoiM
 * 
 */
public class InsertGlobalsInterceptor extends AbstractInterceptor {

	@Override
	protected void insertModelData(ModelAndView modelAndView) {
		Login loginModule = getTerminalSession().getModule(Login.class);
		if (loginModule.isLoggedIn()) {
			modelAndView.addObject("loggedInUser", loginModule.getLoggedInUser());
		}

	}
}
