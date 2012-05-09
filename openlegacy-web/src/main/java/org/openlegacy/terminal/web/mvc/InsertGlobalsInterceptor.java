package org.openlegacy.terminal.web.mvc;

import org.openlegacy.modules.login.Login;
import org.openlegacy.terminal.TerminalSession;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

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
public class InsertGlobalsInterceptor extends HandlerInterceptorAdapter {

	@Inject
	private TerminalSession terminalSession;

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView)
			throws Exception {
		if (modelAndView == null) {
			return;
		}

		if (!terminalSession.isConnected()) {
			return;
		}

		if ((modelAndView.getViewName().startsWith("redirect"))) {
			return;
		}

		Login loginModule = terminalSession.getModule(Login.class);
		if (loginModule.isLoggedIn()) {
			modelAndView.addObject("loggedInUser", loginModule.getLoggedInUser());
		}
	}
}
