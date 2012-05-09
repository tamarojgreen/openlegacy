package org.openlegacy.terminal.web.mvc;

import org.openlegacy.modules.messages.Messages;
import org.openlegacy.terminal.TerminalSession;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Intercepter class for spring MVC. Injects messages into the page context so they can be display within the web page
 * 
 * @author RoiM
 * 
 */
public class InsertMessagesInterceptor extends HandlerInterceptorAdapter {

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

		Messages messagesModule = terminalSession.getModule(Messages.class);
		if (messagesModule == null) {
			return;
		}

		List<String> messages = messagesModule.getMessages();
		if (messages.size() > 0) {
			modelAndView.addObject("messages", messages.toArray());
		}
		messagesModule.resetMessages();

	}
}
