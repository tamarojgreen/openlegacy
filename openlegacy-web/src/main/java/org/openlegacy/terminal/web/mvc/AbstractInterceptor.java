package org.openlegacy.terminal.web.mvc;

import org.openlegacy.terminal.TerminalSession;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract class AbstractInterceptor extends HandlerInterceptorAdapter {

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

		insertModelData(modelAndView);
	}

	protected TerminalSession getTerminalSession() {
		return terminalSession;
	}

	protected abstract void insertModelData(ModelAndView modelAndView);
}
